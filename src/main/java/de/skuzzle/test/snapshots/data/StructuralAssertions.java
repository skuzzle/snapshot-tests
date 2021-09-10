package de.skuzzle.test.snapshots.data;

public interface StructuralAssertions {

    void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, SnapshotException;
}
