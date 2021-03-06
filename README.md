<!-- This file is auto generated during release from readme/README.md -->

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.3.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.3.0/jar)
[![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.3.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-core/1.3.0)
[![Coverage Status](https://coveralls.io/repos/github/skuzzle/snapshot-tests/badge.svg?branch=main)](https://coveralls.io/github/skuzzle/snapshot-tests?branch=main)
[![Twitter Follow](https://img.shields.io/twitter/follow/skuzzleOSS.svg?style=social)](https://twitter.com/skuzzleOSS)

# snapshot-tests
Convenient snapshot testing for JUnit5.

This library allows to conveniently assert on the structure and contents of complex objects. It does so by storing a 
serialized version of the object during the first test execution and during subsequent test executions, compare the
actual object against the stored snapshot.

Read more about snapshot testing in this accompanying [blog post](https://simon.taddiken.net/the-case-for-snapshot-testing/).

### Latest Maven Central coordinates

Please check out the GitHub release page to find Maven & Gradle coordinates for the latest 
release [1.3.0](https://github.com/skuzzle/snapshot-tests/releases/tag/v1.3.0)

## Quick start
_(assumes using `snapshot-tests-jackson` artifact)_

Annotate your test class with `@EnableSnapshotTests` and declare a `Snapshot` parameter in your test case:

```java
@EnableSnapshotTests
class ComplexTest {

    private WhatEverService classUnderTest = ...;

    @Test
    void testCreateComplexObject(Snapshot snapshot) throws Exception {
        ComplexObject actual = classUnderTest.createComplexObject();
        snapshot.assertThat(actual).as(JsonSnapshot.json).matchesSnapshotStructure();
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


## Compatibility
- [x] Requires Java 11
- [x] Tested against JUnit5 `5.8.2`

## Usage

### Updating existing snapshots
There are basically two different approaches to updating persisted snapshots when the requirements for your 
implementation change:
1. Using a test driven approach, you can of course always modify the snapshots manually to reflect the new requirements
 before you change the actual code. This might be a bit tedious if you have a lot of affected snapshot files.
2. If you are confident that you implemented the requirements correctly, you can advise the framework to update all the 
persisted snapshots with the current test results. You can do so by temporarily placing the `@ForceUpdateSnapshots` 
annotation on either your test class or your test method:

```java
@EnableSnapshotTests
@ForceUpdateSnapshots
class YourTestClass {...}
```

or

```java
@Test
@ForceUpdateSnapshots
void yourSnapshotTest(Snapshot snapshot) {...}
```

You can also update snapshots for individual assertions by replacing any of the `matchesSnapshot...` calls with 
`.justUpdateSnapshot()`:

```java
    snapshot.assertThat(actual).as(JsonSnapshot.json).justUpdateSnapshot();
```

**Warning** While updating snapshots, all test cases containing snapshot assertions will fail (for the 
same reason that they are failing the first time the snapshot is created: because no assertion has been 
performed during this run). 

It is also possible to pass the system property `forceUpdateSnapshots` (case-_insensitive_) to the JVM. When running 
the tests from maven it can be achieved like this:

```
mvn clean verify -DargLine=-DforceUpdateSnapshots
```


### Defining the serialized format
Snapshots can be serialized into any format. By default, this library ships with serializers for json 
(relying on the jackson object mapping framework) and xml (relying on jaxb). You can also provide your own 
`SnapshotSerializer`:

```java
@Test
void testSnapshotXml(Snapshot snapshot) throws Exception {
    ComplexObject actual = ...
    snapshot.assertThat(actual).as(XmlSnapshot.xml).matchesSnapshotStructure();
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

### Parameterized tests
_(since 1.1.0)_

Snapshot tests work well together with `@ParameterizedTest` but only if you take care of proper snapshot naming 
yourself like in this snippet:

```java
@ParameterizedTest
@ValueSource(strings = { "string1", "string2" })
void testParameterized(String param, Snapshot snapshot) {

    snapshot.namedAccordingTo(SnapshotNaming.withParameters(param))
            .assertThat(param).asText().matchesSnapshotText();
}
```
This will make each parameter's value part of the generated snapshot's file name. 

Otherwise, when using the default naming strategy, the framework would choose the same snapshot name for every 
parameterized execution (this could actually be desirable if you want to test that your code produces the exact same 
result for different parameters).

Check out the `SnapshotNaming` interface for more options regarding snapshot naming.

### Dealing with random values
A common source of problems are random values within the snapshot data such as dates or generated ids. Generally, you 
should design your code up front so that such randomness can easily be mocked away. For example:
* Instead of using `LocalDateTime.now()` make your code use a shared `Clock` instance that is replacible in tests and 
use `LocalDateTime.now(clock)`
* More generally put: If your code uses random values in any place, consider to use a strategy interface instead which 
can be replaced with a deterministic mock during testing.
* As a last resort, you can implement some normalization. Either post-process your actual test result before taking the
 snapshot or implement a `SnapshotSerializer` which does the normalization. You could also implement 
 `StructralAssertions` in a way that it ignores such random values during comparison.
 
**New**
The latest version of this library comes with a very simple (and experimental) abstraction for customizing the 
structural comparison. You can use json-path resp. xpath expressions to customize the comparison on a per-node basis.

XML example:

```java
snapshot.assertThat(someObjext)
        .as(XmlSnapshot.inferJaxbContext()
                .withComparisonRules(rules -> rules
                        .pathAt("/person/address/city/text()").ignore()
                        .pathAt("/person/date/text()").mustMatch(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"))))
        .matchesSnapshotStructure()
```

JSON example:

```java
snapshot.assertThat(someObjext)
        .as(JsonSnapshot.withDefaultObjectMapper()
                .withComparisonRules(rules -> rules
                        .pathAt("address.city").ignore()
                        .pathAt("date").mustMatch(Pattern.compile("\\d{4}-\\d{2}-\\d{2}"))))
        .matchesSnapshotStructure();
```
 

### Changing the snapshot directory
By default, snapshots are stored in a directory structure according to their test-class's package name relative to 
`src/test/resources`. You can change the relative path using 

```java
@EnableSnapshotTests(snapshotDirectory = "snapshots")
```
Currently it is not possible to use a directory outside `src/test/resources`.

Take care when reusing the same directory for multiple test classes. If they also by coincidence contain equally named 
test methods, snapshots might get overridden unintentionally.

### Orphaned snapshot files
Snapshot files can become orphans if, for example you rename a test class/method or you change the snapshot assertions 
within a test. This framework comes with a sophisticated approach for detecting those orphaned files. By default, we 
will log a warning with the found orphan. You can temporarily place the `@DeleteOrphanedSnapshots` annotation on a 
snapshot test class to have those files deleted automatically.

**Warning:** Deleting orphans should be handled with care. There might be raw occasions where we falsely detect a 
snapshot file as orphan (especially if you are running only parts of your test suite or have disabled tests).