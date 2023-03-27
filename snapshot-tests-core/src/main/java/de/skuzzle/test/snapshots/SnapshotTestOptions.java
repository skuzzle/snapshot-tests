package de.skuzzle.test.snapshots;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.skuzzle.test.snapshots.data.text.TextSnapshot;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Allows to configure some behavior details of the snapshot testing engine. This
 * annotation can either be placed on the test class itself or on single test methods.
 * Annotations on test methods take precedence over the one on the test class. If the
 * annotation is not specified at all, the documented defaults apply.
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
     * Default number of context lines that are displayed in diffs.
     */
    int DEFAULT_CONTEXT_LINES = 5;

    /**
     * Default setting for {@link #normalizeLineEndings()}.
     */
    NormalizeLineEndings DEFAULT_NORMALIZE_LINE_ENDINGS = NormalizeLineEndings.NEVER;

    /**
     * Defines the number of context lines that are printed around a comparison failure.
     * Note that this setting only applies to unified diffs created for structural
     * comparisons. If you use text comparison, then you must control the amount of
     * context lines using {@link TextSnapshot#withContextLines(int)}.
     * <p>
     * Defaults to 5.
     *
     * @return The number of context lines to print in unified diffs within our structural
     *         assertion failures.
     */
    int textDiffContextLines() default DEFAULT_CONTEXT_LINES;

    /**
     * Controls whether an offset is added to the line numbers that are displayed when
     * rendering diffs. You can choose to render line numbers according to the original
     * raw test result or according to the physical lines in the persisted snapshot files.
     * The latter includes some header information at the beginning of the file which add
     * an offset to the actual data lines.
     * <p>
     * Defaults to {@link DiffLineNumbers#ACCODRDING_TO_PERSISTED_SNAPSHOT_FILE}.
     *
     * @return How to calculate line numbers in rendered diffs.
     * @since 1.7.1
     */
    @API(status = Status.EXPERIMENTAL, since = "1.7.1")
    DiffLineNumbers renderLineNumbers() default DiffLineNumbers.ACCODRDING_TO_PERSISTED_SNAPSHOT_FILE;

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

    /**
     * Advises the framework to convert the line endings after snapshot serialization to
     * the given value.
     * <p>
     * By default, line endings are not normalized but when using GIT it is advisable to
     * set this to {@link NormalizeLineEndings#GIT}
     * </p>
     *
     * @return How to normalize line endings.
     * @since 1.10.0
     */
    @API(since = "1.10.0", status = Status.EXPERIMENTAL)
    NormalizeLineEndings normalizeLineEndings() default NormalizeLineEndings.NEVER;

    /**
     * Defines whether an offset is added to the line numbers when rendering diffs in
     * assertion failure messages.
     *
     * @author Simon
     * @see SnapshotTestOptions#renderLineNumbers()
     * @since 1.7.1
     */
    @API(status = Status.EXPERIMENTAL, since = "1.7.1")
    enum DiffLineNumbers {
        /**
         * No offset will be added to line numbers. Line number 1 in rendered diffs maps
         * directly to line number 1 in the raw serialized test output.
         * <p>
         * Note: when using this mode, the line numbers reported in assertion failure
         * message to not match the line numbers in persisted snapshot files.
         */
        ACCORDING_TO_RAW_DATA,
        /**
         * Adds an offset according to the number of snapshot file header lines. Line
         * number 1 in rendered diffs maps to line number 1 in persisted snapshot files.
         * Consequently, real snapshot data starts at 1 + Number of header lines + 1.
         * <p>
         * Note: this option might be preferable over {@link #ACCORDING_TO_RAW_DATA} as
         * line numbers reported in assertion failures directly map to physical lines
         * within the persisted snapshot file.
         */
        ACCODRDING_TO_PERSISTED_SNAPSHOT_FILE;
    }

    /**
     * Defines how and if line endings will be normalized after snapshot serialization.
     * Normalization happens before invoking the {@link StructuralAssertions}.
     */
    @API(since = "1.1.0", status = Status.EXPERIMENTAL)
    enum NormalizeLineEndings {
        /**
         * Line endings will not be normalized. Use this settings if line endings are
         * significant for the test outcome.
         */
        NEVER,
        /** All line endings will be converted to LF (\n). */
        LF,
        /** All line endings will be converted to CRLF (\r\n). */
        CRLF,
        /** All line endings will be converted to the system's default line separator. */
        SYSTEM,
        /**
         * Line endings will be converted according to the local git's
         * <code>core.autocrlf</code> and <code>core.eol</code> settings. Will fall back
         * to the system's default line separator if git config values can not be
         * determined.
         */
        GIT
    }

}
