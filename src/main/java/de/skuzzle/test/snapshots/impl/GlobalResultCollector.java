package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.skuzzle.test.snapshots.SnapshotResult;

class GlobalResultCollector {

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
                .collect(Collectors.joining("\n"));
    }

    public GlobalResultCollector addAllFrom(LocalResultCollector other) {
        this.results.addAll(other.results());
        return this;
    }

    public boolean containsResultForSnapshotAt(Path snapshotFile) {
        return results.stream()
                .map(SnapshotResult::snapshotFile)
                .anyMatch(snapshotFileFromResult -> UncheckedIO.isSameFile(snapshotFileFromResult, snapshotFile));
    }

}
