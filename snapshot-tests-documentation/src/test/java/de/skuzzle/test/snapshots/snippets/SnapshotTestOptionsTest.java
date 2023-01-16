package de.skuzzle.test.snapshots.snippets;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
class SnapshotTestOptionsTest {

    // tag::alwaysPersistActualResult[]
    @Test
    @SnapshotTestOptions(alwaysPersistActualResult = true)
    void testAlwaysPersistActual(Snapshot snapshot) throws Exception {

    }
    // tag::alwaysPersistActualResult[]

    // tag::alwaysPersistActualRawResult[]
    @Test
    @SnapshotTestOptions(alwaysPersistRawResult = true)
    void testAlwaysPersistActualRaw(Snapshot snapshot) throws Exception {

    }
    // tag::alwaysPersistActualRawResult[]
}
