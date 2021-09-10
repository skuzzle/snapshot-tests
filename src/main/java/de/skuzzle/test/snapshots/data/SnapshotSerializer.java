package de.skuzzle.test.snapshots.data;

public interface SnapshotSerializer {

    String serialize(Object testResult) throws SnapshotException;
}
