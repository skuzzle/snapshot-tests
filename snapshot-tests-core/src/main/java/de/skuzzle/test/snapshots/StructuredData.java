package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Combines a {@link SnapshotSerializer} and a {@link StructuralAssertions} instance.
 * <p>
 * This class implements {@link StructuredDataProvider} so that {@link #build()} always
 * returns <code>this</code>. This allows to pass a concrete instance of this class
 * directly to the snapshot DSL {@link ChooseDataFormat#as(StructuredDataProvider)}
 *
 * @author Simon Taddiken
 * @see StructuredDataProvider
 */
@API(status = Status.STABLE)
public final class StructuredData implements StructuredDataProvider {

    private final SnapshotSerializer snapshotSerializer;
    private final StructuralAssertions structuralAssertions;

    private StructuredData(SnapshotSerializer snapshotSerializer, StructuralAssertions structuralAssertions) {
        this.snapshotSerializer = Arguments.requireNonNull(snapshotSerializer, "snapshotSerializer must not be null");
        this.structuralAssertions = Arguments.requireNonNull(structuralAssertions,
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
