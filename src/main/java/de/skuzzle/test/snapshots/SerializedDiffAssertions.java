package de.skuzzle.test.snapshots;

public interface SerializedDiffAssertions {

    void matchesSnapshotStructure() throws Exception;

    void matchesSnapshotText() throws Exception;

}
