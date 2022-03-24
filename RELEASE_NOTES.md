_Note:_ When running your tests with this version for the first time, you will see that your snapshot files are being 
changed because we introduced a new header field. You can and safely check in these changes into your SCM.

* Improve orphan detection to report less false positives
* Multiple JavaDoc improvements
* Improve and simplify internal interface and test framework integration

Maven Central coordinates for this release:

## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.2.2/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.2.2</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.2.2"))
```

## Artifacts
If you only need text based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-core/1.2.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-core/1.2.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-core</artifactId>
    <version>1.2.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-core:1.2.2'
testImplementation("de.skuzzle.test:snapshot-tests-core:1.2.2")
```

If you need json based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.2.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.2.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.2.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jackson:1.2.2'
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.2.2")
```

If you need xml based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.2.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.2.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.2.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb:1.2.2'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.2.2")
```

## Experimental
Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.2.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.2.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.2.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-directory-params:1.2.2'
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.2.2")
```

Object normalization

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.2.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.2.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.2.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.2.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.2.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-normalize:1.2.2'
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.2.2")
```