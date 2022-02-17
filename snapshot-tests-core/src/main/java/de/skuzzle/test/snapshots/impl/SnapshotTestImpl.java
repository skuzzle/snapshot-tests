package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.StructuralAssertions;

/**
 * Aggregates the logic of executing (possibly multiple) snapshot assertions in the
 * context of a single test method.
 *
 * @author Simon Taddiken
 */
final class SnapshotTestImpl implements Snapshot, InternalSnapshotTest {

    private final Method testMethod;
    private final SnapshotConfiguration configuration;
    private final LocalResultCollector localResultCollector = new LocalResultCollector();
    private SnapshotNaming namingStrategy = SnapshotNaming.defaultNaming();

    SnapshotTestImpl(SnapshotConfiguration configuration, Method testMethod) {
        this.configuration = Objects.requireNonNull(configuration, "configuration must not be null");
        this.testMethod = Objects.requireNonNull(testMethod, "testMethod must not be null");
    }

    @Override
    public ChooseActual namedAccordingTo(SnapshotNaming namingStrategy) {
        this.namingStrategy = Objects.requireNonNull(namingStrategy, "namingStrategy must not be null");
        return this;
    }

    @Override
    public ChooseDataFormat assertThat(Object actual) {
        return new SnapshotDslImpl(this, actual);
    }

    private SnapshotHeader determineNextSnapshotHeader(String snapshotName) {
        return SnapshotHeader.fromMap(Map.of(
                SnapshotHeader.SNAPSHOT_NUMBER, "" + localResultCollector.size(),
                SnapshotHeader.TEST_METHOD, testMethod.getName(),
                SnapshotHeader.TEST_CLASS, configuration.testClass().getName(),
                SnapshotHeader.SNAPSHOT_NAME, snapshotName));
    }

    private Path determineSnapshotFile(String snapshotName) throws IOException {
        final String snapshotFileName = InternalSnapshotNaming.getSnapshotFileName(snapshotName);
        return configuration.determineSnapshotDirectory().resolve(snapshotFileName);
    }

    SnapshotTestResult justUpdateSnapshotWith(SnapshotSerializer snapshotSerializer, Object actual) throws Exception {
        final String snapshotName = namingStrategy.determineSnapshotName(testMethod, localResultCollector.size());
        final Path snapshotFilePath = determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        final SnapshotHeader snapshotHeader = determineNextSnapshotHeader(snapshotName);
        final SnapshotFile snapshotFile = SnapshotFile.of(snapshotHeader, serializedActual)
                .writeTo(snapshotFilePath);

        final SnapshotTestResult result = SnapshotTestResult.of(snapshotFilePath, SnapshotStatus.UPDATED_FORCEFULLY,
                snapshotFile);

        this.localResultCollector.add(result);
        return result;
    }

    SnapshotTestResult executeAssertionWith(SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions,
            Object actual) throws Exception {
        final String snapshotName = namingStrategy.determineSnapshotName(testMethod, localResultCollector.size());
        final Path snapshotFilePath = determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        final boolean forceUpdateSnapshots = configuration.isForceUpdateSnapshotsLocal(testMethod);
        final boolean snapshotFileAlreadyExists = Files.exists(snapshotFilePath);

        final SnapshotTestResult result;
        if (forceUpdateSnapshots || !snapshotFileAlreadyExists) {
            final SnapshotHeader snapshotHeader = determineNextSnapshotHeader(snapshotName);
            final SnapshotFile snapshotFile = SnapshotFile.of(snapshotHeader, serializedActual)
                    .writeTo(snapshotFilePath);

            final SnapshotStatus status = snapshotFileAlreadyExists
                    ? SnapshotStatus.UPDATED_FORCEFULLY
                    : SnapshotStatus.CREATED_INITIALLY;
            result = SnapshotTestResult.of(snapshotFilePath, status, snapshotFile);
        } else {
            final SnapshotFile snapshotFile = SnapshotFile.fromSnapshotFile(snapshotFilePath);
            final String storedSnapshot = snapshotFile.snapshot();

            result = compareTestResults(structuralAssertions, storedSnapshot, serializedActual)
                    .map(assertionError -> SnapshotTestResult.forFailedTest(snapshotFilePath, snapshotFile,
                            assertionError))
                    .orElseGet(() -> SnapshotTestResult.of(snapshotFilePath, SnapshotStatus.ASSERTED, snapshotFile));
        }
        this.localResultCollector.add(result);

        if (!configuration.isSoftAssertions()) {
            localResultCollector.assertSuccessEagerly();
        }

        return result;
    }

    @Override
    public List<SnapshotTestResult> testResults() {
        return localResultCollector.results();
    }

    @Override
    public void executeAssertions() throws Exception {
        localResultCollector.assertSuccess();
    }

    private Optional<Throwable> compareTestResults(StructuralAssertions structuralAssertions, String storedSnapshot,
            String serializedActual) {
        try {
            structuralAssertions.assertEquals(storedSnapshot, serializedActual);
            return Optional.empty();
        } catch (final AssertionError e) {
            final AssertionError diffableAssertionError = toDiffableAssertionError(e, serializedActual, storedSnapshot);
            return Optional.of(diffableAssertionError);
        } catch (final SnapshotException e) {
            return Optional.of(e);
        }
    }

    private AssertionError toDiffableAssertionError(AssertionError original, String serializedActual,
            String storedSnapshot) {
        if (original instanceof AssertionFailedError) {
            return original;
        }
        return new AssertionFailedError(original.getMessage(), storedSnapshot, serializedActual, original);
    }

}
