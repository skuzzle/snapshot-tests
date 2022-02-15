package de.skuzzle.test.snapshots.impl;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.io.UncheckedIO;

/**
 * Collects the result of all test cases within a class that is annotated with
 * {@link EnableSnapshotTests}.
 *
 * @author Simon Taddiken
 */
final class OrphanedSnapshotsDetector {

    private static final Logger log = System.getLogger(OrphanedSnapshotsDetector.class.getName());

    private final Set<Method> failedTestMethods = new HashSet<>();
    private final List<SnapshotTestResult> results = new ArrayList<>();

    public OrphanedSnapshotsDetector addAllFrom(Collection<SnapshotTestResult> other) {
        this.results.addAll(Objects.requireNonNull(other));
        return this;
    }

    public void addFailedTestMethod(Method testMethod) {
        this.failedTestMethods.add(Objects.requireNonNull(testMethod));
    }

    private Collection<Path> findOrphanedSnapshotsIn(Path snapshotDirectory) {
        try (final var files = UncheckedIO.list(snapshotDirectory)) {
            return files
                    .filter(this::isOrphanedSnapshot)
                    .collect(Collectors.toList());
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

    public void detectOrCleanupOrphanedSnapshots(Path snapshotDirectory, boolean deleteOrphaned) {
        findOrphanedSnapshotsIn(snapshotDirectory)
                .forEach(orphaned -> {
                    if (deleteOrphaned) {
                        UncheckedIO.delete(orphaned);

                        log.log(Level.INFO, "Deleted orphaned snapshot file {0}", orphaned);
                    } else {
                        log.log(Level.WARNING,
                                "Found orphaned snapshot file. Run with 'forceUpdateSnapshots' option to remove: {0}",
                                orphaned);
                    }
                });
    }
}
