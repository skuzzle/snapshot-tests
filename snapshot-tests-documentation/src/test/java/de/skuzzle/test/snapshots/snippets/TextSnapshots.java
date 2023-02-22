package de.skuzzle.test.snapshots.snippets;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.data.text.TextSnapshot;
import de.skuzzle.test.snapshots.data.text.TextSnapshot.DiffFormat;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class TextSnapshots {

    // tag::native[]
    @Test
    void testNativeTextSnapshot(Snapshot snapshot) {
        final Object someObject = "text";
        snapshot.assertThat(someObject)
                .asText()
                .matchesSnapshotText();
    }
    // end::native[]

    // tag::customized[]
    @Test
    void testCustomizedTextSnapshot(Snapshot snapshot) {
        final Object someObject = "text";
        snapshot.assertThat(someObject)
                .as(TextSnapshot.text()
                        .withContextLines(10) // <1>
                        .withIgnoreWhitespaces(true) // <2>
                        .withDiffFormat(DiffFormat.SPLIT)) // <3>
                .matchesSnapshotText(); // <4>
    }
    // end::customized[]
}
