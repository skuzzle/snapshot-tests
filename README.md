<!-- This file is auto generated during release from readme/README.md -->

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.9.0-SNAPSHOT/jar)
[![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-core/1.9.0-SNAPSHOT)
[![Reference](https://img.shields.io/static/v1?label=Reference&message=1.9.0-SNAPSHOT&color=orange)](https://skuzzle.github.io/snapshot-tests/reference/1.9.0-SNAPSHOT)
[![Coverage Status](https://coveralls.io/repos/github/skuzzle/snapshot-tests/badge.svg?branch=main)](https://coveralls.io/github/skuzzle/snapshot-tests?branch=main)
[![Twitter Follow](https://img.shields.io/twitter/follow/ProjectPolly.svg?style=social)](https://twitter.com/ProjectPolly)

# snapshot-tests
Convenient snapshot testing for JUnit5 and JUnit4. 

This library allows to conveniently assert on the structure and contents of complex objects. It does so by storing a 
serialized version of the object during the first test execution and during subsequent test executions, compare the
actual object against the stored snapshot.

- [x] Requires Java 11, supports Java 17

Supported test frameworks:
- [x] JUnit5  (tested against `5.9.2`) via [snapshot-tests-junit5](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit5/1.9.0-SNAPSHOT/jar)
- [x] JUnit4 (tested against `4.13.2`) via [snapshot-tests-junit4](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit4/1.9.0-SNAPSHOT/jar)

Supported snapshot formats:
- [x] generic plain text (included by default via [snapshot-tests-core](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-core/1.9.0-SNAPSHOT/jar))
- [x] Json via [snapshot-tests-jackson](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.9.0-SNAPSHOT/jar)
- [x] XML via [snapshot-tests-jaxb](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.9.0-SNAPSHOT/jar) xor [snapshot-tests-jaxb-jakarta](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.9.0-SNAPSHOT/jar)
- [x] HTML via [snapshot-tests-html](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-html/1.9.0-SNAPSHOT/jar)

Read more about snapshot testing in this accompanying [blog post](https://simon.taddiken.net/the-case-for-snapshot-testing/).

### Latest Maven Central coordinates

Please check out the GitHub release page to find Maven & Gradle coordinates for the latest 
release [1.9.0-SNAPSHOT](https://github.com/skuzzle/snapshot-tests/releases/tag/v1.9.0-SNAPSHOT)

### Reference Documentation
Starting with release `1.8.0` we provide a new external reference documentation:
* [Latest](https://skuzzle.github.io/snapshot-tests/reference/latest): Always points to the latest version
* [1.9.0-SNAPSHOT](https://skuzzle.github.io/snapshot-tests/reference/1.9.0-SNAPSHOT): Points to a specific version

Over the course of the next releases most of the contents of this README will be transitioned into the new reference 
documentation.

## Quick start
_(assumes using `maven`, `JUnit5` and `snapshot-tests-jackson` artifact)_

Add the following dependencies to your build

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit5</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

Annotate a test class with `@EnableSnapshotTests` and declare a `Snapshot` parameter in your test case:

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


## Notes on test framework support

### JUnit5
Historically, JUnit5 is the preferred test framework and has always natively been supported. The preferred way of 
configuring the build is to add a dependency to `snapshot-tests-junit5` and optionally add a dependency for your 
preferred snapshot format (i.e. like `snapshot-tests-jackson`).

### JUnit5 legacy
The `snapshot-tests-junit5` module has been introduced with version `1.8.0`. Prior to that, you would either add a 
direct dependency to `snapshot-tests-core` or just use a single dependency to you preferred snapshot format which 
would pull in the `-core` module transitively. This setup still works but is discouraged. You will see a warning being 
printed to `System.err` stating the required migration steps.

> **Warning**
> Starting from version `2.0.0` this scenario will no longer be supported.

### JUnit4
JUnit4 support was introduced with version `1.8.0`. Add a dependency to  `snapshot-tests-junit4` and optionally 
add a dependency for your preferred snapshot format like `snapshot-tests-jackson`.

> **Warning**
> In order to seamlessly support the JUnit5 legacy scenario described above, all snapshot format modules will still 
> transitively pull in a JUnit5 dependency. Unfortunately this can only be fixed with the next major release. So long you 
> might want to add a respective exclusion to your dependency:

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

or

```
testImplementation('de.skuzzle.test:snapshot-tests-jackson:1.9.0-SNAPSHOT') {
    exclude group: 'org.junit.jupiter', module: 'junit-jupiter-api'
}
```

## Usage
> NOTE: Most parts of this readme have been migrated to the new [reference documentation](#reference-documentation)
> and thus were already removed from the readme.

### Configuring some more details
**New**
Since version `1.7.0` there is a new `@SnapshotTestOptions` annotation that can either be placed on a test method or 
test class. It allows to configure some details of the snapshot testing engine.

#### Showing more context in unified diffs
Using `@SnapshotTestOptions.textDiffContextLines()` you can advise the framework to print more lines surrounding a
detected change in the unified diffs. Per default, we will only print 5 lines around a change.


#### Line number behavior in diffs
By default, line numbers in the diffs that are rendered in our assertion failure messages reflect the physical line 
number within the snapshot file. That number differs from the line number within the raw test result data because 
snapshot files contain some header information at the beginning.
If you want line numbers in the diffs to reflect the number within the raw data, you can use

```java
@SnapshotTestOptions(renderLineNumbers = DiffLineNumbers.ACCORDING_TO_RAW_DATA)
```