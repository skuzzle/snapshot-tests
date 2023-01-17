package de.skuzzle.test.snapshots;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.data.text.TextSnapshot;
import de.skuzzle.test.snapshots.data.text.TextSnapshot.DiffFormat;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class SnapshotsTest {

    @Test
    @SnapshotTestOptions(alwaysPersistActualResult = true, alwaysPersistRawResult = true)
    void testWriteContextFiles(Snapshot snapshot) throws Exception {
        final Person simon = determinePerson();
        final SnapshotTestResult testResult = snapshot.assertThat(simon).asText().matchesSnapshotText();

        assertThat(testResult.rawActualResultFile()).exists();
        assertThat(testResult.actualResultFile()).exists();
        assertThat(testResult.contextFiles().rawActualResultFile()).exists();
        assertThat(testResult.contextFiles().actualResultFile()).exists();
    }

    @Test
    void testDisabledWithNullInput(Snapshot snapshot) throws Exception {
        final SnapshotTestResult testResult = snapshot.assertThat(null).asText().disabled();
        assertThat(testResult.serializedActual()).isEqualTo("<<unavailable because actual was null>>");
    }

    @Test
    @SnapshotTestOptions(alwaysPersistActualResult = true, alwaysPersistRawResult = true)
    void testWithOneDisabledAssertionForWhichSnapshotHasNotYetBeenCreatedWithContextFiles(Snapshot snapshot)
            throws Exception {

        final Person simon = determinePerson();
        final SnapshotTestResult testResult = snapshot.assertThat(simon).asText().disabled();

        assertThat(testResult.rawActualResultFile()).exists();
        assertThat(testResult.actualResultFile()).exists();
        assertThat(testResult.contextFiles().rawActualResultFile()).exists();
        assertThat(testResult.contextFiles().actualResultFile()).exists();
    }

    @Test
    @SnapshotTestOptions(alwaysPersistActualResult = true, alwaysPersistRawResult = true)
    void testWithOneDisabledAssertionForWhichSnapshotHasHasAlreadyBeenCreatedWithContextFiles(Snapshot snapshot)
            throws Exception {

        final Person simon = determinePerson();
        final SnapshotTestResult testResult = snapshot.assertThat(simon).asText().disabled();

        assertThat(testResult.rawActualResultFile()).exists();
        assertThat(testResult.actualResultFile()).exists();
        assertThat(testResult.contextFiles().rawActualResultFile()).exists();
        assertThat(testResult.contextFiles().actualResultFile()).exists();
    }

    @Test
    void testWithOneDisabledAssertionForWhichSnapshotHasNotYetBeenCreated(Snapshot snapshot) throws Exception {
        final Person simon = determinePerson();
        final SnapshotTestResult testResultDisabled = snapshot.assertThat(simon).asText().disabledBecause("Reasons");

        assertThat(testResultDisabled.status()).isEqualTo(SnapshotStatus.DISABLED);
        assertThat(testResultDisabled.targetFile()).doesNotExist();
        assertThat(testResultDisabled.contextFiles().snapshotFile()).doesNotExist();

        final Person phil = determinePerson().setName("Phil");
        final SnapshotTestResult testResultActive = snapshot.assertThat(phil).asText().matchesSnapshotText();

        assertThat(testResultActive.status()).isEqualTo(SnapshotStatus.ASSERTED);
    }

    @Test
    void testWithOneDisabledAssertionForWhichSnapshotHasAlreadyBeenCreated(Snapshot snapshot) throws Exception {
        final Person simon = determinePerson();
        final SnapshotTestResult testResultDisabled = snapshot.assertThat(simon).asText().disabled();

        assertThat(testResultDisabled.status()).isEqualTo(SnapshotStatus.DISABLED);
        assertThat(testResultDisabled.targetFile()).exists();
        assertThat(testResultDisabled.contextFiles().snapshotFile()).exists();

        final Person phil = determinePerson().setName("Phil");
        snapshot.assertThat(phil).asText().matchesSnapshotText();
    }

    @Test
    void testMultipleSnapshotsInOneTestCase(Snapshot snapshot) throws Throwable {
        final Person simon = determinePerson();
        snapshot.assertThat(simon).asText().matchesSnapshotText();
        final Person phil = determinePerson().setName("Phil");
        snapshot.assertThat(phil).asText().matchesSnapshotText();
    }

    @Test
    void testWithExplicitSnapshotName(Snapshot snapshot) throws Exception {
        final Person simon = determinePerson();
        snapshot.named("simon").assertThat(simon).asText().matchesSnapshotText();
        final Person phil = determinePerson().setName("Phil");
        snapshot.named("phil").assertThat(phil).asText().matchesSnapshotText();
    }

    @Test
    void testMixExplicitAndAutomaticNaming(Snapshot snapshot) throws Exception {
        final Person simon = determinePerson();
        snapshot.named("simon").assertThat(simon).asText().matchesSnapshotText();
        final Person phil = determinePerson().setName("Phil");
        snapshot.assertThat(phil).asText().matchesSnapshotText();
    }

    @Test
    void testCustomizeTextSnapshot(Snapshot snapshot) throws Exception {
        final Person simon = determinePerson();

        snapshot.assertThat(simon).as(TextSnapshot.text()
                .withContextLines(10)
                .withDiffFormat(DiffFormat.SPLIT)
                .withIgnoreWhitespaces(true)).matchesSnapshotText();
    }

    @ParameterizedTest
    @ValueSource(strings = { "string1", "string2" })
    void testParameterized(String param, Snapshot snapshot) {
        snapshot.namedAccordingTo(SnapshotNaming.withParameters(param))
                .assertThat(param).asText().matchesSnapshotText();
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

}
