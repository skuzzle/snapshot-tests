package de.skuzzle.test.snapshots.impl;

import java.lang.System.Logger.Level;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.impl.OrphanCollectorHolder.OrphanCollector;
import de.skuzzle.test.snapshots.io.DirectoryResolver;
import de.skuzzle.test.snapshots.io.UncheckedIO;
import de.skuzzle.test.snapshots.validation.Arguments;

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

    private static final System.Logger log = System.getLogger(SnapshotTestContext.class.getName());

    private final DynamicOrphanedSnapshotsDetector dynamicOrphanedSnapshotsDetector = new DynamicOrphanedSnapshotsDetector();
    private final SnapshotConfiguration snapshotConfiguration;

    private SnapshotTestImpl currentSnapshotTest;

    private SnapshotTestContext(SnapshotConfiguration snapshotConfiguration) {
        this.snapshotConfiguration = Arguments.requireNonNull(snapshotConfiguration,
                "snapshotConfiguration must not be null");
    }

    public static SnapshotTestContext forTestClass(Class<?> testClass) {
        final SnapshotConfiguration configuration = DefaultSnapshotConfiguration.forTestClass(testClass);
        return new SnapshotTestContext(configuration);
    }

    static SnapshotTestContext forConfiguration(SnapshotConfiguration snapshotConfiguration) {
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
        currentSnapshotTest = new SnapshotTestImpl(this, snapshotConfiguration, testMethod);
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
     * Records a failed or skipped test within the currently executed test class. Knowing
     * which tests did not complete successfully is crucial for implementing orphaned
     * snapshot detection.
     *
     * @param testMethod Test method that failed or has been skipped.
     */
    public void recordFailedOrSkippedTest(Method testMethod) {
        this.dynamicOrphanedSnapshotsDetector.addFailedOrSkippedTestMethod(testMethod);
    }

    /**
     * Records the results from all snapshot assertions within a single test method.
     *
     * @param result A snapshot test result of a single snapshot assertion.
     */
    public void recordSnapshotTestResult(SnapshotTestResult result) {
        this.dynamicOrphanedSnapshotsDetector.addResult(result);
    }

    /**
     * Uses the collected context information to detect and optionally also clean up
     * orphaned snapshot files.
     *
     * @return The detected orphans.
     */
    public Collection<Path> detectOrCleanupOrphanedSnapshots() {
        final boolean deleteOrphaned = this.snapshotConfiguration.isDeleteOrphanedSnapshots();
        final Path globalSnapshotDirectory = this.snapshotConfiguration.determineSnapshotDirectory();

        // all orphan detection results will be reported with this collector. By default,
        // the collector doesn't do anything, but it can be exchanged with a mock
        // collector during unit tests to have easier access to the orphans.
        final OrphanCollector collector = OrphanCollectorHolder.getCollector();

        final Stream<OrphanDetectionResult> dynamicOrphans = dynamicOrphanedSnapshotsDetector
                .detectOrphans(globalSnapshotDirectory)
                .peek(collector::addRawResult);

        final Stream<OrphanDetectionResult> staticOrphans = new StaticOrphanedSnapshotDetector()
                .detectOrphans(DirectoryResolver.BASE)
                .peek(collector::addRawResult);

        return new OrphanPostProcessor()
                .orphanedOnly(Stream.concat(dynamicOrphans, staticOrphans).collect(Collectors.toList()))
                .peek(collector::addPostProcessedResult)
                .map(OrphanDetectionResult::snapshotFile)
                .distinct()
                .peek(orphaned -> {
                    if (deleteOrphaned) {
                        UncheckedIO.delete(orphaned);

                        log.log(Level.INFO, "Deleted orphaned snapshot file {0} in {1}",
                                orphaned.getFileName(), orphaned.getParent());
                    } else {
                        log.log(Level.WARNING,
                                "Found orphaned snapshot file. Run with '@DeleteOrphanedSnapshots' annotation to remove: {0} in {1}",
                                orphaned.getFileName(), orphaned.getParent());
                    }
                })
                .collect(Collectors.toList());
    }

}
