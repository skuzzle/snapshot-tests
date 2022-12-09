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


_Note_: This release comes with a few major deprecations that are preparing our transition to the next major version 
that is 2.0. That version will likely see all those deprecated methods to be removed. In general, simple drop in 
replacements are provided and documented to ensure an easy migration.

Maven Central coordinates for this release:

## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-bom/${project.version}/jar)

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>${project.version}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("${project.groupId}:snapshot-tests-bom:${project.version}"))
```

## Artifacts
If you only need text based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-core/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-core/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-core</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-core:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-core:${project.version}")
```

If you need json based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-jackson/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-jackson/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-jackson:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-jackson:${project.version}")
```

If you need xml based snapshots using `javax.xml` legacy namespaces (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-jaxb/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-jaxb/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-jaxb:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-jaxb:${project.version}")
```

If you need xml based snapshots using new `jakarta.xml` namespaces (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-jaxb-jakarta/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-jaxb-jakarta/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-jaxb-jakarta</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-jaxb-jakarta:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-jaxb-jakarta:${project.version}")
```

If you need HTML based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-html/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-html/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-html:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-html:${project.version}")
```

## Experimental
Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-directory-params/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-directory-params/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-directory-params:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-directory-params:${project.version}")
```

Object normalization

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-normalize/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-normalize/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-normalize:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-normalize:${project.version}")
```