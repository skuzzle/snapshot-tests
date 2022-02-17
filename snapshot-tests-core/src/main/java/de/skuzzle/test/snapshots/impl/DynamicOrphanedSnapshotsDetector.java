package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.io.UncheckedIO;

/**
 * Dynamically (=while running the tests) detects orphaned snapshots by observing each
 * snapshot test's result to determine whether the snapshot assertions within all
 * successfully executed tests had changed.
 *
 * @author Simon Taddiken
 * @see StaticOrphanedSnapshotDetector
 */
final class DynamicOrphanedSnapshotsDetector {

    private final Set<Method> failedTestMethods = new HashSet<>();
    private final List<SnapshotTestResult> results = new ArrayList<>();

    public DynamicOrphanedSnapshotsDetector addAllFrom(Collection<SnapshotTestResult> other) {
        this.results.addAll(Objects.requireNonNull(other));
        return this;
    }

    public void addFailedTestMethod(Method testMethod) {
        this.failedTestMethods.add(Objects.requireNonNull(testMethod));
    }

    public Stream<Path> detectOrphans(Path snapshotDirectory) {
        try (final var files = UncheckedIO.list(snapshotDirectory)) {
            return files
                    .filter(this::isOrphanedSnapshot)
                    .collect(Collectors.toList())
                    .stream();
        }
    }

    private boolean isOrphanedSnapshot(Path potentialSnapshotFile) {
        if (!InternalSnapshotNaming.isSnapshotFile(potentialSnapshotFile)) {
            return false;
        }
        // we can not detect orphaned snapshots for failed tests because the test might
        // have failed before creating the snapshot
        if (pertainsToFailedTest(potentialSnapshotFile)) {
            return false;
        }
        return !testResultsContain(potentialSnapshotFile);
    }

    private boolean pertainsToFailedTest(Path snapshotFile) {
        return failedTestMethods.stream()
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
