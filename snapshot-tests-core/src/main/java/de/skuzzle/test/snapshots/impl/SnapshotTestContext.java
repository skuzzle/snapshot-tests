package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.ContextFiles;
import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.impl.OrphanCollectorHolder.OrphanCollector;
import de.skuzzle.test.snapshots.io.DirectoryResolver;
import de.skuzzle.test.snapshots.validation.Arguments;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Context object that pertains to the execution of a whole test that uses snapshot test.
 * <p>
 * In order to initiate the run of a test class which uses snapshot assertions, you need
 * to meet two requirements:
 * <ol>
 * <li>You need to obtain a {@link SnapshotConfiguration} instance from the resp. test
 * class via {@link SnapshotConfiguration#defaultConfigurationFor(Class)}. You can then
 * create a {@link SnapshotTestContext} instance from that configuration.</li>
 * <li>You need to call the lifecycle methods of the context object.</li>
 * </ol>
 * Most aspects of snapshot testing will already work if you just call
 * {@link #finalizeSnapshotTest()} after a test method. However, in order to support full
 * orphan detection capabilities, you need to register both all ignored tests and all
 * failed tests of the current test execution.
 *
 * @author Simon Taddiken
 * @since 1.1.0
 */
@API(status = Status.INTERNAL, since = "1.1.0")
public final class SnapshotTestContext {

    private final DynamicOrphanedSnapshotsDetector dynamicOrphanedSnapshotsDetector = new DynamicOrphanedSnapshotsDetector();
    private final SnapshotConfiguration snapshotConfiguration;
    private final TestFrameworkSupport testFrameworkSupport;

    private SnapshotDslImpl currentSnapshotTest;

    private SnapshotTestContext(SnapshotConfiguration snapshotConfiguration,
            TestFrameworkSupport testFrameworkSupport) {
        this.snapshotConfiguration = Arguments.requireNonNull(snapshotConfiguration,
                "snapshotConfiguration must not be null");
        this.testFrameworkSupport = Arguments.requireNonNull(testFrameworkSupport,
                "testFrameworkSupport must not be null");
    }

    public static SnapshotTestContext forConfiguration(SnapshotConfiguration snapshotConfiguration,
            TestFrameworkSupport testFrameworkSupport) {
        return new SnapshotTestContext(snapshotConfiguration, testFrameworkSupport);
    }

    /**
     * Returns the {@link SnapshotConfiguration}.
     *
     * @return The configuration.
     * @since 1.9.0
     */
    @API(status = Status.INTERNAL, since = "1.9.0")
    public SnapshotConfiguration snapshotConfiguration() {
        return snapshotConfiguration;
    }

    /**
     * Returns the TestFrameworkSupport that encapsulates test framework specific
     * behavior.
     *
     * @return Thes {@link TestFrameworkSupport}.
     * @since 1.10.0
     */
    @API(status = Status.INTERNAL, since = "1.10.0")
    public TestFrameworkSupport testFrameworkSupport() {
        return testFrameworkSupport;
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
        return Snapshot.class.isAssignableFrom(type);
    }

    /**
     * Creates a Snapshot object that can be injected into a test method as starting point
     * of the snapshot DSL.
     * <p>
     * This method changes the state of this context object. A new Snapshot can only be
     * created, when the current one has been retrieved and cleared using
     * {@link #finalizeSnapshotTest()}.
     *
     * @param testMethod The test method.
     * @return A Snapshot instance.
     * @see #finalizeSnapshotTest()
     */
    public de.skuzzle.test.snapshots.Snapshot createSnapshotTestFor(Method testMethod) {
        if (currentSnapshotTest != null) {
            throw new IllegalStateException("There is already a current snapshot test");
        }
        final ResultRecorder resultRecorder = ResultRecorder.forFreshTestMethod(this, testMethod);
        currentSnapshotTest = new SnapshotDslImpl(resultRecorder, snapshotConfiguration, testMethod);
        return currentSnapshotTest;
    }

    /**
     * Finalizes the current test by clearing this context and executing its remaining
     * assertions.
     *
     * @throws Exception If late assertions of the test fail.
     * @see #createSnapshotTestFor(Method)
     */
    public void finalizeSnapshotTest() throws Exception {
        final SnapshotDslImpl current = this.currentSnapshotTest;
        this.currentSnapshotTest = null;
        if (current != null) {
            current.executeFinalAssertions();
        }
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
    void recordSnapshotTestResult(SnapshotTestResult result) {
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

        final Stream<OrphanDetectionResult> staticOrphans = new StaticOrphanedSnapshotDetector(testFrameworkSupport)
                .detectOrphans(DirectoryResolver.BASE)
                .peek(collector::addRawResult);

        return new OrphanPostProcessor()
                .orphanedOnly(Stream.concat(dynamicOrphans, staticOrphans).collect(Collectors.toList()))
                .peek(collector::addPostProcessedResult)
                .map(OrphanDetectionResult::snapshotFile)
                .distinct()
                .peek(orphanedSnapshotFile -> {

                    final Path relativePath = DirectoryResolver.relativize(orphanedSnapshotFile.getParent());
                    if (deleteOrphaned) {
                        final ContextFiles contextFiles = InternalSnapshotNaming
                                .contextFilesForSnapshotFile(orphanedSnapshotFile);
                        contextFiles.deleteFiles();

                        System.err.printf("Deleted orphaned snapshot file %s in %s%n",
                                orphanedSnapshotFile.getFileName(), relativePath);
                    } else {
                        System.err.printf(
                                "Found orphaned snapshot file. Run with '@DeleteOrphanedSnapshots' annotation to remove: %s in %s%n",
                                orphanedSnapshotFile.getFileName(), relativePath);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "SnapshotTestContext[" + snapshotConfiguration + "]";
    }
}
