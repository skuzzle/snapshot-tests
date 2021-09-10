package de.skuzzle.test.snapshots;

import de.skuzzle.test.snapshots.SnapshotDsl.ChoseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseStructure;
import de.skuzzle.test.snapshots.data.SnapshotSerializer;
import de.skuzzle.test.snapshots.data.StructuralAssertions;
import de.skuzzle.test.snapshots.data.StructuredData;
import de.skuzzle.test.snapshots.data.json.JacksonStructuredData;
import de.skuzzle.test.snapshots.data.text.TextDiffStructuralAssertions;
import de.skuzzle.test.snapshots.data.xml.JaxbStructuredData;

class SnapshotDslImpl implements ChoseDataFormat, ChoseStructure, ChoseAssertions {

    private final SnapshotImpl snapshot;
    private final Object actual;
    private SnapshotSerializer snapshotSerializer;
    private StructuralAssertions structuralAssertions;

    public SnapshotDslImpl(SnapshotImpl snapshot, Object actual) {
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
    public ChoseAssertions as(SnapshotSerializer serializer) {
        this.snapshotSerializer = serializer;
        return this;
    }

    @Override
    public ChoseStructure as(StructuredData structure) {
        this.snapshotSerializer = structure.snapshotSerializer();
        this.structuralAssertions = structure.structuralAssertions();
        return this;
    }

    @Override
    public void matchesSnapshotText() throws Exception {
        this.structuralAssertions = new TextDiffStructuralAssertions();
        this.matchesSnapshotStructure();
    }

    @Override
    public void matchesAccordingTo(StructuralAssertions structuralAssertions) throws Exception {
        this.structuralAssertions = structuralAssertions;
        this.matchesSnapshotStructure();
    }

    @Override
    public void matchesSnapshotStructure() throws Exception {
        new SnapshotTestExecutor(snapshot, snapshotSerializer, structuralAssertions, actual)
                .matchesSnapshotStructure();
    }

    @Override
    public ChoseStructure justUpdateSnapshot() throws Exception {
        new SnapshotTestExecutor(snapshot, snapshotSerializer, structuralAssertions, actual)
                .justUpdateSnapshot();
        return this;
    }
}
