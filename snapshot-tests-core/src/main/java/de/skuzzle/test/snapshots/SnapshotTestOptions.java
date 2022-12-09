package de.skuzzle.test.snapshots;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.data.text.TextSnapshot;

/**
 * Allows to configure some behavior details of the snapshot testing engine. This
 * annotation can either be placed on the test class itself or on single test methods.
 * Annotations on test methods take precedence over the one on the test class. If the
 * annotation is not specified at all, the the documented defaults apply.
 * <p>
 * If you want to globally modify the snapshot directory, see the
 * {@link SnapshotDirectory} annotation.
 *
 * @author Simon Taddiken
 * @since 1.7.0
 */
@API(status = Status.EXPERIMENTAL, since = "1.7.0")
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SnapshotTestOptions {
    /**
     * Defines the number of context lines that are printed around a comparison failure.
     * Note that this setting only applies to unified diffs created for structural
     * comparisons. If you use text comparison, then you can control the amount of context
     * lines using {@link TextSnapshot#withContextLines(int)}.
     * <p>
     * Defaults to 5.
     *
     * @return The number of context lines to print in unified diffs within our structural
     *         assertion failures.
     */
    int textDiffContextLines() default 5;

    /**
     * Whether to always persist the latest actual test result in a parallel file next to
     * the <code>.snapshot</code> file. The actual result will be persisted in a file with
     * identical name but with <code>.snapshot_actual</code> file extension.
     * <p>
     * Note: If you opt into enabling this option, you should add the
     * <code>.snapshot_actual</code> to your <code>.gitignore</code> file. Other than the
     * normal <code>.snapshot</code> files, the <code>..._actual</code> files are not
     * intended to be checked into the SCM.
     * <p>
     * If this option is disabled, then all existing <code>..._actual</code> files will be
     * proactively deleted by the framework during test execution.
     * <p>
     * Defaults to false.
     *
     * @return Whether to always persist the latest actual test result.
     */
    boolean alwaysPersistActualResult() default false;

    /**
     * When enabled, the framework will persist the raw actual result without snapshot
     * header as a sibling file to the real <code>.snapshot</code> file with the extension
     * <code>.snapshot_raw</code>.
     * <p>
     * Note: If you opt into enabling this option, you should add the
     * <code>.snapshot_actual</code> to your <code>.gitignore</code> file. Other than the
     * normal <code>.snapshot</code> files, the <code>..._raw</code> files are not
     * intended to be checked into the SCM.
     * <p>
     * If this option is disabled, then all existing <code>..._raw</code> files will be
     * proactively deleted by the framework during test execution.
     * <p>
     * Defaults to false.
     *
     * @return Whether to additionally persist the raw snapshot results.
     */
    boolean alwaysPersistRawResult() default false;

}
