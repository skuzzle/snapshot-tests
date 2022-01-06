package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import de.skuzzle.test.snapshots.SnapshotResult;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotStatus;
import de.skuzzle.test.snapshots.StructuralAssertions;

/**
 * Aggregates the logic of executing (possibly multiple) snapshot assertions in the
 * context of a single test method.
 *
 * @author Simon Taddiken
 */
final class SnapshotTest implements Snapshot {

    private final Method testMethod;
    private final SnapshotConfiguration configuration;
    private final LocalResultCollector localResultCollector = new LocalResultCollector();
    private String explicitName;

    public SnapshotTest(SnapshotConfiguration configuration, Method testMethod) {
        this.configuration = configuration;
        this.testMethod = testMethod;
    }

    @Override
    public ChooseActual named(String snapshotName) {
        this.explicitName = snapshotName;
        return new SnapshotDslImpl(this, null);
    }

    @Override
    public ChooseDataFormat assertThat(Object actual) {
        return new SnapshotDslImpl(this, actual);
    }

    private String determineNextSnapshotName() {
        if (explicitName != null) {
            return explicitName;
        }
        return SnapshotNaming.getSnapshotName(testMethod, localResultCollector.size());
    }

    private SnapshotHeader determineNextSnapshotHeader() {
        return SnapshotHeader.fromMap(Map.of(
                SnapshotHeader.SNAPSHOT_NUMBER, "" + localResultCollector.size(),
                SnapshotHeader.TEST_METHOD, testMethod.getName(),
                SnapshotHeader.TEST_CLASS, configuration.testClass().getName(),
                SnapshotHeader.SNAPSHOT_NAME, determineNextSnapshotName()));
    }

    private Path determineSnapshotFile(String snapshotName) throws IOException {
        return configuration.determineSnapshotDirectory().resolve(SnapshotNaming.getSnapshotFileName(snapshotName));
    }

    SnapshotResult justUpdateSnapshotWith(SnapshotSerializer snapshotSerializer, Object actual) throws Exception {
        final String snapshotName = determineNextSnapshotName();
        final Path snapshotFile = determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        final SnapshotHeader snapshotHeader = determineNextSnapshotHeader();
        final SnapshotFile xx = SnapshotFile.of(snapshotHeader, serializedActual);
        xx.writeTo(snapshotFile);

        final SnapshotResult result = SnapshotResult.of(snapshotFile, SnapshotStatus.UPDATED_FORCEFULLY,
                xx);

        return this.localResultCollector.add(result);
    }

    SnapshotResult executeAssertionWith(SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions,
            Object actual) throws Exception {
        final String snapshotName = determineNextSnapshotName();
        final Path snapshotFile = determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        final boolean forceUpdateSnapshots = configuration.isForceUpdateSnapshots();
        final boolean snapshotFileAlreadyExists = Files.exists(snapshotFile);

        final SnapshotResult result;
        if (forceUpdateSnapshots || !snapshotFileAlreadyExists) {
            final SnapshotHeader snapshotHeader = determineNextSnapshotHeader();
            final SnapshotFile xx = SnapshotFile.of(snapshotHeader, serializedActual);
            xx.writeTo(snapshotFile);

            final SnapshotStatus status = snapshotFileAlreadyExists
                    ? SnapshotStatus.UPDATED_FORCEFULLY
                    : SnapshotStatus.CREATED_INITIALLY;
            result = SnapshotResult.of(snapshotFile, status, xx);
        } else {
            final SnapshotFile xx = SnapshotFile.fromSnapshotFile(snapshotFile);
            final String storedSnapshot = xx.snapshot();

            result = compareTestResults(structuralAssertions, storedSnapshot, serializedActual)
                    .map(assertionError -> SnapshotResult.forFailedTest(snapshotFile, xx, assertionError))
                    .orElseGet(() -> SnapshotResult.of(snapshotFile, SnapshotStatus.ASSERTED, xx));
        }
        this.localResultCollector.add(result);

        if (!configuration.isSoftAssertions()) {
            localResultCollector.assertSuccessEagerly();
        }

        return result;
    }

    void finalizeTest(GlobalResultCollector globalResultCollector) throws Exception {
        globalResultCollector.addAllFrom(localResultCollector);
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
