package de.skuzzle.test.snapshots;

/**
 * Allows to customize how structured data is compared in order to check whether a
 * snapshot matches an actual test result.
 *
 * @author Simon Taddiken
 * @since ever
 */
public interface StructuralAssertions {

    /**
     * Structurally compares two serialized objects. If comparison fails, this method
     * should throw an {@link AssertionError} with a helpful error message.
     *
     * @param storedSnapshot The persisted snapshot.
     * @param serializedActual The serialized actual test result.
     * @throws AssertionError If both objects are not "identical" according to this
     *             implementation.
     * @throws SnapshotException If any kind of technical error occurs during comparison.
     */
    void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, SnapshotException;
}
