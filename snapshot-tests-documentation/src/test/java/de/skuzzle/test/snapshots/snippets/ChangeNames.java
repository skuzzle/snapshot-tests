package de.skuzzle.test.snapshots.snippets;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
class ChangeNames {

    // tag::changeNameStaticString[]
    @Test
    void testChangeSnapshotNameStatic(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot
                .named("person") // <1>
                .assertThat(person)
                .asText()
                .matchesSnapshotText();
    }
    // end::changeNameStaticString[]

    // tag::changeNameStaticStrategy[]
    @Test
    void testChangeSnapshotNameStaticStrategy(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot
                .namedAccordingTo(SnapshotNaming.constant("person")) // <1>
                .assertThat(person)
                .asText()
                .matchesSnapshotText();
    }
    // end::changeNameStaticStrategy[]
}
