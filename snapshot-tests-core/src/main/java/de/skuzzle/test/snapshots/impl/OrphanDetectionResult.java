package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;

/**
 * OrphanStatus of testing a single snapshot file for whether it is orphaned or not.
 *
 * @author Simon Taddiken
 */
final class OrphanDetectionResult {

    private final String detectorName;
    private final Path snapshotFile;
    private final OrphanStatus result;

    OrphanDetectionResult(String detectorName, Path snapshotFile, OrphanStatus result) {
        this.detectorName = detectorName;
        this.snapshotFile = snapshotFile.toAbsolutePath();
        this.result = result;
    }

    public Path snapshotFile() {
        return this.snapshotFile;
    }

    public OrphanStatus status() {
        return this.result;
    }

    enum OrphanStatus {
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
