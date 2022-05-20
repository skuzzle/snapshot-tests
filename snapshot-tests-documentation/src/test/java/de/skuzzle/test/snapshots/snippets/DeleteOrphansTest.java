package de.skuzzle.test.snapshots.snippets;

import de.skuzzle.test.snapshots.DeleteOrphanedSnapshots;
import de.skuzzle.test.snapshots.EnableSnapshotTests;

// tag::delete-orphans[]
@EnableSnapshotTests
@DeleteOrphanedSnapshots // <1>
public class DeleteOrphansTest {

    // ...
}
// end::delete-orphans[]
