package de.skuzzle.test.snapshots;

import javax.xml.bind.JAXBContext;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDsl.ChoseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseStructure;
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

    private static class TestSnapshotDslImpl implements Snapshot, ChoseDataFormat, ChoseStructure, ChoseAssertions {

        @Override
        public ChoseDataFormat assertThat(Object actual) {
            return this;
        }

        @Override
        public ChoseStructure asXml() {
            return this;
        }

        @Override
        public ChoseStructure asJson() {
            return this;
        }

        @Override
        public ChoseAssertions asText() {
            return this;
        }

        @Override
        public ChoseStructure as(StructuredData structure) {
            return this;
        }

        @Override
        public ChoseAssertions as(SnapshotSerializer serializer) {
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
        public ChoseActual named(String snapshotName) {
            return this;
        }

    }

}
