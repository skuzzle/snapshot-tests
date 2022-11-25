package de.skuzzle.test.snapshots.xml;

import static de.skuzzle.test.snapshots.data.xml.XmlSnapshot.xml;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.diff.DifferenceEvaluators;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot;

@EnableSnapshotTests
public class SnapshotsTest {

    @Test
    void testXmlAlreadyAStringWithPrettyPrint(Snapshot snapshot) throws Exception {
        final SnapshotTestResult snapshotResult = snapshot
                .assertThat("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><node>text</node></root>").as(xml)
                .matchesSnapshotStructure();

        assertThat(snapshotResult.serializedActual()).isEqualTo(String.format(""
                + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>%n"
                + "<root>%n"
                + "  <node>text</node>%n"
                + "</root>%n"));
    }

    @Test
    void testXmlAlreadyAStringWithoutPrettyPrint(Snapshot snapshot) throws Exception {
        final SnapshotTestResult snapshotResult = snapshot
                .assertThat("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><node>text</node></root>")
                .as(XmlSnapshot.xml()
                        .withPrettyPrintStringXml(false))
                .matchesSnapshotStructure();

        assertThat(snapshotResult.serializedActual())
                .isEqualTo("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><node>text</node></root>");
    }

    @Test
    void testAsXmlNoRootObject(Snapshot snapshot) throws Exception {
        final PersonWithoutRootElement myself = determinePersonWithoutRootElement();
        final SnapshotTestResult snapshotResult = snapshot.assertThat(myself).as(xml).matchesSnapshotText();
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
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
    void testAsXmlStructureCompareBuilder(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson();
        final SnapshotTestResult snapshotResult = snapshot.assertThat(myself)
                .as(XmlSnapshot
                        .xml()
                        .compareUsing(xmls -> xmls.withDifferenceEvaluator(DifferenceEvaluators.Default).areSimilar()))
                .matchesSnapshotStructure();
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
    }

    @Test
    void testAsXmlStructureCustomStructuralAssertions(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson();
        final SnapshotTestResult snapshotResult = snapshot.assertThat(myself)
                .as(XmlSnapshot.xml().compareUsing(CompareAssert::areSimilar))
                .matchesAccordingTo((expected, actual) -> XmlAssert.assertThat(actual).and(expected)
                        .withDifferenceEvaluator(DifferenceEvaluators.ignorePrologDifferences()).areSimilar());
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
    }

    @Test
    void testAsXmlStructureCompareCustomNew(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson().setName("0000-02-02");
        final SnapshotTestResult snapshotResult = snapshot.assertThat(myself)
                .as(XmlSnapshot.xml()
                        .withComparisonRules(rules -> rules
                                .pathAt("/person/address/city/text()").ignore()
                                .pathAt("/person/name/text()").mustMatch(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"))))
                .matchesSnapshotStructure();
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
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

    private PersonWithoutRootElement determinePersonWithoutRootElement() {
        return new PersonWithoutRootElement()
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

    @XmlRootElement
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

    public static class PersonWithoutRootElement {
        private String name;
        private String surname;
        private LocalDate birthdate;
        private Address address;

        public String getName() {
            return this.name;
        }

        public PersonWithoutRootElement setName(String name) {
            this.name = name;
            return this;
        }

        public String getSurname() {
            return this.surname;
        }

        public PersonWithoutRootElement setSurname(String surname) {
            this.surname = surname;
            return this;
        }

        public LocalDate getBirthdate() {
            return this.birthdate;
        }

        public PersonWithoutRootElement setBirthdate(LocalDate birthdate) {
            this.birthdate = birthdate;
            return this;
        }

        public Address getAddress() {
            return this.address;
        }

        public PersonWithoutRootElement setAddress(Address address) {
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
