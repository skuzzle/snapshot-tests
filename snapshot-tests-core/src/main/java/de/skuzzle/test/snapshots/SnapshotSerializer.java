package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Defines how an object is serialized into a persistable String.
 * <p>
 * Serializers are closely related to {@link StructuralAssertions}. The framework always
 * uses a combination of these two in order to perform a snapshot assertion. When
 * customizing, you should make sure that your implementations are compatible. For
 * example, it would <em>not</em> make sense to serialize objects to json and then use
 * XmlUnit to perform the structural assertions.
 * <p>
 * The serialized string will be persisted as 'snapshot' part within a
 * {@link SnapshotFile}.
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public interface SnapshotSerializer {

    /**
     * Creates a String representation of the provided object.
     *
     * @param testResult The object to serialize.
     * @return The serialized object.
     * @throws SnapshotException If the object could not be serialized for technical
     *             reasons.
     */
    String serialize(Object testResult) throws SnapshotException;
}
