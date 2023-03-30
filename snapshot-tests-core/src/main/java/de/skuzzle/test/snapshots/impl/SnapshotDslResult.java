package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import de.skuzzle.difftool.DiffRenderer;
import de.skuzzle.difftool.SplitDiffRenderer;
import de.skuzzle.difftool.UnifiedDiffRenderer;
import de.skuzzle.test.snapshots.ContextFiles;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.SnapshotTestOptions.NormalizeLineEndings;
import de.skuzzle.test.snapshots.impl.SnapshotAssertionInput.TerminalOperation;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Aggregates the outcome of a single full DSL invocation. Encapsulates all information
 * that are required to perform a single snapshot assertion and provides the logic to
 * create a {@link SnapshotAssertionInput} object.
 * <p>
 * Some state within this class maybe shared with other assertions within the same test
 * method.
 *
 * @author Simon Taddiken
 */
final class SnapshotDslResult {

    // backup snapshot text that will be used in special case when assertions are disabled
    // and null input is given as actual object to snapshot.assertThat(...)
    private static final String UNAVAILABLE_BECAUSE_ACTUAL_WAS_NULL = "<<unavailable because actual was null>>";

    private final SnapshotConfiguration configuration;
    private final ResultRecorder resultRecorder;
    private final Method testMethod;
    private final SnapshotNaming namingStrategy;
    private final SnapshotSerializer snapshotSerializer;
    private final TerminalOperation operation;

    // Will be null if user decided to use default directory
    private final Path directoryOverride;
    private final Object actual;

    SnapshotDslResult(
            SnapshotConfiguration configuration,
            ResultRecorder resultRecorder,
            Method testMethod,
            SnapshotNaming namingStrategy,
            Object actual,
            TerminalOperation operation,
            SnapshotSerializer snapshotSerializer,
            Path directoryOverride) {

        this.configuration = Arguments.requireNonNull(configuration, "configuration must not be null");
        this.resultRecorder = Arguments.requireNonNull(resultRecorder, "resultRecorder must not be null");
        this.testMethod = Arguments.requireNonNull(testMethod, "testMethod must not be null");
        this.namingStrategy = Arguments.requireNonNull(namingStrategy, "namingStrategy must not be null");

        this.operation = Arguments.requireNonNull(operation, "operation must not be null");
        this.snapshotSerializer = Arguments.requireNonNull(snapshotSerializer, "snapshotSerializer must not be null");

        // only things that gracefully accept null
        this.directoryOverride = directoryOverride;
        this.actual = actual;
    }

    private SnapshotHeader determineNextSnapshotHeader(String snapshotName, int snapshotNumber) {
        return SnapshotHeader.fromMap(Map.of(
                SnapshotHeader.SNAPSHOT_NUMBER, "" + snapshotNumber,
                SnapshotHeader.TEST_METHOD, testMethod.getName(),
                SnapshotHeader.TEST_CLASS, configuration.testClass().getName(),
                SnapshotHeader.SNAPSHOT_NAME, snapshotName,
                SnapshotHeader.DYNAMIC_DIRECTORY, "" + (this.directoryOverride != null)));
    }

    private Path determineSnapshotDirectory() throws IOException {
        final Path snapshotDirectory = this.directoryOverride != null
                ? this.directoryOverride
                : this.configuration.determineSnapshotDirectory();
        Files.createDirectories(snapshotDirectory);
        return snapshotDirectory;
    }

    private ContextFiles determineContextFiles(Path snapshotDirectory, String snapshotName)
            throws IOException {
        final String snapshotFileName = InternalSnapshotNaming.getSnapshotFileName(snapshotName);
        final String actualFileName = InternalSnapshotNaming.getSnapshotFileNameActual(snapshotName);
        final String rawFileName = InternalSnapshotNaming.getSnapshotFileNameRaw(snapshotName);

        return ContextFiles.of(
                snapshotDirectory.resolve(snapshotFileName),
                snapshotDirectory.resolve(actualFileName),
                snapshotDirectory.resolve(rawFileName));
    }

    private DiffRenderer determineDiffRenderer(SnapshotTestOptions.DiffFormat diffFormat) {
        switch (diffFormat) {
        case UNIFIED:
            return UnifiedDiffRenderer.INSTANCE;
        case SPLIT:
            return SplitDiffRenderer.INSTANCE;
        }
        throw new IllegalStateException("Unhandled DiffFormat constant: " + diffFormat);
    }

    SnapshotAssertionInput createAssertionInput() throws Exception {
        final int snapshotNumber = resultRecorder.size();
        final Path snapshotDirectory = determineSnapshotDirectory();
        final String snapshotName = namingStrategy.determineSnapshotName(testMethod, snapshotNumber);
        final ContextFiles contextFilePaths = determineContextFiles(snapshotDirectory, snapshotName);

        final boolean disableAssertion = operation == TerminalOperation.DISABLE;
        final boolean forceUpdateSnapshots = configuration.isForceUpdateSnapshots(testMethod)
                || operation == TerminalOperation.FORCE_UPDATE;

        final boolean snapshotFileAlreadyExists = Files.exists(contextFilePaths.snapshotFile());
        final boolean alwaysPersistActualResult = configuration.alwaysPersistActualResult(testMethod);
        final boolean alwaysPersistRawResult = configuration.alwaysPersistRawResult(testMethod);

        final SnapshotHeader snapshotHeader = determineNextSnapshotHeader(snapshotName, snapshotNumber);

        final String serializedActual;
        final boolean actualWasNull = actual == null;
        if (actualWasNull) {
            serializedActual = UNAVAILABLE_BECAUSE_ACTUAL_WAS_NULL;
        } else {
            final NormalizeLineEndings normalizeLineEndings = configuration.normalizeLineEndings(
                    testMethod);
            final SnapshotSerializer normalizingSerializer = NormalizeLineEndingsSnapshotSerializer.wrap(
                    snapshotSerializer,
                    normalizeLineEndings);
            serializedActual = normalizingSerializer.serialize(actual);
        }
        final SnapshotFile actualSnapshotFile = SnapshotFile.of(snapshotHeader, serializedActual);

        final int lineNumberOffset = configuration.addOffsetToReportedLinenumbers(testMethod)
                ? snapshotHeader.lineNumberOffset()
                : 0;
        final int contextLines = configuration.textDiffContextLines(testMethod);
        final DiffRenderer diffRenderer = determineDiffRenderer(configuration.diffFormat(testMethod));

        return new SnapshotAssertionInput(
                snapshotName,
                contextFilePaths,
                actualSnapshotFile,
                actualWasNull,
                disableAssertion,
                forceUpdateSnapshots,
                snapshotFileAlreadyExists,
                alwaysPersistActualResult,
                alwaysPersistRawResult,
                lineNumberOffset,
                contextLines,
                diffRenderer);
    }
}
