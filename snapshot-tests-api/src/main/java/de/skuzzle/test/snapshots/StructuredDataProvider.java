package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Tagging interface for classes that can provide an instance of {@link StructuredData}.
 * This is mostly useful in order for the IDE to list all available snapshot data formats
 * by inspecting the sub-type hierarchy of this interface.
 * <p>
 * Note that {@link StructuredData} itself implements this interface. This allows to pass
 * both a concrete instance of {@linkplain StructuredData} or a dedicated builder
 * implementation to the snapshot DSL.
 *
 * @author Simon Taddiken
 */
@FunctionalInterface
@API(status = Status.STABLE)
public interface StructuredDataProvider {

    /**
     * Provides the {@link StructuredData} instance which defines the serialization format
     * and the way in which serialized objects are compared.
     *
     * @return The {@link StructuredData} instance.
     */
    StructuredData build();
}
