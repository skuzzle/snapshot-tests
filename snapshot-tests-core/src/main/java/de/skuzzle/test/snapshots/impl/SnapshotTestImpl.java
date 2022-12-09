package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
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
import de.skuzzle.test.snapshots.data.text.TextDiff;
import de.skuzzle.test.snapshots.data.text.TextDiffAssertionError;
import de.skuzzle.test.snapshots.validation.Arguments;
import de.skuzzle.test.snapshots.validation.State;

/**
 * Aggregates the logic of executing (possibly multiple) snapshot assertions in the
 * context of a single test method.
 *
 * @author Simon Taddiken
 */
final class SnapshotTestImpl implements Snapshot {

    // backup snapshot text that will be used in special case when assertions are disabled
    // and null input is given as actual object to snapshot.assertThat(...)
    private static final String UNAVAILABLE_BECAUSE_ACTUAL_WAS_NULL = "<<unavailable because actual was null>>";

    private final Method testMethod;
    private final SnapshotTestContext context;
    private final SnapshotConfiguration configuration;
    private final LocalResultCollector localResultCollector = new LocalResultCollector();

    // XXX: These might be mutated multiple times during the execution of a single test if
    // that test uses multiple snapshot assertions with different explicit
    // naming/directory override.
    private SnapshotNaming namingStrategy = SnapshotNaming.defaultNaming();
    private Path directoryOverride;
    private final DslState state = DslState.initial();

    SnapshotTestImpl(SnapshotTestContext context, SnapshotConfiguration configuration, Method testMethod) {
        this.configuration = Arguments.requireNonNull(configuration, "configuration must not be null");
        this.testMethod = Arguments.requireNonNull(testMethod, "testMethod must not be null");
        this.context = Arguments.requireNonNull(context, "context must not be null");
    }

    @Override
    public ChooseActual namedAccordingTo(SnapshotNaming namingStrategy) {
        state.append(DslState.NAME_CHOSEN);
        this.namingStrategy = Arguments.requireNonNull(namingStrategy, "namingStrategy must not be null");
        return this;
    }

    @Override
    public ChooseName in(Path directory) {
        state.append(DslState.DIRECTORY_CHOSEN);
        this.directoryOverride = Arguments.requireNonNull(directory, "snapshot directory must not be null");
        return this;
    }

    @Override
    public ChooseDataFormat assertThat(Object actual) {
        state.append(DslState.ACTUAL_CHOSEN);
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

    private SnapshotFilePaths determineSnapshotFilePaths(String snapshotName) throws IOException {
        return new SnapshotFilePaths(
                determineSnapshotFile(snapshotName),
                determineSnapshotFileActual(snapshotName),
                determineSnapshotFileRaw(snapshotName));
    }

    private Path determineSnapshotFile(String snapshotName) throws IOException {
        final String snapshotFileName = InternalSnapshotNaming.getSnapshotFileName(snapshotName);
        return determineSnapshotDirectory().resolve(snapshotFileName);
    }

    private Path determineSnapshotFileActual(String snapshotName) throws IOException {
        final String snapshotFileName = InternalSnapshotNaming.getSnapshotFileNameActual(snapshotName);
        return determineSnapshotDirectory().resolve(snapshotFileName);
    }

    private Path determineSnapshotFileRaw(String snapshotName) throws IOException {
        final String snapshotFileName = InternalSnapshotNaming.getSnapshotFileNameRaw(snapshotName);
        return determineSnapshotDirectory().resolve(snapshotFileName);
    }

    private void writeAdditionalContextFiles(SnapshotFilePaths snapshotFilePaths, SnapshotFile actualSnapshotFile)
            throws IOException {
        final Path snapshotFileActual = snapshotFilePaths.latestActualSnapshotFile;
        if (configuration.alwaysPersistActualResult(testMethod)) {
            actualSnapshotFile.writeTo(snapshotFileActual);
        } else {
            Files.deleteIfExists(snapshotFileActual);
        }

        final Path snapshotFileRaw = snapshotFilePaths.rawSnapshotFile;
        if (configuration.alwaysPersistRawResult(testMethod)) {
            Files.writeString(snapshotFileRaw, actualSnapshotFile.snapshot(), StandardCharsets.UTF_8);
        } else {
            Files.deleteIfExists(snapshotFileRaw);
        }
    }

    private Path determineSnapshotDirectory() throws IOException {
        final Path snapshotDirectory = this.directoryOverride != null
                ? this.directoryOverride
                : this.configuration.determineSnapshotDirectory();
        Files.createDirectories(snapshotDirectory);
        return snapshotDirectory;
    }

    SnapshotTestResult justUpdateSnapshotWith(SnapshotSerializer snapshotSerializer, Object actual) throws Exception {
        state.reset();
        if (actual == null) {
            throw new AssertionError("Expected actual not to be null in order to take initial snapshot");
        }

        final String snapshotName = namingStrategy.determineSnapshotName(testMethod, localResultCollector.size());
        final SnapshotFilePaths snapshotFilePaths = determineSnapshotFilePaths(snapshotName);

        final String serializedActual = snapshotSerializer.serialize(actual);

        final SnapshotHeader snapshotHeader = determineNextSnapshotHeader(snapshotName);
        final SnapshotFile snapshotFile = SnapshotFile.of(snapshotHeader, serializedActual)
                .writeTo(snapshotFilePaths.snapshotFile);
        writeAdditionalContextFiles(snapshotFilePaths, snapshotFile);

        final SnapshotTestResult result = SnapshotTestResult.of(
                snapshotFilePaths.snapshotFile,
                snapshotFilePaths.latestActualSnapshotFile,
                snapshotFilePaths.rawSnapshotFile,
                SnapshotStatus.UPDATED_FORCEFULLY, snapshotFile, serializedActual);

        recordSnapshotTestResult(result);

        return result;
    }

    private void recordSnapshotTestResult(final SnapshotTestResult result) {
        this.localResultCollector.recordSnapshotTestResult(result);
        this.context.recordSnapshotTestResult(result);
    }

    SnapshotTestResult disabled(SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions,
            Object actual) throws Exception {
        state.reset();
        final String snapshotName = namingStrategy.determineSnapshotName(testMethod, localResultCollector.size());
        final SnapshotFilePaths snapshotFilePaths = determineSnapshotFilePaths(snapshotName);

        final SnapshotHeader snapshotHeader = determineNextSnapshotHeader(snapshotName);

        final String serializedActual = actual == null
                ? UNAVAILABLE_BECAUSE_ACTUAL_WAS_NULL
                : snapshotSerializer.serialize(actual);

        final SnapshotFile snapshotFile = SnapshotFile.of(snapshotHeader, serializedActual);
        writeAdditionalContextFiles(snapshotFilePaths, snapshotFile);

        final SnapshotTestResult result = SnapshotTestResult.of(
                snapshotFilePaths.snapshotFile,
                snapshotFilePaths.latestActualSnapshotFile,
                snapshotFilePaths.rawSnapshotFile,
                SnapshotStatus.DISABLED, snapshotFile, serializedActual);

        recordSnapshotTestResult(result);
        return result;
    }

    SnapshotTestResult executeAssertionWith(SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions,
            Object actual) throws Exception {
        state.reset();
        final String snapshotName = namingStrategy.determineSnapshotName(testMethod, localResultCollector.size());
        final SnapshotFilePaths snapshotFilePaths = determineSnapshotFilePaths(snapshotName);
        final Path snapshotFilePath = snapshotFilePaths.snapshotFile;

        final boolean forceUpdateSnapshots = configuration.isForceUpdateSnapshots(testMethod);
        final boolean snapshotFileAlreadyExists = Files.exists(snapshotFilePath);

        final SnapshotHeader snapshotHeader = determineNextSnapshotHeader(snapshotName);

        final SnapshotTestResult result;
        if (forceUpdateSnapshots || !snapshotFileAlreadyExists) {
            if (actual == null) {
                throw new AssertionError("Expected actual not to be null in order to take initial snapshot");
            }

            final String serializedActual = snapshotSerializer.serialize(actual);

            final SnapshotFile snapshotFile = SnapshotFile.of(snapshotHeader, serializedActual)
                    .writeTo(snapshotFilePath);

            writeAdditionalContextFiles(snapshotFilePaths, snapshotFile);

            final SnapshotStatus status = snapshotFileAlreadyExists
                    ? SnapshotStatus.UPDATED_FORCEFULLY
                    : SnapshotStatus.CREATED_INITIALLY;
            result = SnapshotTestResult.of(snapshotFilePaths.snapshotFile,
                    snapshotFilePaths.latestActualSnapshotFile,
                    snapshotFilePaths.rawSnapshotFile,
                    status, snapshotFile, serializedActual);
        } else {
            final SnapshotFile snapshotFile = readSnapshotFileAndUpdateHeader(snapshotFilePath, snapshotHeader);
            final String storedSnapshot = snapshotFile.snapshot();

            if (actual == null) {
                throw new AssertionError(
                        "Expected actual not to be null but to match stored snapshot:\n\n" + storedSnapshot);
            }

            final String serializedActual = snapshotSerializer.serialize(actual);

            writeAdditionalContextFiles(snapshotFilePaths, SnapshotFile.of(snapshotHeader, serializedActual));

            result = compareTestResults(structuralAssertions, storedSnapshot, serializedActual, snapshotFilePath)
                    .map(assertionError -> SnapshotTestResult.forFailedTest(
                            snapshotFilePaths.snapshotFile,
                            snapshotFilePaths.latestActualSnapshotFile,
                            snapshotFilePaths.rawSnapshotFile,
                            snapshotFile, serializedActual, assertionError))
                    .orElseGet(() -> SnapshotTestResult.of(
                            snapshotFilePaths.snapshotFile,
                            snapshotFilePaths.latestActualSnapshotFile,
                            snapshotFilePaths.rawSnapshotFile,
                            SnapshotStatus.ASSERTED, snapshotFile, serializedActual));
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
        State.check(state.isInitial(),
                "Detected incomplete DSL usage. Please always call a terminal operation (see JavaDoc of the Snapshot class for details). "
                        + "If you want to temporarily disable a snapshot assertion, use the disabled() terminal operation.");
        localResultCollector.assertSuccess();
    }

    private Optional<Throwable> compareTestResults(StructuralAssertions structuralAssertions, String storedSnapshot,
            String serializedActual, Path snapshotFile) {
        try {
            structuralAssertions.assertEquals(storedSnapshot, serializedActual);
            return Optional.empty();
        } catch (final AssertionError e) {
            final AssertionError diffableAssertionError = toDiffableAssertionError(e, serializedActual, storedSnapshot,
                    snapshotFile);
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

        final TextDiff testDiff = determineDiff(original, storedSnapshot, serializedActual);
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

    private TextDiff determineDiff(AssertionError original, String storedSnapshot, String serializedActual) {
        if (original instanceof TextDiffAssertionError) {
            // this is to reuse the diff that has already been created during text
            // comparison in TextDiffStructuralAssertions
            return ((TextDiffAssertionError) original).textDiff();
        } else {
            return TextDiff.diffOf(storedSnapshot, serializedActual, configuration.textDiffContextLines(testMethod));
        }
    }

    private static class SnapshotFilePaths {
        private final Path snapshotFile;
        private final Path rawSnapshotFile;
        private final Path latestActualSnapshotFile;

        private SnapshotFilePaths(Path snapshotFile, Path latestActualSnapshotFile, Path rawSnapshotFile) {
            this.snapshotFile = snapshotFile;
            this.latestActualSnapshotFile = latestActualSnapshotFile;
            this.rawSnapshotFile = rawSnapshotFile;
        }

    }

}
