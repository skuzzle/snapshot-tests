package de.skuzzle.test.snapshots;

import javax.xml.bind.JAXBContext;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseStructure;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.data.xml.JaxbStructuredData;

// Just to try out the dsl
public class SnapshotDslTest {

    @Test
    void testDsl() throws Exception {
        snapshot()
                .assertThat(new Object())
                .asJson()
                .justUpdateSnapshot();

        snapshot()
                .named("explicitname")
                .assertThat(new Object())
                .as(JaxbStructuredData.with(JAXBContext.newInstance()).build())
                .matchesAccordingTo(new StructuralAssertions() {

                    @Override
                    public void assertEquals(String storedSnapshot, String serializedActual)
                            throws AssertionError, SnapshotException {
                        // TODO Auto-generated method stub

                    }
                });

    }

    private Snapshot snapshot() {
        return new TestSnapshotDslImpl();
    }

    private static class TestSnapshotDslImpl implements Snapshot, ChoseDataFormat, ChooseStructure, ChooseAssertions {

        @Override
        public ChoseDataFormat assertThat(Object actual) {
            return this;
        }

        @Override
        public ChooseStructure asXml() {
            return this;
        }

        @Override
        public ChooseStructure asJson() {
            return this;
        }

        @Override
        public ChooseAssertions asText() {
            return this;
        }

        @Override
        public ChooseStructure as(StructuredData structure) {
            return this;
        }

        @Override
        public ChooseAssertions as(SnapshotSerializer serializer) {
            return this;
        }

        @Override
        public SnapshotResult justUpdateSnapshot() {
            return null;
        }

        @Override
        public SnapshotResult matchesSnapshotText() {
            return null;
        }

        @Override
        public SnapshotResult matchesAccordingTo(StructuralAssertions structuralAssertions) {
            return null;
        }

        @Override
        public SnapshotResult matchesSnapshotStructure() throws Exception {
            return null;
        }

        @Override
        public ChooseActual named(String snapshotName) {
            return this;
        }

    }

}
