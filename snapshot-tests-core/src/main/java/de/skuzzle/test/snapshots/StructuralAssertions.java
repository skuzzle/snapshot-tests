package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.opentest4j.AssertionFailedError;

/**
 * Allows to customize how structured data is compared in order to check whether a
 * snapshot matches an actual test result.
 *
 * @see SnapshotSerializer
 * @see StructuredData
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public interface StructuralAssertions {

    /**
     * Structurally compares two serialized objects. If comparison fails, this method
     * should throw an {@link AssertionError} with a helpful error message.
     * <p>
     * Note that, if you don't throw an instance of {@link AssertionFailedError} then the
     * framework will take care of rethrowing your assertion failure as an
     * {@linkplain AssertionFailedError} to make sure that the diff can be viewed in the
     * IDE's diff viewer.
     *
     * @param storedSnapshot The persisted snapshot.
     * @param serializedActual The serialized actual test result.
     * @throws AssertionError If both objects are not "identical" according to this
     *             implementation.
     * @throws SnapshotException If any kind of technical error occurs during comparison.
     * @apiNote Implementors are advised to throw {@link AssertionFailedError} instead of
     *          plain {@link AssertionError}. This allows the user to see an IDE generated
     *          diff of the actual and expected values in most IDEs.
     */
    void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError;
}
