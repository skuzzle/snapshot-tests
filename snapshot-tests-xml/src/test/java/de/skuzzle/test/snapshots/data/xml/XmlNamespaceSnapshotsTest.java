package de.skuzzle.test.snapshots.data.xml;

import static de.skuzzle.test.snapshots.data.xml.XmlSnapshot.xml;
import static de.skuzzle.test.snapshots.data.xml.xmlunit.XPaths.localNamePath;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Pattern;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Test;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@EnableSnapshotTests
public class XmlNamespaceSnapshotsTest {

    @Test
    void testCompareAsLocalNames(Snapshot snapshot) throws Exception {
        snapshot
                .assertThat(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns1:root xmlns:ns1=\"foo:ns1\" xmlns:ns2=\"foo:ns2\"><ns2:node>text</ns2:node></ns1:root>")
                .as(xml)
                .matchesSnapshotStructure();
    }

    @Test
    void testXMLExampleFromJavaDoc(Snapshot snapshot) throws Exception {
        snapshot.assertThat(""
                + "<whatever:root xmlns:whatever=\"foo:1\" xmlns:doesntmatter=\"foo:2\">"
                + "    <doesntmatter:child>some text</doesntmatter:child>\n"
                + "</whatever:root>")
                .as(XmlSnapshot.xml()
                        .withXPathNamespaceContext(Map.of("ns1", "foo:1", "ns2", "foo:2"))
                        .withComparisonRules(rules -> rules
                                .pathAt("/ns1:root/ns2:child/text()").ignore()))
                .matchesSnapshotStructure();
    }

    @Test
    void testAsXmlTextCompare(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson();
        final SnapshotTestResult snapshotResult = snapshot.assertThat(myself).as(xml).matchesSnapshotText();
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
    }

    @Test
    void testAsXmlStructureCompare(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson();
        final SnapshotTestResult snapshotResult = snapshot.assertThat(myself).as(xml).matchesSnapshotStructure();
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
    }

    @Test
    void testXPathCustomRuleWithNamespaces(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson().setName("0000-02-02");
        snapshot.assertThat(myself)
                .as(XmlSnapshot.xml()
                        .withEnableXPathDebugging(true)
                        .withXPathNamespaceContext(Map.of(
                                "addr", "https://foo.bar",
                                "pers", "https://simon.taddiken.net"))
                        .withComparisonRules(rules -> rules
                                .pathAt("/pers:person/addr:address/city/text()").ignore()
                                .pathAt("/pers:person/name/text()").mustMatch(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"))))
                .matchesSnapshotStructure();
    }

    @Test
    void testXPathWitLocalNamesOnly(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson().setName("0000-02-02");
        snapshot.assertThat(myself)
                .as(XmlSnapshot.xml()
                        .withEnableXPathDebugging(true)
                        .withComparisonRules(rules -> rules
                                .pathAt(localNamePath("person", "address", "city") + "/text()").ignore()
                                .pathAt(localNamePath("person", "name") + "/text()")
                                .mustMatch(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"))))
                .matchesSnapshotStructure();
    }

    private Person determinePerson() {
        return new Person()
                .setName("Simon")
                .setSurname("Taddiken")
                .setBirthdate(LocalDate.of(1777, 1, 12))
                .setAddress(new Address()
                        .setCity("Bielefeld")
                        .setCountry("Germany")
                        .setStreet("Gibtsnicht-Straße")
                        .setNumber("1337")
                        .setZipCode("4711"));
    }

    @XmlType(namespace = "https://foo.bar")
    public static class Address {
        private String street;
        private String number;
        private String zipCode;
        private String city;
        private String country;

        public String getStreet() {
            return this.street;
        }

        public Address setStreet(String street) {
            this.street = street;
            return this;
        }

        public String getNumber() {
            return this.number;
        }

        public Address setNumber(String number) {
            this.number = number;
            return this;
        }

        public String getZipCode() {
            return this.zipCode;
        }

        public Address setZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public String getCity() {
            return this.city;
        }

        public Address setCity(String city) {
            this.city = city;
            return this;
        }

        public String getCountry() {
            return this.country;
        }

        public Address setCountry(String country) {
            this.country = country;
            return this;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("Street: ").append(street).append("\n")
                    .append("Number: ").append(number).append("\n")
                    .append("Zip: ").append(zipCode).append("\n")
                    .append("City: ").append(city).append("\n")
                    .append("Country: ").append(country).append("\n")
                    .toString();
        }
    }

    @XmlRootElement(namespace = "https://simon.taddiken.net")
    public static class Person {
        private String name;
        private String surname;
        private LocalDate birthdate;
        private Address address;

        public String getName() {
            return this.name;
        }

        public Person setName(String name) {
            this.name = name;
            return this;
        }

        public String getSurname() {
            return this.surname;
        }

        public Person setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public LocalDate getBirthdate() {
            return this.birthdate;
        }

        public Person setBirthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        @XmlElement(namespace = "https://foo.bar")
        public Address getAddress() {
            return this.address;
        }

        public Person setAddress(Address address) {
            this.address = address;
            return this;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("Name: ").append(name).append("\n")
                    .append("Surname: ").append(surname).append("\n")
                    .append("Birthdate: ").append(birthdate).append("\n")
                    .append("Address: ").append(address).append("\n")
                    .toString();
        }

    }
}
