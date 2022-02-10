package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtendWith;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.impl.SnapshotExtension;

/**
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
 * implementations that allow to serialize snapshots as json or xml. To use them, you need
 * to declare their respective maven modules as dependency.
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
 * of {@link StructuredDataProvider}) you can make use of <em>structural assertions</em> to
 * compare snapshots. Depending on the implementation, those might provide better error
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
 * @author Simon Taddiken
 * @see Snapshot
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@ExtendWith(SnapshotExtension.class)
@API(status = Status.STABLE)
public @interface EnableSnapshotTests {

    /**
     * Define the snapshot directory relative to <code>src/test/resources</code>. If this
     * is not defined, snapshots will be stored in a directory structure according to the
     * package name of the test class.
     *
     * @return The relative directory to store the snapshots.
     */
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
     * @return Whether to update the stored snapshots.
     * @see ChooseAssertions#justUpdateSnapshot()
     */
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
     */
    boolean softAssertions() default false;
}
