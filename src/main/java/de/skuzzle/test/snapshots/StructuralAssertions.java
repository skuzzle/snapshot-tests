package de.skuzzle.test.snapshots;

import org.assertj.core.api.Assertions;

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
     * @implSpec You should <b> not </b> use any variant of <code>assertEquals</code> or
     *           <code>Assertions.assertThat(...).isEqualTo(...)</code> to throw the
     *           AssertionError. IDEs, when they encounter a certain error message format,
     *           allow to view a diff of the actual vs. expected String. This framework
     *           already takes care of providing an error message in the proper format.
     *           Ideally, you should just use {@link Assertions#fail(String)} or throw an
     *           AssertionError with a plain error message.
     */
    void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, SnapshotException;
}
