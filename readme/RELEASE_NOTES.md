* [#42](https://github.com/skuzzle/snapshot-tests/issues/42): Publish flattened version of our artifact to fix BOM distribution
* [#43](https://github.com/skuzzle/snapshot-tests/issues/43): Allow XML structure comparison for String input
* Add `XmlSnapshot.withEnableXPathDebugging(...)` and `HtmlSnapshot.withEnableXPathDebugging(...)` to print results of applying custom comparison rules
* Fixed bugs and performance issues in XPath comparison engine
* `toString()` of `TestFile` and `TestDirectory` contain full absoulte path to the file instead of just the file name
* Deprecate `FilesFrom.directory` in favor of `FilesFrom.testResourcesDirectory` and `FilesFrom.projectDirectory`
* Deprecate `DirectoriesFrom.directory` in favor of `DirectoriesFrom.testResourcesDirectory` and `DirectoriesFrom.projectDirectory`


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

If you need xml based snapshots (includes `-core`):

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