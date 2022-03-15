package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.impl.OrphanDetectionResult.Result;
import de.skuzzle.test.snapshots.io.UncheckedIO;

/**
 * Post processes the results from both the {@link DynamicOrphanedSnapshotsDetector} and
 * {@link StaticOrphanedSnapshotDetector} according to these rules:
 * <p>
 * If both processors produced a result for the same snapshot file and one result was
 * {@link Result#ACTIVE} it is not considered orphaned. Otherwise it is considered
 * orphaned if one result was {@link Result#ORPHAN}.
 *
 * @author Simon Taddiken
 */
class OrphanPostProcessor {

    public Stream<OrphanDetectionResult> orphanedOnly(Collection<OrphanDetectionResult> results) {
        return results.stream()
                .map(result -> resultsFor(result.snapshotFile(), results))
                .distinct()
                .map(this::decideIsOrphan)
                .flatMap(Optional::stream);
    }

    private Optional<OrphanDetectionResult> decideIsOrphan(Map<Result, OrphanDetectionResult> map) {
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
