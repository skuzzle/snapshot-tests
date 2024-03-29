package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.ContextFiles;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
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
                results.stream()
                        .map(SnapshotTestResult::contextFiles)
                        .map(ContextFiles::snapshotFile)
                        .map(Path::getParent))
                .filter(Files::exists);
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
        // test might have failed before creating the snapshot. But for natively disabled
        // tests we can safely assume that they are not orphaned
        OrphanStatus result;
        if (pertainsToDisabledAssertion(snapshotFile)) {
            result = OrphanStatus.ACTIVE;
        } else if (pertainsToFailedOrSkippedTest(snapshotFile)) {
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

    private boolean pertainsToDisabledAssertion(Path snapshotFile) {
        return results.stream()
                .filter(result -> result.status() == SnapshotStatus.DISABLED)
                .anyMatch(result -> result.contextFiles().snapshotFile().equals(snapshotFile));
    }

    private boolean testResultsContain(Path snapshotFile) {
        return results.stream()
                .map(SnapshotTestResult::contextFiles)
                .map(ContextFiles::snapshotFile)
                .filter(Files::exists)
                .anyMatch(snapshotFileFromResult -> UncheckedIO.isSameFile(snapshotFileFromResult,
                        snapshotFile));
    }

}
