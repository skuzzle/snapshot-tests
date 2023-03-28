package de.skuzzle.test.snapshots.snippets;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

// tag::nested-tests[]
@EnableSnapshotTests
public class NestedTests {

    @Test
    void topLevelSnapshotTest(Snapshot snapshot) {
        // ...
    }

    @Nested
    @SnapshotDirectory("common-directory")
    class NestedInnerTest {

        @Nested
        @SnapshotTestOptions(alwaysPersistActualResult = true)
        class SecondLevelInnerClass {

            @Test
            void someSnapshotTest(Snapshot snapshot) {
                // ...
            }
        }

        @Nested
        class AnotherSecondLevelInnerClass {

            @Test
            void anotherSnapshotTest(Snapshot snapshot) {
                // ...
            }
        }
    }

}
// end::nested-tests[]
