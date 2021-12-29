package de.skuzzle.test.snapshots.impl;

import java.util.Objects;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseStructure;
import de.skuzzle.test.snapshots.SnapshotResult;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.data.json.JacksonStructuredData;
import de.skuzzle.test.snapshots.data.text.TextDiffStructuralAssertions;
import de.skuzzle.test.snapshots.data.xml.JaxbStructuredData;

class SnapshotDslImpl implements ChooseActual, ChoseDataFormat, ChooseStructure, ChooseAssertions {

    private final SnapshotTest snapshot;
    private Object actual;
    private SnapshotSerializer snapshotSerializer;
    private StructuralAssertions structuralAssertions;

    public SnapshotDslImpl(SnapshotTest snapshot, Object actual) {
        this.snapshot = Objects.requireNonNull(snapshot);
        this.actual = actual;
    }

    @Override
    public ChoseDataFormat assertThat(Object actual) {
        this.actual = actual;
        return this;
    }

    @Override
    public ChooseStructure asXml() {
        return as(JaxbStructuredData.inferJaxbContext(actual).build());
    }

    @Override
    public ChooseStructure asJson() {
        return as(JacksonStructuredData.withDefaultObjectMapper().build());
    }

    @Override
    public ChooseAssertions asText() {
        return as(Object::toString);
    }

    @Override
    public ChooseAssertions as(SnapshotSerializer serializer) {
        this.snapshotSerializer = Objects.requireNonNull(serializer, "serializer must not be null");
        return this;
    }

    @Override
    public ChooseStructure as(StructuredData structure) {
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
