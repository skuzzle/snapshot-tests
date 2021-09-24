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

import de.skuzzle.test.snapshots.SnapshotResult;

class GlobalResultCollector {

    private final Set<Method> failedTestMethods = new HashSet<>();
    private final List<SnapshotResult> results = new ArrayList<>();

    public SnapshotResult add(SnapshotResult result) {
        this.results.add(Objects.requireNonNull(result));
        return result;
    }

    @Override
    public String toString() {
        return results.stream()
                .map(SnapshotResult::snapshotFile)
                .map(Path::toString)
                .collect(Collectors.joining("\n"))
                + "\n" +
                failedTestMethods.stream()
                        .map(Method::getName)
                        .collect(Collectors.joining("\n"));
    }

    public GlobalResultCollector addAllFrom(LocalResultCollector other) {
        this.results.addAll(other.results());
        return this;
    }

    public void addFailedTestMethod(Method testMethod) {
        this.failedTestMethods.add(Objects.requireNonNull(testMethod));
    }

    public Collection<Path> findOrphanedSnapshotsIn(Path snapshotDirectory) {
        try (final var files = UncheckedIO.list(snapshotDirectory)) {
            return files
                    .filter(existingSnapshot -> isOrphaned(existingSnapshot))
                    .collect(Collectors.toList());
        }
    }

    public boolean isOrphaned(Path potentialSnapshotFile) {
        if (!SnapshotNaming.isSnapshotFile(potentialSnapshotFile)) {
            return false;
        }
        final boolean pertainsToFailedTests = failedTestMethods.stream()
                .anyMatch(failedTestMethod -> SnapshotNaming.isSnapshotFileForMethod(potentialSnapshotFile,
                        failedTestMethod));
        if (pertainsToFailedTests) {
            return false;
        }
        return !results.stream()
                .map(SnapshotResult::snapshotFile)
                .anyMatch(snapshotFileFromResult -> UncheckedIO.isSameFile(snapshotFileFromResult,
                        potentialSnapshotFile));
    }

}
