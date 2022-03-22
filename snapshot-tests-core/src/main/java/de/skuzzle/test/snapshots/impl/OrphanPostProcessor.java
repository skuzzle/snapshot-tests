package de.skuzzle.test.snapshots.impl;

import static java.util.stream.Collectors.groupingBy;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.impl.OrphanDetectionResult.OrphanStatus;

/**
 * Post processes the results from both the {@link DynamicOrphanedSnapshotsDetector} and
 * {@link StaticOrphanedSnapshotDetector} according to these rules:
 * <p>
 * If both processors produced a result for the same snapshot file and one result was
 * {@link OrphanStatus#ACTIVE} it is not considered orphaned. Otherwise it is considered
 * orphaned if one result was {@link OrphanStatus#ORPHAN}.
 *
 * @author Simon Taddiken
 */
class OrphanPostProcessor {

    public Stream<OrphanDetectionResult> orphanedOnly(Collection<OrphanDetectionResult> results) {
        final Map<Path, List<OrphanDetectionResult>> resultsByFile = results.stream()
                .collect(groupingBy(OrphanDetectionResult::snapshotFile));

        return resultsByFile.values().stream().map(this::decideIsOrphan).flatMap(Optional::stream);
    }

    private Optional<OrphanDetectionResult> decideIsOrphan(Collection<OrphanDetectionResult> resultsForOneFile) {
        OrphanDetectionResult orphan = null;
        for (final OrphanDetectionResult result : resultsForOneFile) {
            if (result.status() == OrphanStatus.ACTIVE) {
                return Optional.empty();
            } else if (result.status() == OrphanStatus.ORPHAN) {
                orphan = result;
            }
        }
        return Optional.ofNullable(orphan);
    }
}
