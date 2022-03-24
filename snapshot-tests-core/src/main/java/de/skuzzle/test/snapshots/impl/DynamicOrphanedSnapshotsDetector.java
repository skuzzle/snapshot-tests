package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.impl.OrphanDetectionResult.OrphanStatus;
import de.skuzzle.test.snapshots.io.UncheckedIO;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Dynamically (=while running the tests) detects orphaned snapshots by observing each
 * snapshot test's result to determine whether the snapshot assertions within all
 * successfully executed tests had changed.
 *
 * @author Simon Taddiken
 * @see StaticOrphanedSnapshotDetector
 */
final class DynamicOrphanedSnapshotsDetector {

    private final Set<Method> failedOrSkippedTestMethods = new HashSet<>();
    private final List<SnapshotTestResult> results = new ArrayList<>();

    public void addResult(SnapshotTestResult result) {
        this.results.add(result);
    }

    public void addFailedOrSkippedTestMethod(Method testMethod) {
        this.failedOrSkippedTestMethods.add(Arguments.requireNonNull(testMethod));
    }

    private Stream<Path> snapshotDirectories(Path globalSnapshotDirectory) {
        return Stream.concat(Stream.of(globalSnapshotDirectory),
                results.stream().map(SnapshotTestResult::targetFile).map(Path::getParent));
    }

    public Stream<OrphanDetectionResult> detectOrphans(Path globalSnapshotDirectory) {
        try (final var files = snapshotDirectories(globalSnapshotDirectory).flatMap(UncheckedIO::list)) {
            return files
                    .filter(this::isSnapshotFile)
                    .map(this::isOrphanedSnapshot)
                    .collect(Collectors.toList())
                    .stream();
        }
    }

    private boolean isSnapshotFile(Path potentialSnapshotFile) {
        return InternalSnapshotNaming.isSnapshotFile(potentialSnapshotFile);
    }

    private OrphanDetectionResult isOrphanedSnapshot(Path snapshotFile) {
        // we can not detect orphaned snapshots for failed or skipped tests because the
        // test might have failed before creating the snapshot
        OrphanStatus result;
        if (pertainsToFailedOrSkippedTest(snapshotFile)) {
            result = OrphanStatus.UNSURE;
        } else {
            final boolean containedInTest = testResultsContain(snapshotFile);
            result = containedInTest ? OrphanStatus.ACTIVE : OrphanStatus.ORPHAN;
        }
        return new OrphanDetectionResult(getClass().getSimpleName(), snapshotFile, result);
    }

    private boolean pertainsToFailedOrSkippedTest(Path snapshotFile) {
        return failedOrSkippedTestMethods.stream()
                .anyMatch(failedTestMethod -> InternalSnapshotNaming.isSnapshotFileForMethod(snapshotFile,
                        failedTestMethod));
    }

    private boolean testResultsContain(Path snapshotFile) {
        return results.stream()
                .map(SnapshotTestResult::targetFile)
                .anyMatch(snapshotFileFromResult -> UncheckedIO.isSameFile(snapshotFileFromResult,
                        snapshotFile));
    }

}
