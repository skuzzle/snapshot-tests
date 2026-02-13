package de.skuzzle.test.snapshots;

import java.nio.charset.Charset;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Defines how an object is serialized into a persistable snapshot String.
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
 * @see StructuredData
 * @see StructuralAssertions
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public interface SnapshotSerializer {

    /**
     * Creates a String representation of the provided object. The passed object is
     * guaranteed to be non-null.
     * <p>
     * It is strongly advised that serializers should produce system-independent output.
     * That is, implementors should NOT use {@link System#lineSeparator()},
     * {@link Charset#defaultCharset()} or similar system dependent information to render
     * the snapshot string.
     *
     * @param testResult The object to serialize.
     * @return The serialized object.
     * @throws SnapshotException If the object could not be serialized for technical
     *             reasons.
     */
    String serialize(Object testResult) throws SnapshotException;
}
