package de.skuzzle.test.snapshots;

/**
 * Information about the creation of a single snapshot file.
 *
 * @author Simon Taddiken
 * @since 0.0.2
 */
public enum SnapshotStatus {
    /**
     * Persistent snapshot file did not exist prior to executing this test. It has now
     * been created.
     */
    CREATED_INITIALLY,
    /**
     * Persisted snapshot has been forcefully updated with the actual test result.
     */
    UPDATED_FORCEFULLY,
    /**
     * Persistent snapshot has been compared against the actual test result.
     */
    ASSERTED
}
