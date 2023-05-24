package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.SnapshotTestOptions.DiffFormat;
import de.skuzzle.test.snapshots.SnapshotTestOptions.NormalizeLineEndings;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Supplies configuration options to the snapshot test execution.
 *
 * @author Simon Taddiken
 * @see DefaultSnapshotConfiguration
 * @since 1.1.0
 */
@API(status = Status.INTERNAL, since = "1.1.0")
public interface SnapshotConfiguration {

    /**
     * Creates a SnapshotConfiguration for the given test class.
     *
     * @param testClass The test class.
     * @return The configuration.
     * @since 1.7.0
     */
    @API(status = Status.INTERNAL, since = "1.7.0")
    static SnapshotConfiguration defaultConfigurationFor(Class<?> testClass) {
        return DefaultSnapshotConfiguration.forTestClass(testClass);
    }

    /**
     * Determines the directory into which the snapshot files for the currently executed
     * test class are persisted.
     *
     * @return The snapshot directory.
     */
    Path determineSnapshotDirectory();

    /**
     * The class containing the currently executed test.
     *
     * @return The test class.
     */
    Class<?> testClass();

    /**
     * Determines whether to persist most recent actual test result in a separate file
     * next to the snapshot.
     *
     * @param testMethod The test method.
     * @return Whether to persist most recent actual result.
     * @since 1.7.0
     */
    @API(status = Status.INTERNAL, since = "1.7.0")
    boolean alwaysPersistActualResult(Method testMethod);

    /**
     * Determines whether to persist raw snapshot result in a separate file.
     *
     * @param testMethod The test method.
     * @return Whether to persist raw results.
     * @since 1.7.0
     */
    @API(status = Status.INTERNAL, since = "1.7.0")
    boolean alwaysPersistRawResult(Method testMethod);

    /**
     * Defines how many lines of context are rendered around a changed line-
     *
     * @param testMethod The test method.
     * @return Number of context lines.
     * @since 1.7.0
     */
    @API(status = Status.INTERNAL, since = "1.7.0")
    int textDiffContextLines(Method testMethod);

    /**
     * Whether to add an offset to the line numbers in rendered diffs so that the rendered
     * line numbers match the physical lines in the snapshot file.
     *
     * @param testMethod The test method.
     * @return Whether to add a line number offset.
     * @since 1.7.1
     */
    @API(status = Status.INTERNAL, since = "1.7.1")
    boolean addOffsetToReportedLinenumbers(Method testMethod);

    /**
     * Whether to delete orphaned snapshot files during test execution.
     *
     * @return Whether orphaned snapshot files should be removed.
     */
    boolean isDeleteOrphanedSnapshots();

    /**
     * Determines whether snapshots are to be forcefully updated during the execution of
     * the given test method.
     *
     * @param testMethod The test method.
     * @return Whether to forcefully update snapshots.
     */
    boolean isForceUpdateSnapshots(Method testMethod);

    /**
     * How to normalize line endings after default snapshot serialization.
     *
     * @param testMethod The test method.
     * @return Whether to normalize line endings.
     * @since 1.10.0
     */
    @API(since = "1.10.0", status = Status.INTERNAL)
    NormalizeLineEndings normalizeLineEndings(Method testMethod);

    /**
     * Defines how diffs are rendered when structural comparison failed.
     *
     * @param testMethod The test method.
     * @return The diff format to use.
     */
    @API(status = Status.INTERNAL, since = "1.10.0")
    DiffFormat diffFormat(Method testMethod);

    /**
     * Whether the method is allowed to produce multiple snapshots with same name.
     *
     * @param testMethod The test method
     * @return Whether the method is allowed to produce multiple snapshots with same name.
     * @since 2.0.0
     */
    boolean allowMultipleSnapshotsWithSameName(Method testMethod);

}
