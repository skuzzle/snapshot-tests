* [#25](https://github.com/skuzzle/snapshot-tests/issues/25): Improve exception messages for comparison failures
* [#27](https://github.com/skuzzle/snapshot-tests/issues/27): Allow to customize whitespace behavior for text comparison
* [#28](https://github.com/skuzzle/snapshot-tests/issues/28): Unify static constructors of all `StructuredDataProviders` (see _Deprecations_ below)

### Deprecations
* Deprecate `XmlSnapshot.inferJaxbContext()` in favor of `XmlSnapshot.xml()`
* Deprecate `XmlSnapshot.with(JAXBContext)` in favor of `XmlSnapshot.xml().withJAXBContext(JAXBContext)`
* Deprecate `JsonSnapshot.withDefaultObjectMapper()` in favor of `JsonSnapshot.json()`
* Deprecate `JsonSnapshot.withObjectMapper(ObjectMapper)` in favor of `JsonSnapshot.json(ObjectMapper)`

Maven Central coordinates for this release:

## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.4.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.4.0/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.4.0</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.4.0"))
```

## Artifacts
If you only need text based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.4.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-core/1.4.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.4.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-core/1.4.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-core</artifactId>
    <version>1.4.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-core:1.4.0'
testImplementation("de.skuzzle.test:snapshot-tests-core:1.4.0")
```

If you need json based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.4.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.4.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.4.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.4.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.4.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jackson:1.4.0'
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.4.0")
```

If you need xml based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.4.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.4.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.4.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.4.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.4.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb:1.4.0'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.4.0")
```

## Experimental
Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.4.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.4.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.4.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.4.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.4.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-directory-params:1.4.0'
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.4.0")
```

Object normalization

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.4.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.4.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.4.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.4.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.4.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-normalize:1.4.0'
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.4.0")
```