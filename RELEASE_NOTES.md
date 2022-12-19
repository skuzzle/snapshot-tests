* [#19](https://github.com/skuzzle/snapshot-tests/issues/19): Deprecate the whole `@EnabledSnapshotTests` annotation in favor of annotation with same name within `junit5` package
* [#30](https://github.com/skuzzle/snapshot-tests/issues/30): Deprecate `EnableSnapshotTests.softAssertions`. Soft assertions will no longer be supported in the next major version
* [#32](https://github.com/skuzzle/snapshot-tests/issues/32): Support for jakarta namespaces via new `snapshot-tests-jaxb-jakarta` module
* [#47](https://github.com/skuzzle/snapshot-tests/issues/47): Add new `SnapshotTestOptions` annotation which allows to configure some detailed aspects of the snapshot engine
* Deprecate `EnableSnapshotTests.snapshotDirectory` in favor of new annotation `@SnapshotDirectory`
* Deprecate `SnapshotTestResult.serializedSnapshot()` in favor of `SnapshotTestResult.snapshotFile()`
* Add `SnapshotTestResult.serializedActual()`
* Add `SnapshotTestResult.actualResultFile()`
* Add `SnapshotTestResult.rawActualResultFile()`
* Add the possibility to configure the number of context lines printed around a change in the default unified diff via `@SnapshotTestOptions.textDiffContextLines()`
* Add the possibility to always persist the latest actual result as a sibling file of the real `.snapshot` file via `@SnapshotTestOptions.alwaysPersistActualResult()`
* Add the possibility to additionally persist the raw actual result in a sibling file of the real `.snapshot` file via `@SnapshotTestOptions.alwaysPersistRawResult()`. The raw result does not contain the snapshot header information.
* Improve compatibility with JUnit5's `@Nested` tests (general support for `@Nested` is still experimental though)
* Unified diffs within assertion failure messages now come with full line information


_Note_: This release comes with a few major deprecations that are preparing our transition to the next major version 
that is 2.0. That version will likely see all those deprecated methods to be removed. In general, simple drop in 
replacements are provided and documented to ensure an easy migration.

Maven Central coordinates for this release:

## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.7.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.7.0/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.7.0</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.7.0"))
```

## Artifacts
If you only need text based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.7.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-core/1.7.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.7.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-core/1.7.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-core</artifactId>
    <version>1.7.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-core:1.7.0'
testImplementation("de.skuzzle.test:snapshot-tests-core:1.7.0")
```

If you need json based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.7.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.7.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.7.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.7.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.7.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jackson:1.7.0'
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.7.0")
```

If you need xml based snapshots using `javax.xml` legacy namespaces (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.7.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.7.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.7.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.7.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.7.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb:1.7.0'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.7.0")
```

If you need xml based snapshots using new `jakarta.xml` namespaces (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.7.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.7.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.7.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.7.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb-jakarta</artifactId>
    <version>1.7.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb-jakarta:1.7.0'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb-jakarta:1.7.0")
```

If you need HTML based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.7.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-html/1.7.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.7.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-html/1.7.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>1.7.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-html:1.7.0'
testImplementation("de.skuzzle.test:snapshot-tests-html:1.7.0")
```

## Experimental
Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.7.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.7.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.7.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.7.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.7.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-directory-params:1.7.0'
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.7.0")
```

Object normalization

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.7.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.7.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.7.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.7.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.7.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-normalize:1.7.0'
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.7.0")
```