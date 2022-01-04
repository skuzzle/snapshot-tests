package de.skuzzle.test.snapshots;

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
public interface StructuredDataBuilder {

    /**
     * Provides the {@link StructuredData} instance which defines the serialization format
     * and the way in which serialized objects are compared.
     *
     * @return The {@link StructuredData} instance.
     */
    StructuredData build();
}
