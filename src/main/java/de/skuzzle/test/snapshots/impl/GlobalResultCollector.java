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

import de.skuzzle.test.snapshots.SnapshotAssertions;
import de.skuzzle.test.snapshots.SnapshotResult;

/**
 * Collects the result of all test cases within a class that is annotated with
 * {@link SnapshotAssertions}.
 *
 * @author Simon Taddiken
 */
final class GlobalResultCollector {

    private final Set<Method> failedTestMethods = new HashSet<>();
    private final List<SnapshotResult> results = new ArrayList<>();

    public SnapshotResult add(SnapshotResult result) {
        this.results.add(Objects.requireNonNull(result));
        return result;
    }

    public GlobalResultCollector addAllFrom(LocalResultCollector other) {
        this.results.addAll(Objects.requireNonNull(other).results());
        return this;
    }

    public void addFailedTestMethod(Method testMethod) {
        this.failedTestMethods.add(Objects.requireNonNull(testMethod));
    }

    public Collection<Path> findOrphanedSnapshotsIn(Path snapshotDirectory) {
        try (final var files = UncheckedIO.list(snapshotDirectory)) {
            return files
                    .filter(existingSnapshot -> isOrphanedSnapshot(existingSnapshot))
                    .collect(Collectors.toList());
        }
    }

    private boolean isOrphanedSnapshot(Path potentialSnapshotFile) {
        if (!SnapshotNaming.isSnapshotFile(potentialSnapshotFile)) {
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
                .anyMatch(failedTestMethod -> SnapshotNaming.isSnapshotFileForMethod(snapshotFile, failedTestMethod));
    }

    private boolean testResultsContain(Path snapshotFile) {
        return results.stream()
                .map(SnapshotResult::snapshotFile)
                .anyMatch(snapshotFileFromResult -> UncheckedIO.isSameFile(snapshotFileFromResult,
                        snapshotFile));
    }
}
