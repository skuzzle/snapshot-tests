package de.skuzzle.test.snapshots;

/**
 * Tagging interface for classes that can provide an instance of {@link StructuredData}.
 * This is mostly useful in order for the IDE to list all available snapshot data formats
 * by inspecting the sub-type hierarchy of this interface.
 *
 * @author Simon Taddiken
 */
public interface StructuredDataBuilder {

    StructuredData build();
}
