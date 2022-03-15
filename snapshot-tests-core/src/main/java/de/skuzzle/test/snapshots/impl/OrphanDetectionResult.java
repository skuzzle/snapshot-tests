package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;

/**
 * Result of testing a single snapshot file for whether it is orphaned or not.
 *
 * @author Simon Taddiken
 */
final class OrphanDetectionResult {

    private final String detectorName;
    private final Path snapshotFile;
    private final Result result;

    OrphanDetectionResult(String detectorName, Path snapshotFile, Result result) {
        this.detectorName = detectorName;
        this.snapshotFile = snapshotFile;
        this.result = result;
    }

    public Path snapshotFile() {
        return this.snapshotFile;
    }

    public Result result() {
        return this.result;
    }

    enum Result {
        ACTIVE,
        ORPHAN,
        UNSURE
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("detectorName=").append(detectorName)
                .append(", snapshotFile=").append(snapshotFile)
                .append(", result=").append(result)
                .toString();
    }
}
