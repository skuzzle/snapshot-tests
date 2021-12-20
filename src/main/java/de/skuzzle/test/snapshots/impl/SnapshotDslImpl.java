package de.skuzzle.test.snapshots.impl;

import java.util.Objects;

import de.skuzzle.test.snapshots.SnapshotDsl.ChoseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseStructure;
import de.skuzzle.test.snapshots.SnapshotResult;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.data.json.JacksonStructuredData;
import de.skuzzle.test.snapshots.data.text.TextDiffStructuralAssertions;
import de.skuzzle.test.snapshots.data.xml.JaxbStructuredData;

class SnapshotDslImpl implements ChoseDataFormat, ChoseStructure, ChoseAssertions {

    private final SnapshotTest snapshot;
    private final Object actual;
    private SnapshotSerializer snapshotSerializer;
    private StructuralAssertions structuralAssertions;

    public SnapshotDslImpl(SnapshotTest snapshot, Object actual) {
        this.snapshot = snapshot;
        this.actual = actual;
    }

    @Override
    public ChoseStructure asXml() {
        return as(JaxbStructuredData.inferJaxbContext(actual).build());
    }

    @Override
    public ChoseStructure asJson() {
        return as(JacksonStructuredData.withDefaultObjectMapper().build());
    }

    @Override
    public ChoseAssertions asText() {
        return as(Object::toString);
    }

    @Override
    public ChoseAssertions as(SnapshotSerializer serializer) {
        this.snapshotSerializer = Objects.requireNonNull(serializer, "serializer must not be null");
        return this;
    }

    @Override
    public ChoseStructure as(StructuredData structure) {
        Objects.requireNonNull(structure, "structure must not be null");
        this.snapshotSerializer = structure.snapshotSerializer();
        this.structuralAssertions = structure.structuralAssertions();
        return this;
    }

    @Override
    public SnapshotResult matchesSnapshotText() {
        return this.matchesAccordingTo(new TextDiffStructuralAssertions());
    }

    @Override
    public SnapshotResult matchesSnapshotStructure() throws Exception {
        return this.matchesAccordingTo(structuralAssertions);
    }

    @Override
    public SnapshotResult matchesAccordingTo(StructuralAssertions structuralAssertions) {
        Objects.requireNonNull(structuralAssertions, "structuralAssertions must not be null");
        try {
            return snapshot.executeAssertionWith(snapshotSerializer, structuralAssertions, actual);
        } catch (final Exception e) {
            throw new IllegalStateException("Technical problem while performing the snapshot assertion", e);
        }
    }

    @Override
    public SnapshotResult justUpdateSnapshot() {
        try {
            return snapshot.justUpdateSnapshotWith(snapshotSerializer, actual);
        } catch (final Exception e) {
            throw new IllegalStateException("Technical problem while updating the snapshot", e);
        }
    }
}
