* Detect whether a test method had been renamed and update a snapshot's header information (works in some cases. 
  In other cases, a new snapshot might be taken and the existing one becomse an orphan).
* Fix and improve orphan detection (especially when using custom snapshot names and/or custom snapshot directories)

Maven Central coordinates for this release:

## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.1-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.2.1-SNAPSHOT/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.2.1-SNAPSHOT</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.2.1-SNAPSHOT"))
```

## Artifacts
If you only need text based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.1-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-core/1.2.1-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.1-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-core/1.2.1-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-core</artifactId>
    <version>1.2.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-core:1.2.1-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-core:1.2.1-SNAPSHOT")
```

If you need json based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.1-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.2.1-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.1-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.2.1-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.2.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jackson:1.2.1-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.2.1-SNAPSHOT")
```

If you need xml based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.1-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.2.1-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.1-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.2.1-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.2.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb:1.2.1-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.2.1-SNAPSHOT")
```

## Experimental
Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.1-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.2.1-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.1-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.2.1-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.2.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-directory-params:1.2.1-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.2.1-SNAPSHOT")
```

Object normalization

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.1-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.2.1-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.1-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.2.1-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.2.1-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-normalize:1.2.1-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.2.1-SNAPSHOT")
```