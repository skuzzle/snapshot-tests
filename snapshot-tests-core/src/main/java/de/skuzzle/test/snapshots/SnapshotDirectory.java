package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Drop in replacement for {@link EnableSnapshotTests#snapshotDirectory()}. Allows to
 * customize the snapshot directory either globally for a test class or locally for a
 * single test method.
 *
 * @author Simon Taddiken
 * @since 1.7.0
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@API(status = Status.EXPERIMENTAL, since = "1.7.0")
public @interface SnapshotDirectory {
    /**
     * Define the snapshot directory relative to <code>src/test/resources</code>. If this
     * is not defined, snapshots will be stored in a directory structure according to the
     * package name of the test class.
     *
     * @return The relative directory to store the snapshots.
     */
    String value() default "";

    Class<? extends SnapshotDirectoryStrategy> determinedBy() default DefaultSnapshotDirectoryStrategy.class;
}
