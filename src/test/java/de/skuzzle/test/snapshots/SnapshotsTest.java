package de.skuzzle.test.snapshots;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

@SnapshotAssertions(forceUpdateSnapshots = false)
public class SnapshotsTest {

    @Test
    void testAsJsonTextCompare(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson();
        snapshot.assertThat(myself).asJson().matchesSnapshotText();
    }

    @Test
    void testAsJsonStructureCompare(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson();
        final SnapshotResult snapshotResult = snapshot.assertThat(myself).asJson().matchesSnapshotStructure();
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
    }

    @Test
    void testAsXmlTextCompare(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson();
        final SnapshotResult snapshotResult = snapshot.assertThat(myself).asXml().matchesSnapshotText();
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
    }

    @Test
    void testAsXmlStructureCompare(Snapshot snapshot) throws Exception {
        final Person myself = determinePerson();
        final SnapshotResult snapshotResult = snapshot.assertThat(myself).asXml().matchesSnapshotStructure();
        assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
    }

    @Test
    void testMultipleSnapshotsInOneTestCase(Snapshot snapshot) throws Throwable {
        final Person simon = determinePerson();
        snapshot.assertThat(simon).asXml().matchesSnapshotStructure();
        final Person phil = determinePerson().setName("Phil");
        snapshot.assertThat(phil).asXml().matchesSnapshotStructure();
    }

    @Test
    void testWithExplicitSnapshotName(Snapshot snapshot) throws Exception {
        final Person simon = determinePerson();
        snapshot.named("simon").assertThat(simon).asXml().matchesSnapshotStructure();
        final Person phil = determinePerson().setName("Phil");
        snapshot.named("phil").assertThat(phil).asXml().matchesSnapshotStructure();
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

}
