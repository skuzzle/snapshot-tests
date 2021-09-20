* [#2](https://github.com/skuzzle/snapshot-tests/issues/2): Allow to access some snapshot information from within the test case.
* [#4](https://github.com/skuzzle/snapshot-tests/issues/4): Retain original stack trace on assertion failure
* Internal refactoring
* Don't rely on spring-boot dependency management anymore

Maven Central coordinates for this release:

```xml
<dependency>
    <groupId>${project.groupId}</groupId>
    <artifactId>${project.artifactId}</artifactId>
    <version>${project.version}</version>
    <scope>test</scope>
</dependency>
```