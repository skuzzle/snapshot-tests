package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.ContextFiles;
import de.skuzzle.test.snapshots.SnapshotFile;

/**
 * Holds all the input that is required to execute a single terminal snapshot operation.
 * These information are resolved from the values configured via the DSL as well as
 * contextual information from the surrounding test method and test class.
 * <p>
 * Instances are created from {@link SnapshotDslResult#createAssertionInput()}.
 *
 * @author Simon
 * @since 1.8.0
 */
final class SnapshotAssertionInput {

    private final String snapshotName;
    private final ContextFiles contextFiles;
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

    private final boolean interactive;

    SnapshotAssertionInput(String snapshotName,
            ContextFiles contextFiles,
            SnapshotFile actualSnapshotFile,
            boolean softAssertions,
            boolean actualWasNull,
            boolean disableAssertion,
            boolean forceUpdateSnapshots,
            boolean snapshotFileAreadyExists,
            boolean alwaysPersistActualResult,
            boolean alwaysPersistRawResult,
            int lineNumberOffset,
            int contextLines,
            boolean interactive) {
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
        this.interactive = interactive;
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

    public ContextFiles contextFiles() {
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

    public boolean interactive() {
        return interactive;
    }

    enum TerminalOperation {
        ASSERT,
        DISABLE,
        FORCE_UPDATE
    }

}
