package de.skuzzle.test.snapshots;

import java.util.Objects;

/**
 * Combines a {@link SnapshotSerializer} and a {@link StructuralAssertions} instance.
 *
 * @author Simon Taddiken
 * @since ever
 */
public final class StructuredData {

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
}
