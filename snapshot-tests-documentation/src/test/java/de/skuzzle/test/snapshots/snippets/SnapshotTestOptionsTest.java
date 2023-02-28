package de.skuzzle.test.snapshots.snippets;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Test;

@EnableSnapshotTests
class SnapshotTestOptionsTest {

    // tag::alwaysPersistActualResult[]
    @Test
    @SnapshotTestOptions(alwaysPersistActualResult = true)
    void testAlwaysPersistActual(Snapshot snapshot) throws Exception {

    }
    // end::alwaysPersistActualResult[]

    // tag::alwaysPersistActualRawResult[]
    @Test
    @SnapshotTestOptions(alwaysPersistRawResult = true)
    void testAlwaysPersistActualRaw(Snapshot snapshot) throws Exception {

    }
    // end::alwaysPersistActualRawResult[]
}
