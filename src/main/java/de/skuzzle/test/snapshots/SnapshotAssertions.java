package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import de.skuzzle.test.snapshots.SnapshotDsl.ChoseAssertions;
import de.skuzzle.test.snapshots.impl.SnapshotExtension;

/**
 * Enables the snapshot-test capabilities. When you mark a class with this annotation, you
 * can use snapshot assertions like this:
 *
 * <pre>
 * &#64;SnapshotAssertions
 * class MyTestClass {
 *
 *     &#64;Test
 *     void testSomething(Snapshot snapshot) throws Exception {
 *         Object actual = ...
 *         snapshot.assertThat(actual).asXml().matchesSnapshot();
 *     }
 * }
 * </pre>
 *
 * @author Simon Taddiken
 * @since ever.
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@ExtendWith(SnapshotExtension.class)
public @interface SnapshotAssertions {

    /**
     * Define the snapshot directory relative to <code>src/test/resources</code>. If this
     * is not defined, snapshots will be stored in a directory structure according to the
     * package name of the test class.
     *
     * @return The relative directory to store the snapshots.
     * @since ever
     */
    String snapshotDirectory() default "";

    /**
     * Can be set to <code>true</code> <b>temporarily</b> in order to force to update the
     * persisted snapshots with the current test results.
     * <p>
     * <b>Warning:</b> While this is attribute is set to true, all snapshot assertions
     * will fail with an error. This is to prevent accidentally checking in disabled
     * assertions.
     * <p>
     * After snapshots have been updated, you should reset this flag to <code>false</code>
     * and run the tests again before checking your code into any SCM.
     *
     * @return Whether to update the stored snapshots.
     * @see ChoseAssertions#justUpdateSnapshot()
     * @since 0.0.2 (renamed from updateSnapshots)
     */
    boolean forceUpdateSnapshots() default false;

    /**
     * When enabled, a test method using snapshot assertions will continue to execute,
     * even if the snapshot execution failed. This allows to collect multiple failing
     * snapshots with a single test execution.
     * <p>
     * The failures from all snapshot comparisons within the single test methods will be
     * collected and reported after the test method completed.
     *
     * @return Whether to enable soft assertions. Defaults to <code>false</code>.
     * @since 0.0.2
     */
    boolean softAssertions() default false;
}
