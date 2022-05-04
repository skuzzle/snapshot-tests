package de.skuzzle.test.snapshots.impl;

import static de.skuzzle.test.snapshots.SnapshotTestResult.forFailedTest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseName;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Aggregates the logic of executing (possibly multiple) snapshot assertions in the
 * context of a single test method.
 *
 * @author Simon Taddiken
 */
final class SnapshotTestImpl implements Snapshot {

    private final Method testMethod;
    private final SnapshotTestContext context;
    private final SnapshotConfiguration configuration;
    private final LocalResultCollector localResultCollector = new LocalResultCollector();

    // XXX: These might be mutated multiple times during the execution of a single test if
    // that test uses multiple snapshot assertions with different explicit
    // naming/directory override.
    private SnapshotNaming namingStrategy = SnapshotNaming.defaultNaming();
    private Path directoryOverride;

    SnapshotTestImpl(SnapshotTestContext context, SnapshotConfiguration configuration, Method testMethod) {
        this.configuration = Arguments.requireNonNull(configuration, "configuration must not be null");
        this.testMethod = Arguments.requireNonNull(testMethod, "testMethod must not be null");
        this.context = context;
    }

    @Override
    public ChooseActual namedAccordingTo(SnapshotNaming namingStrategy) {
        this.namingStrategy = Arguments.requireNonNull(namingStrategy, "namingStrategy must not be null");
        return this;
    }

    @Override
    public ChooseName in(Path directory) {
        this.directoryOverride = Arguments.requireNonNull(directory, "snapshot directory must not be null");
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
                SnapshotHeader.SNAPSHOT_NAME, snapshotName,
                SnapshotHeader.DYNAMIC_DIRECTORY, "" + (this.directoryOverride != null)));
    }

    private Path determineSnapshotFile(String snapshotName) throws IOException {
        final String snapshotFileName = InternalSnapshotNaming.getSnapshotFileName(snapshotName);
        return determineSnapshotDirectory().resolve(snapshotFileName);
    }

    private Path determineSnapshotDirectory() throws IOException {
        final Path snapshotDirectory = this.directoryOverride != null
                ? this.directoryOverride
                : this.configuration.determineSnapshotDirectory();
        Files.createDirectories(snapshotDirectory);
        return snapshotDirectory;
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

        recordSnapshotTestResult(result);

        return result;
    }

    private void recordSnapshotTestResult(final SnapshotTestResult result) {
        this.localResultCollector.recordSnapshotTestResult(result);
        this.context.recordSnapshotTestResult(result);
    }

    SnapshotTestResult executeAssertionWith(SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions,
            Object actual) throws Exception {
        final String snapshotName = namingStrategy.determineSnapshotName(testMethod, localResultCollector.size());
        final Path snapshotFilePath = determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        final boolean forceUpdateSnapshots = configuration.isForceUpdateSnapshotsLocal(testMethod);
        final boolean snapshotFileAlreadyExists = Files.exists(snapshotFilePath);

        final SnapshotHeader snapshotHeader = determineNextSnapshotHeader(snapshotName);

        final SnapshotTestResult result;
        if (forceUpdateSnapshots || !snapshotFileAlreadyExists) {
            final SnapshotFile snapshotFile = SnapshotFile.of(snapshotHeader, serializedActual)
                    .writeTo(snapshotFilePath);

            final SnapshotStatus status = snapshotFileAlreadyExists
                    ? SnapshotStatus.UPDATED_FORCEFULLY
                    : SnapshotStatus.CREATED_INITIALLY;
            result = SnapshotTestResult.of(snapshotFilePath, status, snapshotFile);
        } else {
            final SnapshotFile snapshotFile = readSnapshotFileAndUpdateHeader(snapshotFilePath, snapshotHeader);
            final String storedSnapshot = snapshotFile.snapshot();

            result = compareTestResults(structuralAssertions, storedSnapshot, serializedActual, snapshotFilePath)
                    .map(assertionError -> forFailedTest(snapshotFilePath, snapshotFile, assertionError))
                    .orElseGet(() -> SnapshotTestResult.of(snapshotFilePath, SnapshotStatus.ASSERTED, snapshotFile));
        }
        recordSnapshotTestResult(result);

        if (!configuration.isSoftAssertions()) {
            localResultCollector.assertSuccessEagerly();
        }

        return result;
    }

    private SnapshotFile readSnapshotFileAndUpdateHeader(Path snapshotFilePath, SnapshotHeader newHeader)
            throws IOException {
        SnapshotFile snapshotFile = SnapshotFile.fromSnapshotFile(snapshotFilePath);

        // persistently update the snapshot's header if for example the class or test
        // method has been renamed
        // This happens only if the snapshot was taken with a custom name or custom
        // directory or a new library version introduces changes to the header
        if (!newHeader.equals(snapshotFile.header())) {
            snapshotFile = snapshotFile.changeHeader(newHeader).writeTo(snapshotFilePath);
        }
        return snapshotFile;
    }

    public void executeFinalAssertions() throws Exception {
        localResultCollector.assertSuccess();
    }

    private Optional<Throwable> compareTestResults(StructuralAssertions structuralAssertions, String storedSnapshot,
            String serializedActual, Path snapshotFile) {
        try {
            structuralAssertions.assertEquals(storedSnapshot, serializedActual);
            return Optional.empty();
        } catch (final AssertionError e) {
            final AssertionError diffableAssertionError = toDiffableAssertionError(e, serializedActual, storedSnapshot, snapshotFile);
            return Optional.of(diffableAssertionError);
        } catch (final SnapshotException e) {
            return Optional.of(e);
        }
    }

    private AssertionError toDiffableAssertionError(AssertionError original, String serializedActual,
            String storedSnapshot, Path snapshotFile) {
        final StringBuilder assertionMessage = new StringBuilder();
        if (original.getMessage() != null) {
            assertionMessage.append(original.getMessage());
        }
        assertionMessage.append(System.lineSeparator())
        .append(System.lineSeparator())
        .append("Snapshot location:")
        .append(System.lineSeparator()).append("\t")
        .append(snapshotFile.toString())
        .append(System.lineSeparator());

        final TextDiff testDiff = TextDiff.diffOf(storedSnapshot, serializedActual);
        if (testDiff.hasDifference()) {
            assertionMessage
            .append(System.lineSeparator())
            .append("Full unified diff of actual result and stored snapshot:")
            .append(System.lineSeparator())
            .append(testDiff);
        }
        final AssertionFailedError error = new AssertionFailedError(assertionMessage.toString(), 
                storedSnapshot, serializedActual, original.getCause());
        final String internalPackage = SnapshotTestImpl.class.getPackageName();
        Throwables.filterStackTrace(error, element -> element.getClassName().startsWith(internalPackage));
        return error;
    }

}
