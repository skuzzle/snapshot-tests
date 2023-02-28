package de.skuzzle.test.snapshots.snippets;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.data.json.JsonSnapshot;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Test;

@EnableSnapshotTests
public class ComparisonTests {

    private Person person() {
        return okPerson();
        // return nokPerson();
    }

    private Person okPerson() {
        return Person.determinePerson();
    }

    private Person nokPerson() {
        return okPerson().setName("Peter").setSurname("Pan");
    }

    // tag::disabled-assertions[]
    @Test
    void testWithDisabledAssertions(Snapshot snapshot) throws Exception {
        final Person person1 = person();
        final Person person2 = person();
        final Person person3 = person();
        snapshot.assertThat(person1).asText().disabled(); // <1>
        snapshot.assertThat(person2).asText().disabledBecause("See bug reference TBD-1337"); // <2>
        snapshot.assertThat(person3).asText().matchesSnapshotText(); // <3>
    }
    // end::disabled-assertions[]

    // tag::multiple-assertions[]
    @Test
    void testWithMultipleAssertions(Snapshot snapshot) throws Exception {
        final Person person1 = person();
        final Person person2 = person();
        snapshot.assertThat(person1).asText().matchesSnapshotText();
        snapshot.assertThat(person2).asText().matchesSnapshotText();
    }
    // end::multiple-assertions[]

    @Test
    void testCreateSnapshotAsText(Snapshot snapshot) throws Exception {
        final Person actual = person();

        // tag::text-compare[]
        snapshot.assertThat(actual)
                .asText()
                .matchesSnapshotText(); // <1>
        // end::text-compare[]
    }

    @Test
    void testCreateSnapshotAsJson(Snapshot snapshot) throws Exception {
        final Person actual = person();

        // tag::structure-compare-json[]
        snapshot.assertThat(actual)
                .as(JsonSnapshot.json()) // <1>
                .matchesSnapshotStructure(); // <2>
        // end::structure-compare-json[]
    }

    @Test
    void testCreateSnapshotAsXml(Snapshot snapshot) throws Exception {
        final Person actual = person();

        // tag::structure-compare-xml[]
        snapshot.assertThat(actual)
                .as(XmlSnapshot.xml())
                .matchesSnapshotStructure();
        // end::structure-compare-xml[]
    }
}
