package de.skuzzle.test.snapshots;

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
    public interface Snapshot extends ChooseActual, ChooseName {

    }

    public interface ChooseActual {
        /**
         * Will create a serialized snapshot of the provided actual test result and store
         * it on disk.
         *
         * @param actual The actual test result.
         * @return Fluent API object for choosing the snapshot format.
         * @since ever
         */
        ChooseDataFormat assertThat(Object actual);
    }

    public interface ChooseName {
        /**
         * Choose a name for the snapshot file. This overrides the default naming scheme
         * of using <code>method name + consecutive number</code>. Note that, when you
         * specify the same name twice within test cases for the same snapshot directory,
         * snapshots will be silently overridden and tests may subsequently fail.
         *
         * @param snapshotName The name of the snapshot to create.
         * @return Fluent API object for choosing the snapshot format.
         */
        ChooseActual named(String snapshotName);
    }

    /**
     * Allows to chose the structure into which the actual test result will be serialized.
     * A custom serializer can be passed using {@link #as(SnapshotSerializer)}.
     *
     * @author Simon Taddiken
     * @since ever
     */
    public interface ChooseDataFormat {

        /**
         * "Serializes" the actual test result using {@link Object#toString()}.
         *
         * @return Fluent API object for performing the snapshot assertion.
         * @since 0.0.4
         */
        ChooseAssertions asText();

        ChooseStructure as(StructuredData structure);

        ChooseStructure as(StructuredDataBuilder structuredDataBuilder);

        ChooseAssertions as(SnapshotSerializer serializer);
    }

    public interface ChooseAssertions {

        /**
         * This method just updates the persisted snapshot with the current actual test
         * result. <b>It will always make the test fail with an assertion failure.</b> Use
         * it only temporarily as replacement for {@link #matchesSnapshotText()},
         * {@link #matchesAccordingTo(StructuralAssertions)} or
         * {@link ChooseStructure#matchesSnapshotStructure()}
         *
         * @deprecated This method is NOT deprecated. Deprecation serves only to mark this
         *             method in your IDE as it should only be used temporarily.
         * @return Details about the snapshot.
         * @throws AssertionError Always thrown by this method to indicate that a call to
         *             this method must be removed to enable snapshot assertions.
         * @since ever
         */
        @Deprecated
        SnapshotResult justUpdateSnapshot();

        /**
         * Asserts that the serialized actual test result matches the persisted snapshot
         * using a generic String diff algorithm. As this assertions only does a String
         * comparison it can be used regardless which {@link SnapshotSerializer} has been
         * used.
         *
         * @return Details about the snapshot.
         * @throws AssertionError If the serialized objects do not match.
         * @since ever
         */
        SnapshotResult matchesSnapshotText();

        /**
         * Asserts that the serialized actual test result structurally matches the
         * persisted snapshot. This method delegates to given {@link StructuralAssertions}
         * instance.
         *
         * @return Details about the snapshot.
         * @throws AssertionError If the serialized objects do not match according to
         *             {@link StructuralAssertions#assertEquals(String, String)}.
         * @since ever
         */
        SnapshotResult matchesAccordingTo(StructuralAssertions structuralAssertions);
    }

    public interface ChooseStructure extends ChooseAssertions {

        /**
         * Asserts that the serialized actual test result structurally matches the
         * persisted snapshot. This method delegates to the {@link StructuralAssertions}
         * instance in place
         *
         * @return Details about the snapshot.
         * @throws AssertionError If the serialized objects do not match according to
         *             {@link StructuralAssertions#assertEquals(String, String)} or if
         *             snapshots have been updated.
         * @throws Exception If any kind of technical exception (except assertion failure)
         *             occurred.
         * @since ever
         */
        SnapshotResult matchesSnapshotStructure() throws Exception;

    }
}
