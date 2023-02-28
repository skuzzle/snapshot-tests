package de.skuzzle.test.snapshots.snippets;

import static java.util.regex.Pattern.compile;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Test;

@EnableSnapshotTests
public class XmlSnapshots {
    // tag::simple[]
    @Test
    void testXMLnapshotWithDefaults(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot.assertThat(person)
                .as(XmlSnapshot.xml())
                .matchesSnapshotStructure();
    }
    // end::simple[]

    // tag::customized[]
    @Test
    void testXMLSnapshotWithCustomOptions(Snapshot snapshot) throws JAXBException {
        final Person person = Person.determinePerson();
        final JAXBContext customJaxbContext = JAXBContext.newInstance(Person.class);

        snapshot.assertThat(person)
                .as(XmlSnapshot.xml()
                        .withJAXBContext(customJaxbContext) // <1>
                        .withMarshaller(ctx -> ctx.createMarshaller()) // <2>
                        .withEnableXPathDebugging(true) // <3>
                        .withXPathNamespaceContext(Map.of("ns1", "foo:1", "ns2", "foo:2")) // <4>
                        .withComparisonRules(rules -> rules // <5>
                                .pathAt("/person/address/city/text()").ignore()
                                .pathAt("/person/date/text()").mustMatch(compile("\\d{4}-\\d{2}-\\d{2}"))
                                .pathAt("/ns1:root/ns2:child/text()").ignore())
                        .compareUsing(compareAssert -> compareAssert.areSimilar()) // <6>
                        .withPrettyPrintStringXml(true) // <7>

                )
                .matchesSnapshotStructure();
    }
    // end::customized[]
}
