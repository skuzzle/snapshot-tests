package de.skuzzle.test.snapshots.snippets;

import de.skuzzle.test.snapshots.EnableSnapshotTests;

import org.junit.jupiter.api.Disabled;

public class SoftAssertions {

    @Disabled
    // tag::softassertions[]
    @EnableSnapshotTests(softAssertions = true)
    class SomeTest {
        // ...
    }
    // end::softassertions[]

}
