package de.skuzzle.test.snapshots.snippets;

import org.junit.jupiter.api.Disabled;

import de.skuzzle.test.snapshots.EnableSnapshotTests;

public class SoftAssertions {

    @Disabled
    // tag::softassertions[]
    @EnableSnapshotTests(softAssertions = true)
    class SomeTest {
        // ...
    }
    // end::softassertions[]

}
