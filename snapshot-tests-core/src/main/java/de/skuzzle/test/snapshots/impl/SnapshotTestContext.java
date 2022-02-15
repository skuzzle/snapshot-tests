package de.skuzzle.test.snapshots.impl;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.io.UncheckedIO;

/**
 * Context object that pertains to the execution of a whole test class which is annotated
 * with {@link EnableSnapshotTests}.
 *
 * @author Simon Taddiken
 * @since 1.1.0
 * @see InternalSnapshotTest
 */
@API(status = Status.INTERNAL, since = "1.1.0")
public final class SnapshotTestContext {

    private static final Logger log = System.getLogger(SnapshotTestContext.class.getName());

    private final OrphanedSnapshotsDetector orphanedSnapshotDetector = new OrphanedSnapshotsDetector();
    private final SnapshotConfiguration snapshotConfiguration;

    private SnapshotTestImpl currentSnapshotTest;

    private SnapshotTestContext(SnapshotConfiguration snapshotConfiguration) {
        this.snapshotConfiguration = Objects.requireNonNull(snapshotConfiguration,
                "snapshotConfiguration must not be null");
    }

    private SnapshotTestContext(Class<?> testClass) {
        this.snapshotConfiguration = DefaultSnapshotConfiguration.forTestClass(testClass);
    }

    public static SnapshotTestContext forTestClass(Class<?> testClass) {
        return new SnapshotTestContext(testClass);
    }

    public static SnapshotTestContext forConfiguration(SnapshotConfiguration snapshotConfiguration) {
        return new SnapshotTestContext(snapshotConfiguration);
    }

    /**
     * Determines whether the parameters with the given type are eligible for injecting
     * the object that is created by {@link #createSnapshotTestFor(Method)}.
     *
     * @param type A type.
     * @return Whether the type of the object returned by
     *         {@link #createSnapshotTestFor(Method)} is compatible to the given type.
     */
    public boolean isSnapshotParameter(Class<?> type) {
        return type == Snapshot.class;
    }

    /**
     * Creates a {@link Snapshot} object that can be injected into a test method as
     * starting point of the snapshot DSL.
     * <p>
     * This method changes the state of this context object. A new
     * {@link InternalSnapshotTest} can only be created, when the current one has been
     * retrieved and cleared using {@link #clearCurrentSnapshotTest()}.
     *
     * @param testMethod The test method.
     * @return A {@link InternalSnapshotTest} instance.
     * @see #clearCurrentSnapshotTest()
     */
    public Snapshot createSnapshotTestFor(Method testMethod) {
        if (currentSnapshotTest != null) {
            throw new IllegalStateException("There is already a current snapshot test");
        }
        currentSnapshotTest = new SnapshotTestImpl(snapshotConfiguration, testMethod);
        return currentSnapshotTest;
    }

    /**
     * Clears and retrieves the {@link InternalSnapshotTest} instance that has been
     * created by {@link #createSnapshotTestFor(Method)}. This method is intended to be
     * used after the execution of a single test method in order to retrieve and process
     * the {@link InternalSnapshotTest#testResults()}.
     *
     * @return The {@link InternalSnapshotTest} instance or an empty optional if the
     *         currently executed test did not use snapshot assertions.
     * @see #createSnapshotTestFor(Method)
     */
    public Optional<InternalSnapshotTest> clearCurrentSnapshotTest() {
        final InternalSnapshotTest current = currentSnapshotTest;
        this.currentSnapshotTest = null;
        return Optional.ofNullable(current);
    }

    /**
     * Records a failing test within the currently executed test class. Knowing which
     * tests failed is crucial for implementing orphaned snapshot detection.
     *
     * @param testMethod Test method that failed.
     */
    public void recordFailedTest(Method testMethod) {
        this.orphanedSnapshotDetector.addFailedTestMethod(testMethod);
    }

    /**
     * Records the results from all snapshot assertions within a single test method.
     *
     * @param results The snapshot test results of a single test method.
     */
    public void recordSnapshotTestResults(Collection<SnapshotTestResult> results) {
        this.orphanedSnapshotDetector.addAllFrom(results);
    }

    /**
     * Uses the collected context information to detect and optionally also clean up
     * orphaned snapshot files.
     *
     * @return The detected orphans.
     */
    public Collection<Path> detectOrCleanupOrphanedSnapshots() {
        final boolean deleteOrphaned = this.snapshotConfiguration.isForceUpdateSnapshotsGlobal();
        final Path snapshotDirectory = snapshotConfiguration.determineSnapshotDirectory();
        return this.orphanedSnapshotDetector.findOrphanedSnapshotsIn(snapshotDirectory).stream()
                .peek(orphaned -> {
                    if (deleteOrphaned) {
                        UncheckedIO.delete(orphaned);

                        log.log(Level.INFO, "Deleted orphaned snapshot file {0} in ",
                                orphaned.getFileName(), orphaned.getParent());
                    } else {
                        log.log(Level.WARNING,
                                "Found orphaned snapshot file. Run with 'forceUpdateSnapshots' option to remove: {0} in {1}",
                                orphaned.getFileName(), orphaned.getParent());
                    }
                })
                .collect(Collectors.toList());
    }

}
