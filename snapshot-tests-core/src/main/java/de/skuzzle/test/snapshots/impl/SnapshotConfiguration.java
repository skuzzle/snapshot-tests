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
interface SnapshotConfiguration {

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
     */
    boolean isSoftAssertions();

}