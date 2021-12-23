[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=${project.version}&color=blue)](https://search.maven.org/artifact/${project.groupId}/${project.artifactId}/${project.version}/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=${project.version}&color=orange)](http://www.javadoc.io/doc/${project.groupId}/${project.artifactId}/${project.version})

* [#6](https://github.com/skuzzle/snapshot-tests/issues/6) Allow to specify explicit snapshot name
* [#7](https://github.com/skuzzle/snapshot-tests/issues/7) Write snapshot information to headers (**breaking**)

Maven Central coordinates for this release:

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>${project.artifactId}</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```