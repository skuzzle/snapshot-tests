package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtendWith;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.junit5.LegacyJUnit5SnapshotExtension;

/**
 * <h2>Enabling snapshot tests</h2>
 * <p>
 * Enables the snapshot-test capabilities. When you mark a class with this annotation, you
 * can use snapshot assertions by declaring a parameter of type {@link Snapshot} in your
 * test case like this:
 *
 * <pre>
 * &#64;EnableSnapshotTests
 * class MyTestClass {
 *
 *     &#64;Test
 *     void testSomething(Snapshot snapshot) throws Exception {
 *         Object actual = ...
 *         snapshot.assertThat(actual).asText().matchesSnapshotText();
 *     }
 * }
 * </pre>
 * <p>
 * <code>asText()</code> will 'serialize' actual test results using
 * {@link Object#toString()}. There are additional {@link StructuredDataProvider}
 * implementations that allow to serialize snapshots as json, xml or html. To use them,
 * you need to declare their respective maven modules as dependency.
 *
 * <pre>
 *     &#64;Test
 *     void testSomething(Snapshot snapshot) throws Exception {
 *         Object actual = ...
 *         snapshot.assertThat(actual).as(TextSnapshot.text).matchesSnapshotText();
 *         snapshot.assertThat(actual).as(JsonSnapshot.json).matchesSnapshotText();
 *         snapshot.assertThat(actual).as(XmlSnapshot.xml).matchesSnapshotText();
 *     }
 * </pre>
 * <p>
 * When providing a structured data format like json/xml (or in general: an implementation
 * of {@link StructuredDataProvider}) you can make use of <em>structural assertions</em>
 * to compare snapshots. Depending on the implementation, those might provide better error
 * messages than plain text comparison.
 *
 * <pre>
 *     &#64;Test
 *     void testSomething(Snapshot snapshot) throws Exception {
 *         Object actual = ...
 *         snapshot.assertThat(actual).as(JsonSnapshot.json).matchesSnapshotStructure();
 *         snapshot.assertThat(actual).as(XmlSnapshot.xml).matchesSnapshotStructure();
 *     }
 * </pre>
 *
 * <h2>Parameterized tests</h2>
 * <p>
 * Snapshot tests can be combined with JUnit5's parameterized tests, but only when you
 * provide an explicit name for each snapshot assertion. With the default automatic
 * snapshot naming scheme, snapshots would otherwise be overridden for each parameterized
 * execution.
 *
 * <pre>
 *     &#64;ParameterizedTest
 *     &#64;Values(strings = { "string1", "string2" })
 *     void testSomething(String parameter, Snapshot snapshot) throws Exception {
 *         Object actual = ...
 *
 *         // BAD: would choose the same snapshot file name 'testSomething_0.snapshot' disregarding the parameter
 *         // (Note: this could be desired if you expect the same output for all parameters)
 *         snapshot.assertThat(actual).as...;
 *
 *         // GOOD: Append the parameter's value to the snapshot name to have separate snapshots per execution
 *         // This will create snapshots named 'testSomething_0_string1.snapshot' and 'testSomething_0_string2.snapshot'
 *         snapshot.namedAccordingTo(SnapshotNaming.withParameters(parameter))
 *                 .assertThat(actual).as...;
 * </pre>
 *
 * <h2>Updating snapshots</h2>
 * <p>
 * Snapshots can become outdated when your code under test changes on purpose. In that
 * case you can advice the framework to override existing snapshots with your code under
 * test's actual result by placing the annotation {@link ForceUpdateSnapshots} on either
 * the whole snapshot test class or on a single test method.
 *
 * <h2>Orphaned snapshots</h2>
 * <p>
 * Snapshot files can become orphans if, for example you rename a test class/method or you
 * change the snapshot assertions within a test. This framework comes with a sophisticated
 * approach for detecting those orphaned files. By default, we will log a warning with the
 * found orphan. You can temporarily place the {@link DeleteOrphanedSnapshots} annotation
 * on a snapshot test class to have those files deleted automatically.
 *
 * @author Simon Taddiken
 * @see Snapshot
 * @see SnapshotNaming
 * @see SnapshotDirectory
 * @see SnapshotTestOptions
 * @see DeleteOrphanedSnapshots
 * @see ForceUpdateSnapshots
 * @deprecated Since 1.7.0 - This class is deprecated in favor of the
 *             {@link de.skuzzle.test.snapshots.junit5.EnableSnapshotTests} class within
 *             the <code>junit5</code> package. Note that the variant in the
 *             <code>junit5</code> package already comes with all the deprecated methods
 *             removed and replaced with their respective alternatives. For details,
 *             inspect the deprecation notes on the attributes within this class.
 */
@Retention(RUNTIME)
@Target({ TYPE })
@ExtendWith(LegacyJUnit5SnapshotExtension.class)
@API(status = Status.DEPRECATED, since = "1.7.0")
@Deprecated(since = "1.7.0", forRemoval = true)
public @interface EnableSnapshotTests {

    /**
     * Define the snapshot directory relative to <code>src/test/resources</code>. If this
     * is not defined, snapshots will be stored in a directory structure according to the
     * package name of the test class.
     *
     * @return The relative directory to store the snapshots.
     * @deprecated Since 1.7.0 - Use {@link SnapshotDirectory} annotation instead.
     */
    @Deprecated(since = "1.7.0", forRemoval = true)
    @API(status = Status.DEPRECATED, since = "1.7.0")
    String snapshotDirectory() default "";

    /**
     * Can be set to <code>true</code> <b>temporarily</b> in order to force to update the
     * persisted snapshots with the current test results.
     * <p>
     * <b>Warning:</b> While this is attribute is set to true, all tests containing
     * snapshot assertions will fail with an error. This is to prevent accidentally
     * checking in disabled assertions.
     * <p>
     * After snapshots have been updated, you should reset this flag to <code>false</code>
     * and run the tests again before checking your code into any SCM.
     *
     * @deprecated Since 1.1.0 - Use the {@link ForceUpdateSnapshots} annotation instead.
     * @return Whether to update the stored snapshots.
     * @see ChooseAssertions#justUpdateSnapshot()
     * @see ForceUpdateSnapshots
     */
    @API(status = Status.DEPRECATED, since = "1.1.0")
    @Deprecated(since = "1.1.0", forRemoval = true)
    boolean forceUpdateSnapshots() default false;

    /**
     * When enabled, a test method using snapshot assertions will continue to execute,
     * even if a snapshot assertion failed. This allows to collect multiple failing
     * snapshots with a single test execution.
     * <p>
     * The failures from all snapshot comparisons within the single test methods will be
     * collected and reported after the test method completed.
     *
     * @return Whether to enable soft assertions. Defaults to <code>false</code>.
     * @deprecated Since 1.7.0 - Soft assertions will no longer be supported from version
     *             2.0 on. You could use AssertJ's SoftAssertions as a replacement.
     */
    @Deprecated(since = "1.7.0", forRemoval = true)
    @API(status = Status.DEPRECATED, since = "1.7.0")
    boolean softAssertions() default false;
}
