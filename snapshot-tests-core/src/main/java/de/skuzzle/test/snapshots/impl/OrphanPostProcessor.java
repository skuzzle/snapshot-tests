package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.impl.OrphanDetectionResult.Result;
import de.skuzzle.test.snapshots.io.UncheckedIO;

class OrphanPostProcessor {

    public Stream<OrphanDetectionResult> orphanedOnly(Collection<OrphanDetectionResult> results) {
        return results.stream()
                .map(result -> resultsFor(result.snapshotFile(), results))
                .distinct()
                .map(this::decide)
                .flatMap(Optional::stream);
    }

    private Optional<OrphanDetectionResult> decide(Map<Result, OrphanDetectionResult> map) {
        if (map.containsKey(Result.ACTIVE)) {
            return Optional.empty();
        }
        return Optional.ofNullable(map.get(Result.ORPHAN));

    }

    private Map<Result, OrphanDetectionResult> resultsFor(Path file, Collection<OrphanDetectionResult> allResults) {
        final Map<Result, OrphanDetectionResult> result = new HashMap<>();
        for (final OrphanDetectionResult orphanDetectionResult : allResults) {
            if (UncheckedIO.isSameFile(orphanDetectionResult.snapshotFile(), file)) {
                result.put(orphanDetectionResult.result(), orphanDetectionResult);
            }
        }
        return result;
    }
}
