<!-- This file is auto generated during release from readme/README.md -->

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/${project.groupId}/${project.artifactId}/badge.svg)](https://maven-badges.herokuapp.com/maven-central/${project.groupId}/${project.artifactId})
[![JavaDoc](http://javadoc-badge.appspot.com/${project.groupId}/${project.artifactId}.svg?label=JavaDoc)](http://javadoc-badge.appspot.com/${project.groupId}/${project.artifactId})
[![Coverage Status](https://coveralls.io/repos/github/skuzzle/${github.name}/badge.svg?branch=main)](https://coveralls.io/github/skuzzle/${github.name}?branch=main)
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
- [x] Requires Java ${version.java}
- [x] Tested against Spring-Boot `${version.spring-boot}, ${compatible-spring-boot-versions}`

## Usage

### Updating existing snapshots
There are basically two different approaches to updating persisted snapshots when the requirements for your 
implementation change:
1. Using a test driven approach, you can of course always modify the snapshots manually to reflect the new requirements
 before you change the actual code. This might be a bit tedious if you have a lot of affected snapshot files 
 (this is an anti-pattern on its own by the way).
2. If you are confident that you implemented the requirements correctly, you can advise the framework to update the 
persisted snapshots with the current test results. You can do so by setting the `updateSnapshots` attribute like so:
`@SnapshotAssertions(updateSnapshots = true)` 
**Warning** While `updateSnapshots` is set to true, all test cases containing snapshot assertions will fail. 


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
