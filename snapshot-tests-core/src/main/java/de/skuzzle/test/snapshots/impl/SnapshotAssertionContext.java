package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;

import de.skuzzle.test.snapshots.SnapshotFile;

final class SnapshotAssertionContext {
    private final String snapshotName;
    private final ContextFilePaths contextFiles;
    private final SnapshotFile actualSnapshotFile;
    private final boolean forceUpdateSnapshots;
    private final boolean snapshotFileAreadyExists;
    private final boolean disableAssertion;
    private final TerminalOperation terminalOperation;

    public SnapshotAssertionContext(String snapshotName, ContextFilePaths contextFiles, SnapshotFile actualSnapshotFile,
            boolean disableAssertion, boolean forceUpdateSnapshots, boolean snapshotFileAreadyExists,
            TerminalOperation terminalOperation) {
        this.snapshotName = snapshotName;
        this.contextFiles = contextFiles;
        this.forceUpdateSnapshots = forceUpdateSnapshots;
        this.snapshotFileAreadyExists = snapshotFileAreadyExists;
        this.disableAssertion = disableAssertion;
        this.terminalOperation = terminalOperation;
        this.actualSnapshotFile = actualSnapshotFile;
    }

    public SnapshotFile getActualSnapshotFile() {
        return actualSnapshotFile;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public TerminalOperation getTerminalOperation() {
        return terminalOperation;
    }

    public ContextFilePaths getContextFiles() {
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

    static class ContextFilePaths {
        final Path snapshotFile;
        final Path rawSnapshotFile;
        final Path latestActualSnapshotFile;

        ContextFilePaths(Path snapshotFile, Path latestActualSnapshotFile, Path rawSnapshotFile) {
            this.snapshotFile = snapshotFile;
            this.latestActualSnapshotFile = latestActualSnapshotFile;
            this.rawSnapshotFile = rawSnapshotFile;
        }

    }
}
