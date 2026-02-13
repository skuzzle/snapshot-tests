package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Allows to customize how structured data is compared in order to check whether a
 * snapshot matches an actual test result.
 *
 * @author Simon Taddiken
 * @see SnapshotSerializer
 * @see StructuredData
 */
@API(status = Status.STABLE)
public interface StructuralAssertions {

    /**
     * Structurally compares two serialized objects. If comparison fails, this method
     * should throw an {@link AssertionError} with a helpful error message.
     * <p>
     * Note that, if you don't throw an instance of
     * <code>org.opentest4j.AssertionFailedError</code> then the framework will take care
     * of rethrowing your assertion failure as an <code>AssertionFailedError</code> to
     * make sure that the diff can be viewed in the IDE's diff viewer.
     *
     * @param storedSnapshot The persisted snapshot.
     * @param serializedActual The serialized actual test result.
     * @throws AssertionError If both objects are not "identical" according to this
     *             implementation.
     * @throws SnapshotException If any kind of technical error occurs during comparison.
     */
    void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError;
}
