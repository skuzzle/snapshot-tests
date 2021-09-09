package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import de.skuzzle.test.snapshots.SnapshotDsl.ChoseAssertions;

/**
 * Enables the snapshot-test capabilities.
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
     * stored snapshot with the current test results.
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
     * @since ever.
     */
    boolean updateSnapshots() default false;
}
