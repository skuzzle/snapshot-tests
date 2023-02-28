package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Can be <em>temporarily</em> placed on a test class or test method to forcefully update
 * snapshots.
 * <p>
 * When placed on a test class, snapshots in all tests within that class will be
 * forcefully updated with latest actual result. When placed on a test method, only
 * snapshots created within that test will be forcefully updated with latest actual test
 * result.
 * <p>
 * Snapshot files for snapshot assertions that are {@link ChooseAssertions#disabled()
 * disabled} will not be updated.
 * <p>
 * This annotation replaces the {@link EnableSnapshotTests#forceUpdateSnapshots()} flag
 * because it is easier to use.
 * <p>
 * Besides using this annotations, snapshots can also be updated globally by passing the
 * system property <code>forceUpdateSnapshots</code> (case insensitive) to the JVM.
 *
 * @deprecated This annotation is <b>NOT</b> deprecated. Deprecation serves only to mark
 *             this annotation in your IDE as it should only be used temporarily.
 * @see ChooseAssertions#justUpdateSnapshot()
 * @author Simon Taddiken
 * @since 1.1.0
 * @apiNote <b>Warning:</b> While this is annotation is present, all tests containing
 *          snapshot assertions will fail with an error. This is to prevent accidentally
 *          checking in disabled assertions.
 */
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@Deprecated
@API(status = Status.STABLE, since = "1.1.0")
public @interface ForceUpdateSnapshots {

}
