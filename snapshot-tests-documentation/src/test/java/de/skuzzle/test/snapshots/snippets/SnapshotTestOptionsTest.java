package de.skuzzle.test.snapshots.snippets;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Test;

@EnableSnapshotTests
class SnapshotTestOptionsTest {

    @Test
    // tag::normalizeLineEndingsGit[]
    @SnapshotTestOptions(normalizeLineEndings = SnapshotTestOptions.NormalizeLineEndings.GIT)
    // end::normalizeLineEndingsGit[]
    void normalizeLineEndingsGit(Snapshot snapshot) {

    }

    // tag::alwaysPersistActualResult[]
    @Test
    @SnapshotTestOptions(alwaysPersistActualResult = true)
    void testAlwaysPersistActual(Snapshot snapshot) {

    }
    // end::alwaysPersistActualResult[]

    // tag::alwaysPersistActualRawResult[]
    @Test
    @SnapshotTestOptions(alwaysPersistRawResult = true)
    void testAlwaysPersistActualRaw(Snapshot snapshot) {

    }
    // end::alwaysPersistActualRawResult[]
}
