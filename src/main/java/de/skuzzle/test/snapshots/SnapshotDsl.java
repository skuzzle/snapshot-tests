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

        /**
         * This method just updates the persisted snapshot with the current actual test
         * result. <b>It will always make the test fail with an assertion failure.</b>
         *
         * @return this (theoretically, but this method always throws)
         * @throws AssertionError Always thrown by this method to indicate that a call to
         *             this method must be removed to enable snapshot assertions.
         * @throws Exception If any kind of technical exception (except assertion failure)
         *             occurred.
         * @since ever
         */
        ChoseAssertions justUpdateSnapshot() throws Exception;

        /**
         * Asserts that the serialized actual test result matches the persisted snapshot
         * using a generic String diff algorithm. As this assertions only does a String
         * comparison it can be used regardless which {@link SnapshotSerializer} has been
         * used.
         *
         * @throws AssertionError If the serialized objects do not match or if snapshots
         *             have been updated.
         * @throws Exception If any kind of technical exception (except assertion failure)
         *             occurred.
         * @since ever
         */
        void matchesSnapshotText() throws Exception;

        /**
         * Asserts that the serialized actual test result structurally matches the
         * persisted snapshot. This method delegates to given {@link StructuralAssertions}
         * instance.
         *
         * @throws AssertionError If the serialized objects do not match according to
         *             {@link StructuralAssertions#assertEquals(String, String)} or if
         *             snapshots have been updated.
         * @throws Exception If any kind of technical exception (except assertion failure)
         *             occurred.
         * @since ever
         */
        void matchesAccordingTo(StructuralAssertions structuralAssertions) throws Exception;
    }

    public interface ChoseStructure extends ChoseAssertions {

        /**
         * {@inheritDoc}
         */
        @Override
        ChoseStructure justUpdateSnapshot() throws Exception;

        /**
         * Asserts that the serialized actual test result structurally matches the
         * persisted snapshot. This method delegates to the {@link StructuralAssertions}
         * instance in place
         *
         * @throws AssertionError If the serialized objects do not match according to
         *             {@link StructuralAssertions#assertEquals(String, String)} or if
         *             snapshots have been updated.
         * @throws Exception If any kind of technical exception (except assertion failure)
         *             occurred.
         * @since ever
         */
        void matchesSnapshotStructure() throws Exception;

    }
}
