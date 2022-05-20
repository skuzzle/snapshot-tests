package de.skuzzle.test.snapshots.snippets;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.data.json.JsonSnapshot;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot;

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
