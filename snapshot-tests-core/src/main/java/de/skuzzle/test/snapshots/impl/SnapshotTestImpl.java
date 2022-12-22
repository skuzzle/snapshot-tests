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
import de.skuzzle.test.snapshots.data.text.TextDiff.Settings;
import de.skuzzle.test.snapshots.data.text.TextDiffAssertionError;
import de.skuzzle.test.snapshots.impl.SnapshotAssertionContext.ContextFilePaths;
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

    private ContextFilePaths determineContextFilePaths(String snapshotName) throws IOException {
        return new ContextFilePaths(
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

    private void writeAdditionalContextFiles(ContextFilePaths snapshotFilePaths, SnapshotFile actualSnapshotFile)
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

    private SnapshotAssertionContext newAssertionContext(SnapshotSerializer snapshotSerializer,
            TerminalOperation operation, Object actual)
            throws Exception {
        final String snapshotName = namingStrategy.determineSnapshotName(testMethod, localResultCollector.size());
        final ContextFilePaths contextFilePaths = determineContextFilePaths(snapshotName);
        final Path snapshotFilePath = contextFilePaths.snapshotFile;

        final boolean disableAssertion = operation == TerminalOperation.DISABLE;
        final boolean forceUpdateSnapshots = configuration.isForceUpdateSnapshots(testMethod)
                || operation == TerminalOperation.FORCE_UPDATE;
        final boolean snapshotFileAlreadyExists = Files.exists(snapshotFilePath);

        final SnapshotHeader snapshotHeader = determineNextSnapshotHeader(snapshotName);
        final String serializedActual = actual == null
                ? UNAVAILABLE_BECAUSE_ACTUAL_WAS_NULL
                : snapshotSerializer.serialize(actual);
        final SnapshotFile actualSnapshotFile = SnapshotFile.of(snapshotHeader, serializedActual);

        return new SnapshotAssertionContext(snapshotName,
                contextFilePaths,
                actualSnapshotFile,
                disableAssertion,
                forceUpdateSnapshots,
                snapshotFileAlreadyExists, operation);
    }

    private SnapshotStatus determineStatus(SnapshotAssertionContext snapshotAssertionContext) {
        if (snapshotAssertionContext.isDisableAssertion()) {
            return SnapshotStatus.DISABLED;
        } else if (!snapshotAssertionContext.isSnapshotFileAreadyExists()) {
            return SnapshotStatus.CREATED_INITIALLY;
        } else if (snapshotAssertionContext.isForceUpdateSnapshots()) {
            return SnapshotStatus.UPDATED_FORCEFULLY;
        } else {
            return SnapshotStatus.ASSERTED;
        }
    }

    private boolean decideWriteContextFiles(SnapshotAssertionContext snapshotAssertionContext) {
        return !snapshotAssertionContext.isDisableAssertion();
    }

    private boolean decideCheckNull(SnapshotAssertionContext snapshotAssertionContext) {
        return !snapshotAssertionContext.isDisableAssertion();
    }

    SnapshotTestResult executeTerminalOperation(SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions, TerminalOperation operation,
            Object actual) throws Exception {
        state.reset();

        final SnapshotAssertionContext snapshotAssertionContext = newAssertionContext(
                snapshotSerializer, operation, actual);

        if (decideCheckNull(snapshotAssertionContext)) {
            if (actual == null) {
                throw new AssertionError("Expected actual not to be null in order to take snapshot");
            }
        }

        final SnapshotFile actualSnapshotFile = snapshotAssertionContext.getActualSnapshotFile();

        if (decideWriteContextFiles(snapshotAssertionContext)) {
            writeAdditionalContextFiles(snapshotAssertionContext.getContextFiles(), actualSnapshotFile);
        }

        final String serializedActual = actualSnapshotFile.snapshot();
        final SnapshotStatus status = determineStatus(snapshotAssertionContext);

        final SnapshotTestResult result;
        switch (status) {
        case ASSERTED:
            final SnapshotFile existingSnapshotFile = readSnapshotFileAndUpdateHeader(
                    snapshotAssertionContext.getContextFiles().snapshotFile,
                    snapshotAssertionContext.getActualSnapshotFile().header());

            final String serializedExpected = existingSnapshotFile.snapshot();

            final int lineNumberOffset = configuration.addOffsetToReportedLinenumbers(testMethod)
                    ? snapshotAssertionContext.getActualSnapshotFile().header().lineNumberOffset()
                    : 0;

            result = compareTestResults(structuralAssertions, serializedExpected, serializedActual,
                    snapshotAssertionContext.getContextFiles().snapshotFile, lineNumberOffset)
                            .map(assertionError -> SnapshotTestResult.forFailedTest(
                                    snapshotAssertionContext.getContextFiles().snapshotFile,
                                    snapshotAssertionContext.getContextFiles().latestActualSnapshotFile,
                                    snapshotAssertionContext.getContextFiles().rawSnapshotFile,
                                    existingSnapshotFile, serializedActual, assertionError))
                            .orElseGet(() -> SnapshotTestResult.of(
                                    snapshotAssertionContext.getContextFiles().snapshotFile,
                                    snapshotAssertionContext.getContextFiles().latestActualSnapshotFile,
                                    snapshotAssertionContext.getContextFiles().rawSnapshotFile,
                                    status, existingSnapshotFile, serializedActual));
            break;

        case CREATED_INITIALLY:
        case UPDATED_FORCEFULLY:
            actualSnapshotFile.writeTo(snapshotAssertionContext.getContextFiles().snapshotFile);

            result = SnapshotTestResult.of(snapshotAssertionContext.getContextFiles().snapshotFile,
                    snapshotAssertionContext.getContextFiles().latestActualSnapshotFile,
                    snapshotAssertionContext.getContextFiles().rawSnapshotFile,
                    status, actualSnapshotFile, serializedActual);
            break;
        case DISABLED:
            result = SnapshotTestResult.of(
                    snapshotAssertionContext.getContextFiles().snapshotFile,
                    snapshotAssertionContext.getContextFiles().latestActualSnapshotFile,
                    snapshotAssertionContext.getContextFiles().rawSnapshotFile,
                    status, actualSnapshotFile, serializedActual);
            break;
        default:
            throw new IllegalStateException();
        }

        recordSnapshotTestResult(result);

        if (!configuration.isSoftAssertions()) {
            localResultCollector.assertSuccessEagerly();
        }

        return result;
    }

    private void recordSnapshotTestResult(final SnapshotTestResult result) {
        this.localResultCollector.recordSnapshotTestResult(result);
        this.context.recordSnapshotTestResult(result);
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
            String serializedActual, Path snapshotFile, int lineNumberOffset) {
        try {
            structuralAssertions.assertEquals(storedSnapshot, serializedActual);
            return Optional.empty();
        } catch (final AssertionError e) {
            final AssertionError diffableAssertionError = toDiffableAssertionError(e, serializedActual, storedSnapshot,
                    snapshotFile, lineNumberOffset);
            return Optional.of(diffableAssertionError);
        } catch (final SnapshotException e) {
            return Optional.of(e);
        }
    }

    private AssertionError toDiffableAssertionError(AssertionError original, String serializedActual,
            String storedSnapshot, Path snapshotFile, int lineNumberOffset) {
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
        if (testDiff.differencesDetected()) {
            assertionMessage
                    .append(System.lineSeparator())
                    .append("Full unified diff of actual result and stored snapshot:")
                    .append(System.lineSeparator())
                    .append(testDiff.renderDiffWithOffset(lineNumberOffset));
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
            return TextDiff.compare(
                    Settings.defaultSettings()
                            .withInlineOpeningChangeMarker("~~~~")
                            .withInlineClosingChangeMarker("~~~~")
                            .withContextLines(configuration.textDiffContextLines(testMethod)),
                    storedSnapshot, serializedActual);
        }
    }

}
