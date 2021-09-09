package de.skuzzle.test.snapshots;

import de.skuzzle.test.snapshots.data.SnapshotSerializer;
import de.skuzzle.test.snapshots.data.StructuralAssertions;
import de.skuzzle.test.snapshots.data.StructuredData;

/**
 * DSL for defining snapshot tests.
 *
 * @author Simon Taddiken
 */
public interface SnapshotDsl {

    /**
     * Allows to do snapshot assertions. An instance of this class can be injected into
     * your test case by just specifying a parameter of this type:
     *
     * <pre>
     * &#64;Test
     * void test(Snapshot snapshot) throws Exception {
     *     ...
     *     snapshot.assertThat(...)...
     * }
     * </pre>
     *
     * Note that the respective test class must be annotated with
     * {@link SnapshotAssertions}.
     *
     * @author Simon Taddiken
     * @since ever
     */
    public interface Snapshot {

        /**
         * Will create a serialized snapshot of the provided actual test result and store
         * it on disk.
         *
         * @param actual The actual test result.
         * @return TBD
         * @since ever
         */
        ChoseDataFormat assertThat(Object actual);
    }

    /**
     * Allows to chose the structure into which the actual test result will be serialized.
     * A custom serializer can be passed using {@link #as(SnapshotSerializer)}.
     *
     * @author Simon Taddiken
     * @since ever
     */
    public interface ChoseDataFormat {

        /**
         * Serializes the actual test result into an XML string.
         *
         * @return TBD
         * @since ever
         */
        ChoseStructure asXml();

        ChoseStructure asJson();

        ChoseAssertions as(SnapshotSerializer serializer);

        ChoseStructure as(StructuredData structure);

    }

    public interface ChoseAssertions {

        void matchesSnapshotText() throws Exception;

        void matchesAccordingTo(StructuralAssertions structuralAssertions) throws Exception;
    }

    public interface ChoseStructure extends ChoseAssertions {

        void matchesSnapshotStructure() throws Exception;

    }
}
