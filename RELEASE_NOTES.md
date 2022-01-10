[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=0.0.6&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-parent/0.0.6/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=0.0.6&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-parent/0.0.6)

* [#11](https://github.com/skuzzle/snapshot-tests/issues/11) Rename `@SnapshotAssertions` to `@EnableSnapshotTests` (**breaking**)

Maven Central coordinates for this release:

If you only need text based snapshots:
```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-core</artifactId>
    <version>0.0.6</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-core:0.0.6'
```

If you need json based snapshots (includes `-core`):
```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>0.0.6</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jackson:0.0.6'
```

If you need xml based snapshots (includes `-core`):
```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>0.0.6</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb:0.0.6'
```