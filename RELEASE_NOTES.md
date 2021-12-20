[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=0.0.4&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests/0.0.4/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=0.0.4&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests/0.0.4)

* Remove `throws` clauses from assert methods
* Add `asText()` as shortcut for `as(Object::toString)`
* Fix NPE in case of mixing snapshot and non-snapshot tests

Maven Central coordinates for this release:

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests</artifactId>
    <version>0.0.4</version>
    <scope>test</scope>
</dependency>
```