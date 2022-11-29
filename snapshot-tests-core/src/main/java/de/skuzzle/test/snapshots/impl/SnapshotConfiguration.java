package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Supplies configuration options to the snapshot test execution.
 *
 * @author Simon Taddiken
 * @since 1.1.0
 * @see DefaultSnapshotConfiguration
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
     *
     * @deprecated Since 1.7.0 - Only introduced for backward compatibility.
     */
    @Deprecated(since = "1.7.0", forRemoval = true)
    @API(status = Status.DEPRECATED, since = "1.7.0")
    static SnapshotConfiguration legacyConfigurationFor(Class<?> testClass) {
        return LegacySnapshotConfiguration.forTestClass(testClass);
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
     * Whether to delete orphaned snapshot files during test execution.
     *
     * @return Whether orphaned snapshot files should be removed.
     */
    boolean isDeleteOrphanedSnapshots();

    /**
     * Determines whether snapshots are to be forcefully updated during the execution of a
     * whole test class.
     *
     * @return Whether to forcefully update snapshots.
     */
    boolean isForceUpdateSnapshotsGlobal();

    /**
     * Determines whether snapshots are to be forcefully updated during the execution of
     * the given test method.
     *
     * @param testMethod The test method.
     * @return Whether to forcefully update snapshots.
     */
    boolean isForceUpdateSnapshotsLocal(Method testMethod);

    /**
     * Whether soft assertions shall be used. When set to true, a failing snapshot
     * assertion will not make the test immediately fail. Instead, all snapshot test
     * results are collected and processed at once when the test method finishes.
     *
     * @return Whether to use soft assertions.
     * @deprecated Since 1.7.0 - Soft assertion will no longer be supported with version
     *             2.0
     */
    @Deprecated(since = "1.7.0", forRemoval = true)
    @API(status = Status.DEPRECATED, since = "1.7.0")
    boolean isSoftAssertions();

}