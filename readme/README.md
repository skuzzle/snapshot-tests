<!-- This file is auto generated during release from readme/README.md -->

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/${project.groupId}/${project.artifactId}/badge.svg)](https://maven-badges.herokuapp.com/maven-central/${project.groupId}/${project.artifactId})
[![JavaDoc](http://javadoc-badge.appspot.com/${project.groupId}/${project.artifactId}.svg?label=JavaDoc)](http://javadoc-badge.appspot.com/${project.groupId}/${project.artifactId})
[![Coverage Status](https://coveralls.io/repos/github/skuzzle/${github.name}/badge.svg?branch=main)](https://coveralls.io/github/skuzzle/${github.name}?branch=main)
[![Twitter Follow](https://img.shields.io/twitter/follow/skuzzleOSS.svg?style=social)](https://twitter.com/skuzzleOSS)

# snapshot-tests
Convenient snapshot testing for JUnit5.

This library allows to conveniently assert on the structure and contents of complex objects. It does so by storing a 
serialized version of the object during the first test execution and during subsequent test executions, compare the
actual object against the stored snapshot.

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>${project.artifactId}</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

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
Snapshot testing workflow:
1. Implement your test cases and add one ore more snapshot assertions as shown above.
2. When you now execute these tests the first time, serialized snapshots of your test results will be persisted 
**and the tests will fail**
3. Execute the same tests again. Now, the framework will compare the test results against the persisted snapshots. 
If your code under test produces deterministic results, tests should now be green
4. Check in the persisted snapshots into your SCM

## Rationale


## Compatibility
- [x] Requires Java ${version.java}
- [x] Tested against Spring-Boot `${version.spring-boot}, ${compatible-spring-boot-versions}`

## Usage

### Updating existing snapshots
There are basically two different approaches to updating persisted snapshots when the requirements for your 
implementation change:
1. Using a test driven approach, you can of course always modify the snapshots manually to reflect the new requirements
 before you change the actual code. This might be a bit tedious if you have a lot of affected snapshot files.
2. If you are confident that you implemented the requirements correctly, you can advise the framework to update all the 
persisted snapshots with the current test results. You can do so by setting the `updateSnapshots` attribute like so:

```java
@SnapshotAssertions(forceUpdateSnapshots = true)
```

You can also update snapshots for individual assertions by replacing any of the `matchesSnapshot...` calls with 
`.justUpdateSnapshot()`:

```java
    snapshot.assertThat(actual).asJson().justUpdateSnapshot();
```

**Warning** While updating snapshots, all test cases containing snapshot assertions will fail (for the 
same reason that they are failing the first time the snapshot is created: because no assertion has been 
performed during this run). 



### Defining the serialized format
Snapshots can be serialized into any format. By default, this library ships with serializers for json 
(relying on the jackson object mapping framework) and xml (relying on jaxb). You can also provide your own 
`SnapshotSerializer`:

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
Once serialized, the library uses `StructuralAssertions` to compare two serialized objects. By default, we use 
`xml-unit` for comparing xmls and `jsonassert` for comparing jsons. Generic text comparison is implemented using the 
awesome `diff_match_patch` class from Neil Fraser.
When using a custom `SnapshotSerializer` you can also supply a custom `StructuralAssertions` implementation to implement
comparisons specific to your serialization format.

### Multiple snapshots in same test case
You can create multiple snapshots using `snapshot.assertThat(...)` from within a single test case. The framework will
assign each snapshot a consecutive number.

### Dealing with random values
A common source of problems are random values within the snapshoted data such as dates or generated ids. This framework
comes with no means to resolve those issues. Instead you should design your code so that such randomness can easily be 
mocked away. For example:
* Instead of using `LocalDateTime.now()` make your code use a shared `Clock` instance that is replacible in tests and 
use `LocalDateTime.now(clock)`
* More generally put: If your code uses random values in any place, consider to use a strategy interface instead which 
can be replaced with a deterministic mock during testing.

### Changing the snapshot directory
By default, snapshots are stored in a directory structure according to their test-class's package name relative to 
`src/main/resources`. You can change the relative path using 

```java
@SnapshotAssertions(snapshotDirectory = "snapshots")
```
Currently it is not possible to use a directory outside `src/main/resources`.

Take care when reusing the same directory for multiple test classes. If they also by coincidence contain equally named 
test methods, snapshots might get overridden unintentionally.

## Changelog

## Version 0.0.2
* [#2](https://github.com/skuzzle/snapshot-tests/issues/2): Allow to access some snapshot information from within the test case.
* [#4](https://github.com/skuzzle/snapshot-tests/issues/4): Retain original stack trace on assertion failure
* Internal refactoring
* Don't rely on spring-boot dependency management anymore

<details>
  <summary><b>Previous releases</b></summary>
  
### Version 0.0.1
* Initial


  None

</details>
