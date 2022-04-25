* Simple API to specify custom rules using XPath for XML comparison
* Change: Custom json matchers are only tested against the new actual result and not against the persisted snapshot anymore
* [#24](https://github.com/skuzzle/snapshot-tests/issues/24): Only create snapshot directory if necessary

### Experimental
* Fix: object traversal failed on primitive arrays
* Fix: serious bug in `ObjectMemberAction.replaceConsistently`
* Add: new simpler overload of `ObjectMemberAction.replaceConsistently`
* Add: new package for string normalization

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