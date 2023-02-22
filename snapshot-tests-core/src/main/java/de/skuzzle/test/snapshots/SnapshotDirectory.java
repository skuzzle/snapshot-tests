package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDirectory;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

/**
 * Drop in replacement for {@link EnableSnapshotTests#snapshotDirectory()}. Allows to
 * customize the snapshot directory globally for a test class.
 * <p>
 * If you don't customize the snapshot directory at all, then snapshots will by default be
 * stored in a directory structure according to the package name of the test class. If you
 * already specified a custom directory using the DSL (via
 * {@link Snapshot#in(java.nio.file.Path)}) then this annotation will have no effect for
 * that particular assertion.
 * <p>
 * You can either specify {@link #value()} which will interpret the string as directory
 * relative to this project's src/test/resources directory. Or you can specify the type of
 * a class that implements {@link SnapshotDirectoryStrategy} using
 * {@link #determinedBy()}. With the strategy you are no longer bounded to use a directory
 * relative to src/test/resources.
 *
 * @author Simon Taddiken
 * @see SnapshotDirectoryStrategy
 * @see ChooseDirectory
 * @since 1.7.0
 */
@Retention(RUNTIME)
@Target({ TYPE })
@API(status = Status.EXPERIMENTAL, since = "1.7.0")
public @interface SnapshotDirectory {
    /**
     * Define the snapshot directory relative to <code>src/test/resources</code>.
     * <p>
     * Note: If you specify a custom {@link SnapshotDirectoryStrategy} using
     * {@link #determinedBy()}, the strategy implementation might apply custom semantics
     * to this value. Check with the respective strategy's documentation.
     *
     * @return The relative directory to store the snapshots.
     */
    String value() default "";

    /**
     * Specify the type of a class that implements {@link SnapshotDirectoryStrategy}. The
     * strategy will be used to determine the actual snapshot directory.
     * <p>
     * By default, there is no strategy in place, meaning that the default semantics as
     * described in the documentation to the {@link #value()} property apply.
     *
     * @return The strategy to use.
     */
    Class<? extends SnapshotDirectoryStrategy> determinedBy() default DefaultSnapshotDirectoryStrategy.class;
}
