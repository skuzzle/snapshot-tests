<!-- This file is auto generated during release from readme/README.md -->

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.skuzzle.test/snapshot-tests/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.skuzzle.test/snapshot-tests)
[![JavaDoc](http://javadoc-badge.appspot.com/de.skuzzle.test/snapshot-tests.svg?label=JavaDoc)](http://javadoc-badge.appspot.com/de.skuzzle.test/snapshot-tests)
[![Coverage Status](https://coveralls.io/repos/github/skuzzle/snapshot-tests/badge.svg?branch=main)](https://coveralls.io/github/skuzzle/snapshot-tests?branch=main)
[![Twitter Follow](https://img.shields.io/twitter/follow/skuzzleOSS.svg?style=social)](https://twitter.com/skuzzleOSS)

# snapshot-tests
Convenient snapshot testing for JUnit5.

This library allows to conveniently assert on the structure and contents of complex objects. It does so by storing a 
serialized version of the object during the first test execution and during subsequent test executions, compares the
actual object against the stored snapshot.

## Quick start
Annotate your test class with `@SnapshotAssertions` and declare a `Snapshot` parameter in your test case:

```java
@SnapshotAssertions
class ComplexTest {

    private WhatEverService classUnderTest = ...;

    @Test
    void testCreateComplexObject(Snapshot snapshot) throws Exception {
        ComplexObject actual = classUnderTest.createComplexObject();
        snapshot.assertThat(actual).asJson().matchesSnapshotStructure();
    }
}
```
During first test execution, this will create a file named `testCreateComplexObject_0.snapshot` somewhere below 
`src/test/resources` containing the json serialized object.
On every subsequent execution, the json representation of the `actual` object will be compared against the contents of
that file.

## Compatibility
- [x] Requires Java 11
- [x] Tested against Spring-Boot `2.2.13.RELEASE, 2.3.12.RELEASE, 2.4.10, 2.5.4`

## Usage

### Defining the serialized format
Snapshots can be serialized into any format. By default, this library ships with serializers for json 
(relying on the jackson object mapping framework) and xm (relying on jaxb). You can also provide your own 
`SnapshotSerializer`.

```java
@Test
void testSnapshotXml(Snapshot snapshot) throws Exception {
    ComplexObject actual = ...
    snapshot.assertThat(actual).asJson().matchesSnapshotStructure();
}

@Test
void testSnapshotToString(Snapshot snapshot) throws Exception {
    ComplexObject actual = ...
    // When providing a custom SnapshotSerializer, you have to either use the generic 'matchesSnapshotText' assertions
    // or also provide a custom StructuralAssertions implementation (see further examples)
    snapshot.assertThat(actual).as(Object::toString).matchesSnapshotText();
}
```

### Structural assertions
Once serialized, the library uses `StructuralAssertions` to compare two serialized objects. By default, we user 
`xml-unit` for comparing xmls and `jsonassert` for comparing jsons. Generic text comparison is implemented using the 
awesome `diff_match_patch` class from Neil Fraser.
When using a custom `SnapshotSerializer` you can also supply a custom `StructuralAssertions` implementation to implement
comparisons specific to your serialization format.

## Changelog

### Version 0.0.1
* Initial

<details>
  <summary><b>Previous releases</b></summary>
  None

</details>
