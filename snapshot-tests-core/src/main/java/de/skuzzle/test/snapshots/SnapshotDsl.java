package de.skuzzle.test.snapshots;

import java.nio.file.Path;

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
     * <p>
     * Note that the Snapshot instance that is being injected into your test is stateful
     * and not thread safe. You should also refrain from using incomplete DSL usages. You
     * must always end the DSL call chain with any of the terminal operations
     * {@link ChooseAssertions#disabled()},
     * {@link ChooseAssertions#disabledBecause(String)},
     * {@link ChooseAssertions#matchesAccordingTo(StructuralAssertions)},
     * {@link ChooseAssertions#matchesSnapshotText()} or
     * {@link ChooseStructure#matchesSnapshotStructure()}.
     * <p>
     * Each method in the DSL advances the Snapshot instance's internal state until a
     * terminal operation has been called. So it is illegal to reuse DSL stages that have
     * already been completed like in this example:
     *
     * <pre>
     * snapshot.assertThat(actual);
     * snapshot.assertThat(actual);
     * </pre>
     *
     * This snippet does not only use two incomplete usages, but also tries to re-use the
     * 'choose-actual' stage without having called a terminal operation.
     *
     * @author Simon Taddiken
     * @see EnableSnapshotTests
     * @deprecated Since 1.8.0 - Use the new top-level type
     *             {@link de.skuzzle.test.snapshots.Snapshot Snapshot} instead.
     */
    @API(status = Status.DEPRECATED, since = "1.8.0")
    @Deprecated(since = "1.8.0", forRemoval = true)
    public interface Snapshot extends ChooseActual, ChooseName, ChooseDirectory {

    }

    @API(status = Status.STABLE)
    public interface ChooseActual {
        /**
         * Will create a serialized snapshot of the provided actual test result and store
         * it on disk. Note that the actual object is expected to be non-null. If it is
         * null, an AssertionError will be raised when calling any of the final matches...
         * methods on the snapshot DSL instance.
         *
         * @param actual The actual test result.
         * @return Fluent API object for choosing the snapshot format. Do NOT assume it is
         *         the same object as 'this'!
         */
        ChooseDataFormat assertThat(Object actual);
    }

    @API(status = Status.EXPERIMENTAL, since = "1.2.0")
    public interface ChooseDirectory {

        /**
         * Allows to choose the directory into which the snapshot will be persisted. The
         * path configured here takes precedence over what is configured via
         * {@link SnapshotDirectory}.
         * <p>
         * The path configured here will not be resolved against the
         * <code>src/test/resources</code> directory. If you provide a relative path, it
         * will be considered to be relative to the project's root directory.
         * <p>
         * <b>Warning:</b> Changing the directory has severe impact on orphan detection
         * and might lead to false positives. Use with caution as long as this API is
         * marked 'EXPERIMENTAL'.
         *
         * @param directory The directory into which to write the snapshot.
         * @return Fluent API object for choosing the snapshot format. Do NOT assume it is
         *         the same object as 'this'!
         * @since 1.2.0
         */
        @API(status = Status.EXPERIMENTAL, since = "1.2.0")
        ChooseName in(Path directory);
    }

    @API(status = Status.STABLE)
    public interface ChooseName extends ChooseActual {
        /**
         * Choose a name for the snapshot file. This overrides the default naming scheme
         * of using <code>method name + consecutive number</code>. Note that, when you
         * specify the same name twice within test cases for the same snapshot directory,
         * snapshots will be silently overridden and tests may subsequently fail.
         * <p>
         * Note that you can not change the extension of snapshot files which will always
         * be <code>.snapshot</code>.
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
         * <p>
         * The {@link SnapshotNaming} interface has some static methods with useful
         * implementations. You can also easily implement the interface yourself.
         * <p>
         * For example, in a parameterized test, snapshot can be named like this:
         *
         * <pre>
         * &#64;ParameterizedTest
         * &#64;ValueSource(strings = { "a", "b" })
         * void someTest(String parameter, Snapshot snapshot) {
         *     snapshot.namedAccordingTo(SnapshotNaming.withParameters(parameter))
         *             .assertThat(parameter)
         *             .asText()
         *             .matchesSnapshotText();
         * }
         * </pre>
         * <p>
         * Note that you can not change the extension of snapshot files which will always
         * be <code>.snapshot</code>.
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
         * <p>
         * <em>Note:</em> This method will always ignore whitespace changes. This includes
         * any changes in line breaks.
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
         * <p>
         * Note: This method immediately unwraps the given {@link StructuredDataProvider}
         * by calling its {@link StructuredDataProvider#build() build} method.
         * <p>
         * Implementations are generally provided by each of the available extension
         * modules and follow the naming scheme <code>[Format]Snapshot</code>. The
         * following implementations are provided by the respective maven artifacts:
         * <table>
         * <caption>StructuredDataProvider implementations</caption> <thead>
         * <tr>
         * <th>StructuredDataProvider</th>
         * <th>Maven Artifact</th>
         * </tr>
         * </thead> <tbody>
         * <tr>
         * <td>{@link TextSnapshot}</td>
         * <td>included in <code>snapshot-tests-core</code></td>
         * </tr>
         * <tr>
         * <td><code>de.skuzzle.tests.snapshots.data.JsonSnapshot</code></td>
         * <td><code>snapshot-tests-jackson</code></td>
         * </tr>
         * <tr>
         * <td><code>de.skuzzle.tests.snapshots.data.XmlSnapshot</code></td>
         * <td><code>snapshot-tests-jaxb</code> or
         * <code>snapshot-tests-jaxb-jakarta</code></td>
         * </tr>
         * <tr>
         * <td><code>de.skuzzle.tests.snapshots.data.HtmlSnapshot</code></td>
         * <td><code>snapshot-tests-html</code></td>
         * </tr>
         * </tbody>
         * </table>
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
         * @return Details about the snapshot.
         * @throws AssertionError Always thrown by this method to indicate that a call to
         *             this method must be removed to enable snapshot assertions.
         * @deprecated This method is <b>NOT</b> deprecated. Deprecation serves only to
         *             mark this method in your IDE as it should only be used temporarily.
         * @see ForceUpdateSnapshots
         */
        @Deprecated
        SnapshotTestResult justUpdateSnapshot();

        /**
         * This is a terminal DSL operation which just advances the internal state of the
         * injected Snapshot instance but which does not apply any comparisons. This
         * method is useful if you have multiple assertions within a single test case and
         * you want to temporarily one. If you rely on automatic snapshot naming,
         * subsequent assertions will still work as expected when using this method as
         * opposed to just commenting out the assertion.
         * <p>
         * When a DSL statement is terminated with this operation and no persisted
         * snapshot file exists, none will be created. However the actual test result will
         * still be serialized. Thus it is still possible to inspect the serialized result
         * within the return {@link SnapshotTestResult} object. However, the referenced
         * snapshot file will not exist in such cases.
         * <p>
         * If a snapshot file already exists, it will not be reported as orphan file as
         * long as the assertion is disabled.
         * <p>
         * Note: We can not make the same guarantees about orphan detection when using
         * JUnit5's native mechanism for disabling tests. In such cases, we try to make a
         * best effort guess based on statically available information.
         *
         * @return Details about the snapshot. Note that the referenced snapshot file
         *         might not necessarily exist.
         * @since 1.5.0
         */
        @API(status = Status.EXPERIMENTAL, since = "1.5.0")
        SnapshotTestResult disabled();

        /**
         * This is a terminal DSL operation which just advances the internal state of the
         * injected Snapshot instance but which does not apply any comparisons. This
         * method is useful if you have multiple assertions within a single test case and
         * you want to temporarily one. If you rely on automatic snapshot naming,
         * subsequent assertions will still work as expected when using this method as
         * opposed to just commenting out the assertion.
         * <p>
         * When a DSL statement is terminated with this operation and no persisted
         * snapshot file exists, none will be created. However the actual test result will
         * still be serialized. Thus it is still possible to inspect the serialized result
         * within the return {@link SnapshotTestResult} object. However, the referenced
         * snapshot file will not exist in such cases.
         * <p>
         * If a snapshot file already exists, it will not be reported as orphan file as
         * long as the assertion is disabled.
         * <p>
         * Note: We can not make the same guarantees about orphan detection when using
         * JUnit5's native mechanism for disabling tests. In such cases, we try to make a
         * best effort guess based on statically available information.
         * 
         * @param message Just an informative message describing why this assertion is
         *            disabled.
         * @return Details about the snapshot. Note that the referenced snapshot file
         *         might not necessarily exist.
         * @since 1.8.0
         */
        @API(status = Status.EXPERIMENTAL, since = "1.8.0")
        SnapshotTestResult disabledBecause(String message);

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
