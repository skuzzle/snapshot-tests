* [#23](https://github.com/skuzzle/snapshot-tests/issues/33): Allow to configure strictness of JSON comparison
* [#34](https://github.com/skuzzle/snapshot-tests/issues/34): Support for HTML snapshots
* [#36](https://github.com/skuzzle/snapshot-tests/issues/36): Throw proper `AssertionError` if actual snapshot input is null
* [#37](https://github.com/skuzzle/snapshot-tests/issues/37): Improve rendering of huge diffs by leaving out large unchanged parts
* [#39](https://github.com/skuzzle/snapshot-tests/issues/39): Detect incomplete/illegal DSL usages
* [#40](https://github.com/skuzzle/snapshot-tests/issues/40): Allow to gracefully disable snapshot assertions
* Build against JUnit 5.9.1 (coming from 5.8.2)
* Clean up dependencies


Maven Central coordinates for this release:

## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.5.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.5.0/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.5.0</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.5.0"))
```

## Artifacts
If you only need text based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.5.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-core/1.5.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.5.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-core/1.5.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-core</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-core:1.5.0'
testImplementation("de.skuzzle.test:snapshot-tests-core:1.5.0")
```

If you need json based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.5.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.5.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.5.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.5.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jackson:1.5.0'
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.5.0")
```

If you need xml based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.5.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.5.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.5.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.5.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb:1.5.0'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.5.0")
```

If you need HTML based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.5.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-html/1.5.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.5.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-html/1.5.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-html:1.5.0'
testImplementation("de.skuzzle.test:snapshot-tests-html:1.5.0")
```

## Experimental
Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.5.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.5.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.5.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.5.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-directory-params:1.5.0'
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.5.0")
```

Object normalization

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.5.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.5.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.5.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.5.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-normalize:1.5.0'
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.5.0")
```