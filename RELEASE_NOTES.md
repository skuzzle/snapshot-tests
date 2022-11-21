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

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.6.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.6.0/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.6.0</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.6.0"))
```

## Artifacts
If you only need text based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.6.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-core/1.6.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.6.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-core/1.6.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-core</artifactId>
    <version>1.6.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-core:1.6.0'
testImplementation("de.skuzzle.test:snapshot-tests-core:1.6.0")
```

If you need json based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.6.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.6.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.6.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.6.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.6.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jackson:1.6.0'
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.6.0")
```

If you need xml based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.6.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.6.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.6.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.6.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.6.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb:1.6.0'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.6.0")
```

If you need HTML based snapshots (includes `-core`):

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.6.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-html/1.6.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.6.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-html/1.6.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>1.6.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-html:1.6.0'
testImplementation("de.skuzzle.test:snapshot-tests-html:1.6.0")
```

## Experimental
Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.6.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.6.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.6.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.6.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.6.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-directory-params:1.6.0'
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.6.0")
```

Object normalization

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.6.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.6.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.6.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.6.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.6.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-normalize:1.6.0'
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.6.0")
```