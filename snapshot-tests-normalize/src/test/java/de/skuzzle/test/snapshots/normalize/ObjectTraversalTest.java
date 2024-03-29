package de.skuzzle.test.snapshots.normalize;

import static de.skuzzle.test.snapshots.normalize.ObjectMemberAction.members;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@EnableSnapshotTests
public class ObjectTraversalTest {

    private static Stream<Arguments> strategies() {
        return Stream.of(
                ObjectMembers.usingJavaBeansConventions(),
                ObjectMembers.fieldBased())
                .map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void testTraverseSingle(ObjectMembers strategy, Snapshot snapshot) throws Exception {
        final Person person = determinePerson();
        final String result = ObjectTraversal.members(person, strategy)
                .map(ObjectMember::toString)
                .collect(Collectors.joining("\n"));

        // intentionally use the same snapshot for all test arguments to make sure they
        // deliver identical results
        snapshot.named("traverseSingle").assertThat(result).asText().matchesSnapshotText();
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void testObjectMemberAction(ObjectMembers strategy, Snapshot snapshot) throws Exception {
        final Person person = determinePerson();
        ObjectTraversal.applyActions(person, strategy, ObjectMemberAction.members()
                .where(objMember -> objMember.collectionParent().isPresent())
                .removeFromParent());

        // intentionally use the same snapshot for all test arguments to make sure they
        // deliver identical results
        snapshot.named("testObjectMemberAction").assertThat(person).asText().matchesSnapshotText();
    }

    @ParameterizedTest
    @MethodSource("strategies")
    void testConsistentlyReplaceUuid(ObjectMembers strategy, Snapshot snapshot) throws Exception {
        final Person person = (Person) determinePerson().setUuid(UUID.randomUUID().toString());

        ObjectTraversal.applyActions(person, strategy, members()
                .withStringValueMatching("[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}")
                .consistentlyReplaceWith(Generators.deterministicUUID()));

        // intentionally use the same snapshot for all test arguments to make sure they
        // deliver identical results
        snapshot.named("testConsistentlyReplaceUuid").assertThat(person).asText().matchesSnapshotText();
    }

    private Person determinePerson() {
        return (Person) new Person()
                .setName("Simon")
                .setSurname("Taddiken")
                .setBirthdate(LocalDate.of(1777, 1, 12))
                .addAddress(new Address()
                        .setCity("Bielefeld")
                        .setCountry("Germany")
                        .setStreet("Gibtsnicht-Straße")
                        .setNumber("1337")
                        .setZipCode("4711"))
                .addAddress(new Address()
                        .setCity("London")
                        .setCountry("UK")
                        .setStreet("Whatever-Rd")
                        .setNumber("42")
                        .setZipCode("11833"))
                .setReadonly(true)
                .setId(4711)
                .addAttribute("height", new Attribute("175").addAdditionalValue("cm"))
                .addAttribute("weight", new Attribute("60").addAdditionalValue("tons"));
    }

    public static class Address {
        private String street;
        private String number;
        private String zipCode;
        private String city;
        private String country;

        public static String staticConst = "staticConst";

        public static String getStaticValue() {
            return "xyz";
        }

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

    public static class Person extends Entity {
        private String name;
        private String surname;
        private LocalDate birthdate;
        private final List<Address> addresses = new ArrayList<>();
        private final byte[] primitiveArray = new byte[10];
        private final String[] objectArray = new String[10];

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

        public List<Address> getAddresses() {
            return this.addresses;
        }

        public Person addAddress(Address address) {
            this.addresses.add(address);
            return this;
        }

        public byte[] getPrimitiveArray() {
            return this.primitiveArray;
        }

        public String[] getObjectArray() {
            return this.objectArray;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append(super.toString())
                    .append("Name: ").append(name).append("\n")
                    .append("Surname: ").append(surname).append("\n")
                    .append("Birthdate: ").append(birthdate).append("\n")
                    .append("Addresses: ").append(addresses).append("\n")
                    .append("PrimitiveArray: ").append(Arrays.toString(primitiveArray)).append("\n")
                    .append("ObjectArray: ").append(Arrays.toString(objectArray)).append("\n")
                    .toString();
        }
    }

    public static class Entity {
        private int id;
        private String uuid;
        private boolean readonly;
        private final Map<String, Attribute> attributes = new HashMap<>();

        public Entity setId(int id) {
            this.id = id;
            return this;
        }

        public int getId() {
            return this.id;
        }

        public Entity setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public String getUuid() {
            return this.uuid;
        }

        public boolean isReadonly() {
            return this.readonly;
        }

        public Entity setReadonly(boolean readonly) {
            this.readonly = readonly;
            return this;
        }

        public Entity addAttribute(String key, Attribute attribute) {
            this.attributes.put(key, attribute);
            return this;
        }

        public Map<String, Attribute> getAttributes() {
            return this.attributes;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("id: ").append(id).append("\n")
                    .append("uuid: ").append(uuid).append("\n")
                    .append("readonly: ").append(readonly).append("\n")
                    .append("attributes: ").append(attributes).append("\n")
                    .toString();
        }
    }

    static class Attribute {
        private String value;
        private final List<String> additionalValues = new ArrayList<>();

        public Attribute(String value) {
            this.value = value;
        }

        public Attribute setValue(String value) {
            this.value = value;
            return this;
        }

        public String getValue() {
            return this.value;
        }

        public Attribute addAdditionalValue(String additionalValue) {
            this.additionalValues.add(additionalValue);
            return this;
        }

        public List<String> getAdditionalValues() {
            return this.additionalValues;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("{value: ").append(value).append(", ")
                    .append("additionalValues: ").append(additionalValues).append("}")
                    .toString();
        }
    }
}
