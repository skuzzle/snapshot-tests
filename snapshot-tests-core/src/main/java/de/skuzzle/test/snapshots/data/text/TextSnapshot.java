package de.skuzzle.test.snapshots.data.text;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataBuilder;

/**
 * Take snapshots using {@link Object#toString()}. Use the static instance
 * {@link TextSnapshot#text}.
 *
 * @author Simon Taddiken
 */
public final class TextSnapshot implements StructuredDataBuilder {

    /**
     * Take Snapshots using {@link Object#toString()} and compare the results using a
     * generic String diff algorithm.
     */
    public static final StructuredData text = new TextSnapshot().build();

    private final SnapshotSerializer toString = Object::toString;
    private final StructuralAssertions assertions = new TextDiffStructuralAssertions();

    private TextSnapshot() {
        // hidden
    }

    @Override
    public StructuredData build() {
        return StructuredData.with(toString, assertions);
    }

}
