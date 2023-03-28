package de.skuzzle.test.snapshots.snippets;

import java.util.regex.Pattern;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.data.json.CompareMode;
import de.skuzzle.test.snapshots.data.json.JsonSnapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.Test;

@EnableSnapshotTests
public class JsonSnapshots {

    // tag::simple[]
    @Test
    void testJSONSnapshotWithDefaults(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot.assertThat(person)
                .as(JsonSnapshot.json())
                .matchesSnapshotStructure();
    }
    // end::simple[]

    // tag::customized[]
    @Test
    void testJSONSnapshotWithCustomOptions(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        final ObjectMapper customObjectMapper = new ObjectMapper();

        snapshot.assertThat(person)
                .as(JsonSnapshot.json(customObjectMapper) // <1>
                        .withCompareMode(CompareMode.NON_EXTENSIBLE) // <2>
                        .withComparisonRules(rules -> rules
                                .pathAt("address.city").ignore() // <3>
                                .pathAt("date").mustMatch(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"))) // <4>
                        .configure(mapper -> mapper
                                .registerModule(new JavaTimeModule()))) // <5>
                .matchesSnapshotStructure();
    }
    // end::customized[]

}
