package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.StructuralAssertions;

/**
 * Holds all the input that is required to execute a single terminal snapshot operation.
 * These information are resolved from the values configured via the DSL as well as
 * contextual information from the surrounding test method and test class.
 *
 * @author Simon
 */
final class SnapshotAssertionInput {

    private final String snapshotName;
    private final ContextFilePaths contextFiles;
    private final SnapshotFile actualSnapshotFile;
    private final boolean softAssertions;
    private final boolean actualWasNull;
    private final boolean forceUpdateSnapshots;
    private final boolean snapshotFileAreadyExists;
    private final boolean disableAssertion;
    private final boolean alwaysPersistActualResult;
    private final boolean alwaysPersistRawResult;
    private final int lineNumberOffset;
    private final int contextLines;

    SnapshotAssertionInput(String snapshotName,
            ContextFilePaths contextFiles,
            SnapshotFile actualSnapshotFile,
            boolean softAssertions,
            boolean actualWasNull,
            boolean disableAssertion,
            boolean forceUpdateSnapshots,
            boolean snapshotFileAreadyExists,
            boolean alwaysPersistActualResult,
            boolean alwaysPersistRawResult,
            int lineNumberOffset,
            int contextLines) {
        this.snapshotName = snapshotName;
        this.contextFiles = contextFiles;
        this.softAssertions = softAssertions;
        this.actualWasNull = actualWasNull;
        this.forceUpdateSnapshots = forceUpdateSnapshots;
        this.snapshotFileAreadyExists = snapshotFileAreadyExists;
        this.disableAssertion = disableAssertion;
        this.alwaysPersistActualResult = alwaysPersistActualResult;
        this.alwaysPersistRawResult = alwaysPersistRawResult;
        this.actualSnapshotFile = actualSnapshotFile;
        this.lineNumberOffset = lineNumberOffset;
        this.contextLines = contextLines;
    }

    SnapshotTestResult executeWith(StructuralAssertions structuralAssertions, ExecutionLifecycle lifecycle)
            throws Exception {
        lifecycle.beforeExecution(this);

        final SnapshotTestResult testResult = lifecycle.execute(this, structuralAssertions);

        lifecycle.afterExecution(this, testResult);
        return testResult;
    }

    @Deprecated
    public boolean isSoftAssertions() {
        return softAssertions;
    }

    public boolean actualWasNull() {
        return actualWasNull;
    }

    public SnapshotFile actualSnapshotFile() {
        return actualSnapshotFile;
    }

    public String snapshotName() {
        return snapshotName;
    }

    public ContextFilePaths contextFiles() {
        return contextFiles;
    }

    public boolean isForceUpdateSnapshots() {
        return forceUpdateSnapshots;
    }

    public boolean isSnapshotFileAreadyExists() {
        return snapshotFileAreadyExists;
    }

    public boolean isDisableAssertion() {
        return disableAssertion;
    }

    public boolean alwaysPersistActualResult() {
        return alwaysPersistActualResult;
    }

    public boolean isAlwaysPersistRawResult() {
        return alwaysPersistRawResult;
    }

    public int lineNumberOffset() {
        return lineNumberOffset;
    }

    public int contextLines() {
        return contextLines;
    }

    enum TerminalOperation {
        ASSERT,
        DISABLE,
        FORCE_UPDATE
    }

    static final class ContextFilePaths {
        final Path snapshotFile;
        final Path rawSnapshotFile;
        final Path latestActualSnapshotFile;

        private ContextFilePaths(Path snapshotFile, Path latestActualSnapshotFile, Path rawSnapshotFile) {
            this.snapshotFile = snapshotFile;
            this.latestActualSnapshotFile = latestActualSnapshotFile;
            this.rawSnapshotFile = rawSnapshotFile;
        }

        public static ContextFilePaths determineContextFilePaths(Path snapshotDirectory, String snapshotName)
                throws IOException {
            final String snapshotFileName = InternalSnapshotNaming.getSnapshotFileName(snapshotName);
            final String actualFileName = InternalSnapshotNaming.getSnapshotFileNameActual(snapshotName);
            final String rawFileName = InternalSnapshotNaming.getSnapshotFileNameRaw(snapshotName);

            return new ContextFilePaths(
                    snapshotDirectory.resolve(snapshotFileName),
                    snapshotDirectory.resolve(actualFileName),
                    snapshotDirectory.resolve(rawFileName));
        }
    }
}
