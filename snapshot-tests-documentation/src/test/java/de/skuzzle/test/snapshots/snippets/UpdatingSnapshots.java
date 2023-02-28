package de.skuzzle.test.snapshots.snippets;

import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
@EnableSnapshotTests
class UpdatingSnapshots {

    // tag::justupdate[]
    @Test
    void testUpdateSnapshot(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot.assertThat(person)
                .asText()
                .justUpdateSnapshot(); // <1>
    }
    // end::justupdate[]

    // tag::force-update-on-method[]
    @Test
    @ForceUpdateSnapshots
    void testUpdateSnapshotWithAnnotation(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot.assertThat(person)
                .asText()
                .matchesSnapshotText();
    }
    // end::force-update-on-method[]

    @Disabled
    // tag::force-update-on-class[]
    @EnableSnapshotTests
    @ForceUpdateSnapshots
    class PersonTest {

        @Test
        void testUpdateSnapshotWithAnnotation(Snapshot snapshot) {
            final Person person = Person.determinePerson();
            snapshot.assertThat(person)
                    .asText()
                    .matchesSnapshotText();
        }

    }
    // end::force-update-on-class[]

}
