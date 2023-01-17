package de.skuzzle.test.snapshots.snippets;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class ParameterizedTests {

    // tag::testWithParams[]
    @ParameterizedTest
    @ValueSource(strings = { "string1", "string2" })
    void testParameterized(String param, Snapshot snapshot) {

        snapshot.namedAccordingTo(SnapshotNaming.withParameters(param))
                .assertThat(param)
                .asText()
                .matchesSnapshotText();
    }
    // end::testWithParams[]

}
