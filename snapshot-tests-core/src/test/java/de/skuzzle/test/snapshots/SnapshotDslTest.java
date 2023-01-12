package de.skuzzle.test.snapshots;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseName;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseStructure;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

// Just to try out the dsl
public class SnapshotDslTest {

    @Test
    void testDsl() throws Exception {
        snapshot()
                .namedAccordingTo(SnapshotNaming.defaultNaming())
                .assertThat(new Object())
                .asText()
                .matchesSnapshotText();

    }

    @Test
    void testDsl2() throws Exception {
        snapshot()
                .in(Path.of("tbd", "x"))
                .namedAccordingTo(SnapshotNaming.defaultNaming())
                .assertThat(new Object())
                .asText()
                .matchesSnapshotText();

    }

    private Snapshot snapshot() {
        return new TestSnapshotDslImpl();
    }

    private static class TestSnapshotDslImpl implements Snapshot, ChooseDataFormat, ChooseStructure, ChooseAssertions {

        @Override
        public ChooseDataFormat assertThat(Object actual) {
            return this;
        }

        @Override
        public ChooseAssertions asText() {
            return this;
        }

        @Override
        public ChooseAssertions as(SnapshotSerializer serializer) {
            return this;
        }

        @Override
        public SnapshotTestResult justUpdateSnapshot() {
            return null;
        }

        @Override
        public SnapshotTestResult matchesSnapshotText() {
            return null;
        }

        @Override
        public SnapshotTestResult disabled() {
            return null;
        }

        @Override
        public SnapshotTestResult matchesAccordingTo(StructuralAssertions structuralAssertions) {
            return null;
        }

        @Override
        public SnapshotTestResult matchesSnapshotStructure() throws Exception {
            return null;
        }

        @Override
        public ChooseActual named(String snapshotName) {
            return this;
        }

        @Override
        public ChooseActual namedAccordingTo(SnapshotNaming namingStrategy) {
            return this;
        }

        @Override
        public ChooseStructure as(StructuredDataProvider structuredDataBuilder) {
            return this;
        }

        @Override
        public ChooseName in(Path directory) {
            return this;
        }

        @Override
        public SnapshotTestResult disabledBecause(String message) {
            return null;
        }

    }

}
