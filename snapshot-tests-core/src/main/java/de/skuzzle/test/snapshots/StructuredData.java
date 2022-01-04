package de.skuzzle.test.snapshots;

import java.util.Objects;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;

/**
 * Combines a {@link SnapshotSerializer} and a {@link StructuralAssertions} instance.
 * <p>
 * This class implements {@link StructuredDataBuilder} so that {@link #build()} always
 * returns <code>this</code>. This allows to pass a concrete instance of this class
 * directly to the snapshot DSL {@link ChooseDataFormat#as(StructuredDataBuilder)}
 *
 * @author Simon Taddiken
 * @since ever
 * @see StructuredDataBuilder
 */
public final class StructuredData implements StructuredDataBuilder {

    private final SnapshotSerializer snapshotSerializer;
    private final StructuralAssertions structuralAssertions;

    private StructuredData(SnapshotSerializer snapshotSerializer, StructuralAssertions structuralAssertions) {
        this.snapshotSerializer = Objects.requireNonNull(snapshotSerializer, "snapshotSerializer must not be null");
        this.structuralAssertions = Objects.requireNonNull(structuralAssertions,
                "structuralAssertions must not be null");
    }

    public static StructuredData with(SnapshotSerializer snapshotSerializer, StructuralAssertions sructuralAssertions) {
        return new StructuredData(snapshotSerializer, sructuralAssertions);
    }

    public SnapshotSerializer snapshotSerializer() {
        return this.snapshotSerializer;
    }

    public StructuralAssertions structuralAssertions() {
        return this.structuralAssertions;
    }

    @Override
    public StructuredData build() {
        return this;
    }
}
