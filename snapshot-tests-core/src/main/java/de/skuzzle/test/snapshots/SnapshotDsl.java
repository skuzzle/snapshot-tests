package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.data.text.TextSnapshot;

/**
 * DSL for defining snapshot tests. The main entry point is {@link Snapshot}.
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
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
     * {@link EnableSnapshotTests}, otherwise the test framework will not be able to
     * resolve the <code>Snapshot</code> parameter of the test method.
     *
     * @author Simon Taddiken
     */
    @API(status = Status.STABLE)
    public interface Snapshot extends ChooseActual, ChooseName {

    }

    @API(status = Status.STABLE)
    public interface ChooseActual {
        /**
         * Will create a serialized snapshot of the provided actual test result and store
         * it on disk.
         *
         * @param actual The actual test result.
         * @return Fluent API object for choosing the snapshot format. Do NOT assume it is
         *         the same object as 'this'!
         */
        ChooseDataFormat assertThat(Object actual);
    }

    @API(status = Status.STABLE)
    public interface ChooseName {
        /**
         * Choose a name for the snapshot file. This overrides the default naming scheme
         * of using <code>method name + consecutive number</code>. Note that, when you
         * specify the same name twice within test cases for the same snapshot directory,
         * snapshots will be silently overridden and tests may subsequently fail.
         *
         * @param snapshotName The name of the snapshot to create.
         * @return Fluent API object for choosing the snapshot format. Do NOT assume it is
         *         the same object as 'this'!
         */
        default ChooseActual named(String snapshotName) {
            return namedAccordingTo(SnapshotNaming.constant(snapshotName));
        }

        /**
         * Choose a name for the snapshot file according to the given strategy.
         *
         * @param namingStrategy The naming strategy to use.
         * @return Fluent API object for choosing the snapshot format. Do NOT assume it is
         *         the same object as 'this'!
         * @since 1.1.0
         */
        @API(status = Status.EXPERIMENTAL, since = "1.1.0")
        ChooseActual namedAccordingTo(SnapshotNaming namingStrategy);
    }

    /**
     * Allows to choose the structure into which the actual test result will be
     * serialized. A custom serializer can be passed using
     * {@link #as(SnapshotSerializer)}.
     *
     * @author Simon Taddiken
     */
    @API(status = Status.STABLE)
    public interface ChooseDataFormat {

        /**
         * "Serializes" the actual test result using {@link Object#toString()} and
         * compares the results using a generic string diff algorithm.
         * <p>
         * Calling this method is equivalent to calling
         * <code>.as(TextSnapshot.text)</code>.
         *
         * @return Fluent API object for performing the snapshot assertion. Do NOT assume
         *         it is the same object as 'this'!
         * @since 0.0.4
         * @see #as(StructuredDataProvider)
         */
        ChooseAssertions asText();

        /**
         * Specify the serialization format <em>and</em> the way in which serialized
         * objects are compared. A {@link StructuredData} instance combines both a
         * {@link SnapshotSerializer} and a {@link StructuralAssertions} instance.
         *
         * @param structuredDataProvider The {@link StructuredDataProvider} instance.
         * @return Fluent API object for performing the snapshot assertion. Do NOT assume
         *         it is the same object as 'this'!
         * @see StructuredData
         */
        ChooseStructure as(StructuredDataProvider structuredDataProvider);

        /**
         * Specify the serialization format.
         *
         * @param serializer The serializer to use.
         * @return Fluent API object for performing the snapshot assertion. Do NOT assume
         *         it is the same object as 'this'!
         */
        ChooseAssertions as(SnapshotSerializer serializer);
    }

    /**
     * DSL stage to choose how to perform the snapshot assertion.
     *
     * @author Simon Taddiken
     */
    @API(status = Status.STABLE)
    public interface ChooseAssertions {

        /**
         * This method just updates the persisted snapshot with the current actual test
         * result. <b>It will always make the test fail with an assertion failure.</b> Use
         * it only temporarily as replacement for {@link #matchesSnapshotText()},
         * {@link #matchesAccordingTo(StructuralAssertions)} or
         * {@link ChooseStructure#matchesSnapshotStructure()}
         *
         * @deprecated This method is <b>NOT</b> deprecated. Deprecation serves only to
         *             mark this method in your IDE as it should only be used temporarily.
         * @return Details about the snapshot.
         * @throws AssertionError Always thrown by this method to indicate that a call to
         *             this method must be removed to enable snapshot assertions. @ see
         * @see ForceUpdateSnapshots
         */
        @Deprecated
        SnapshotTestResult justUpdateSnapshot();

        /**
         * Asserts that the serialized actual test result matches the persisted snapshot
         * using a generic String diff algorithm. As this assertions only does a String
         * comparison it can be used regardless which {@link SnapshotSerializer} has been
         * used.
         *
         * @return Details about the snapshot.
         * @throws AssertionError If the serialized objects do not match.
         * @see TextSnapshot
         */
        SnapshotTestResult matchesSnapshotText();

        /**
         * Asserts that the serialized actual test result structurally matches the
         * persisted snapshot. This method delegates to given {@link StructuralAssertions}
         * instance.
         *
         * @param structuralAssertions The {@link StructuralAssertions} instance to use.
         * @return Details about the snapshot.
         * @throws AssertionError If the serialized objects do not match according to
         *             {@link StructuralAssertions#assertEquals(String, String)}.
         */
        SnapshotTestResult matchesAccordingTo(StructuralAssertions structuralAssertions);
    }

    @API(status = Status.STABLE)
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
         */
        SnapshotTestResult matchesSnapshotStructure() throws Exception;

    }
}
