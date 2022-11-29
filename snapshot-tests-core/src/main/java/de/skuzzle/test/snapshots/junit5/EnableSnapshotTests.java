package de.skuzzle.test.snapshots.junit5;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtendWith;

import de.skuzzle.test.snapshots.DeleteOrphanedSnapshots;
import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.StructuredDataProvider;

/**
 * <h1>Enabling snapshot tests</h1>
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
 * @see DeleteOrphanedSnapshots
 * @see ForceUpdateSnapshots
 */
@Retention(RUNTIME)
@Target({ TYPE })
@ExtendWith(JUnit5SnapshotExtension.class)
@API(status = Status.STABLE, since = "1.7.0")
public @interface EnableSnapshotTests {

}
