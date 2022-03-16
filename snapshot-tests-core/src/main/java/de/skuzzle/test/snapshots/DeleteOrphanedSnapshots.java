package de.skuzzle.test.snapshots;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

/**
 * Tagging annotation that can be <b>temporarily</b> placed on a snapshot test to have
 * orphaned snapshot files automatically deleted.
 * <p>
 * <b>WARNING:</b> This will also delete orphans that do not necessarily pertain to the
 * currently executed test class. Use with caution especially when you are executing only
 * parts of your test suite (snapshots pertaining to excluded tests might falsely be
 * determined orphaned).
 * <p>
 * An orphaned snapshot file is one for which at least one of the following conditions
 * apply:
 * <ol>
 * <li>the test class (as stated in the snapshot's header) does not exist anymore</li>
 * <li>the snapshot file is not located in the test class's snapshot directory</li>
 * <li>the test method (as stated in the header) does not exist in that class anymore</li>
 * <li>the test method is no longer a snapshot test (does not have a {@link Snapshot}
 * parameter</li>
 * <li>the test method is private or static</li>
 * <li>the test method completed successfully but did not produce the snapshot file</li>
 * </ol>
 * <p>
 * Besides using this annotations, orphans can also be deleted by passing the system
 * property <code>deleteOrphanedSnapshots</code> (case insensitive) to the JVM.
 *
 * @deprecated This annotation is <b>NOT</b> deprecated. Deprecation serves only to mark
 *             this annotation in your IDE as it should only ever be used temporarily.
 * @since 1.1.0
 * @author Simon Taddiken
 */
@Retention(RUNTIME)
@Target({ TYPE })
@API(status = Status.EXPERIMENTAL, since = "1.1.0")
@Deprecated
public @interface DeleteOrphanedSnapshots {

}
