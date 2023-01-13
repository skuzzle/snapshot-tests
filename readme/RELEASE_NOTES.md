⚠️ℹ️ **Migration Info**: 

When upgrading from a version prior to `1.8.0`, instead of depending on `snapshot-tests-core` you should depend on either 
`snapshot-tests-junit5` or `snapshot-tests-junit4`. This will become mandatory with the next major version!

**All Changes**: 

* [#52](https://github.com/skuzzle/snapshot-tests/issues/52): Test methods that contain at least on `disabled` assertion and no failed assertions will properly be marked as 'skipped' by the test framework
* [#54](https://github.com/skuzzle/snapshot-tests/pull/54): Add support for JUnit4
* Move JUnit5 support into separate module
* Build against JUnit `5.9.2` (coming from `5.9.1`)
* Add `ContextFiles` class which groups the paths to all generated files
* Deprecate `SnapshotTestResult.targetFile()`, `SnapshotTestResult.actualResultFile()` and  `SnapshotTestResult.rawActualResultFile()` in favor of `SnapshotTestResult.contextFiles()`
* Add `disabledBecause(String)` terminal DSL operation. The string can be used to leave an informative message to readers so they know why the assertion is disabled
* Improve formatting of orphaned snapshot warning
* Streamline internal implementation


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

### Choose a test framework

ℹ️ All options include support for plain text snapshots.

If you are using **JUnit5**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-junit5/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-junit5/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-junit5</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-junit5:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-junit5:${project.version}")
```

If you are using **JUnit4**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/snapshot-tests-junit4/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/snapshot-tests-junit4/${project.version})

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>snapshot-tests-junit4</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation '${project.groupId}:snapshot-tests-junit4:${project.version}'
testImplementation("${project.groupId}:snapshot-tests-junit4:${project.version}")
```

### Choose a snapshot format (optional)
<details>
    <summary>Show supported snapshot format artifacts</summary>
    
If you want **JSON** based snapshots:

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

If you want **XML** based snapshots using jaxb and `javax.xml` legacy namespaces:

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

If you want **XML** based snapshots using jaxb new `jakarta.xml` namespaces:

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

If you want **HTML** based snapshots:

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
</details>

### Additional utilities

<details>
    <summary>Show utility artifacts</summary>

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

Object normalization (⚠️ Experimental⚠)

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
</details>