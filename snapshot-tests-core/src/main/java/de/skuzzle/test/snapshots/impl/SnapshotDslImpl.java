package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseStructure;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;
import de.skuzzle.test.snapshots.data.text.TextSnapshot;
import de.skuzzle.test.snapshots.validation.Arguments;

final class SnapshotDslImpl implements ChooseActual, ChooseDataFormat, ChooseStructure, ChooseAssertions {

    private final SnapshotTestImpl snapshot;
    private Object actual;
    private SnapshotSerializer snapshotSerializer = TextSnapshot.text.snapshotSerializer();
    private StructuralAssertions structuralAssertions = TextSnapshot.text.structuralAssertions();

    SnapshotDslImpl(SnapshotTestImpl snapshot, Object actual) {
        this.snapshot = Arguments.requireNonNull(snapshot);
        this.actual = actual;
    }

    @Override
    public ChooseDataFormat assertThat(Object actual) {
        this.actual = actual;
        return this;
    }

    @Override
    public ChooseAssertions asText() {
        return as(TextSnapshot.text);
    }

    @Override
    public ChooseAssertions as(SnapshotSerializer serializer) {
        this.snapshotSerializer = Arguments.requireNonNull(serializer, "serializer must not be null");
        return this;
    }

    @Override
    public ChooseStructure as(StructuredDataProvider structuredDataBuilder) {
        final StructuredData structure = Arguments
                .requireNonNull(structuredDataBuilder, "structuredDataBuilder must not be null").build();
        this.snapshotSerializer = structure.snapshotSerializer();
        this.structuralAssertions = structure.structuralAssertions();
        return this;
    }

    private boolean isCustomTextSnapshot() {
        return this.structuralAssertions.getClass().equals(TextSnapshot.text.structuralAssertions().getClass());
    }

    @Override
    public SnapshotTestResult matchesSnapshotText() {
        if (isCustomTextSnapshot()) {
            // prevent surprises when using a customized TextSnapshot instance. We then
            // use the actually configured TextStructuralAssertions instead of the default
            // on
            return this.matchesAccordingTo(structuralAssertions);
        }
        return this.matchesAccordingTo(TextSnapshot.text.structuralAssertions());
    }

    @Override
    public SnapshotTestResult matchesSnapshotStructure() throws Exception {
        return this.matchesAccordingTo(structuralAssertions);
    }

    @Override
    public SnapshotTestResult matchesAccordingTo(StructuralAssertions structuralAssertions) {
        Arguments.requireNonNull(structuralAssertions, "structuralAssertions must not be null");
        try {
            return snapshot.executeAssertionWith(snapshotSerializer, structuralAssertions, actual);
        } catch (final Exception e) {
            throw new IllegalStateException("Technical problem while performing the snapshot assertion", e);
        }
    }

    @Override
    public SnapshotTestResult justUpdateSnapshot() {
        try {
            return snapshot.justUpdateSnapshotWith(snapshotSerializer, actual);
        } catch (final Exception e) {
            throw new IllegalStateException("Technical problem while updating the snapshot", e);
        }
    }

    @Override
    public SnapshotTestResult disabled() {
        try {
            return snapshot.disabled(snapshotSerializer, structuralAssertions, actual);
        } catch (final Exception e) {
            throw new IllegalStateException("Technical problem while handling diabled assertion", e);
        }
    }
}
