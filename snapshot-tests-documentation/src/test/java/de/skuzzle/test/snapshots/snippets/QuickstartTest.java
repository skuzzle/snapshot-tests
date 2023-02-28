package de.skuzzle.test.snapshots.snippets;

// tag::quickstart[]
import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Test;

@EnableSnapshotTests // <1>
public class QuickstartTest {

    @Test
    void testCreateSnapshotAsText(Snapshot snapshot) throws Exception { // <2>
        final Person actual = Person.determinePerson();
        snapshot.assertThat(actual)
                .asText() // <3>
                .matchesSnapshotText(); // <4>
    }
}
// end::quickstart[]
//@formatter:on
