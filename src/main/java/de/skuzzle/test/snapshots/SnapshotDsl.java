package de.skuzzle.test.snapshots;

import javax.xml.bind.JAXBContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.skuzzle.test.snapshots.data.json.JacksonStructuredData;
import de.skuzzle.test.snapshots.data.xml.JaxbStructuredData;

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
    public interface Snapshot extends ChoseActual, ChoseName {

    }

    public interface ChoseActual {
        /**
         * Will create a serialized snapshot of the provided actual test result and store
         * it on disk.
         *
         * @param actual The actual test result.
         * @return Fluent API object for chosing the snapshot format.
         * @since ever
         */
        ChoseDataFormat assertThat(Object actual);
    }

    public interface ChoseName {

        ChoseActual named(String snapshotName);
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
         * Serializes the actual test result into an XML string. This will try to infer a
         * proper {@link JAXBContext} from the provided actual test result.
         * <p>
         * If you need to customize XML related snapshot tests, you should use
         * {@link #as(StructuredData)} together with {@link JaxbStructuredData}.
         *
         * @return Fluent API object for performing the snapshot assertion.
         * @since ever
         */
        ChoseStructure asXml();

        /**
         * Serializes the actual test result into a json string. This will use an
         * {@link ObjectMapper} with a default configuration suitable for most use cases.
         * <p>
         * If you need to customize json related snapshot tests, you should use
         * {@link #as(StructuredData)} together with {@link JacksonStructuredData}.
         *
         * @return Fluent API object for performing the snapshot assertion.
         * @since ever
         */
        ChoseStructure asJson();

        /**
         * "Serializes" the actual test result using {@link Object#toString()}.
         *
         * @return Fluent API object for performing the snapshot assertion.
         * @since 0.0.4
         */
        ChoseAssertions asText();

        ChoseStructure as(StructuredData structure);

        ChoseAssertions as(SnapshotSerializer serializer);
    }

    public interface ChoseAssertions {

        /**
         * This method just updates the persisted snapshot with the current actual test
         * result. <b>It will always make the test fail with an assertion failure.</b>
         *
         * @return Details about the snapshot.
         * @throws AssertionError Always thrown by this method to indicate that a call to
         *             this method must be removed to enable snapshot assertions.
         * @since ever
         */
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

    public interface ChoseStructure extends ChoseAssertions {

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
