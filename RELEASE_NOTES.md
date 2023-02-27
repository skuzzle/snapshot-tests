**Migration Info**: 

> **Note**
> When upgrading from a version prior to `1.8.0`, instead of depending on `snapshot-tests-core` you should now 
> either depend on `snapshot-tests-junit5` or `snapshot-tests-junit4`. 
> This will become mandatory with the next major version!
> 
> Check the resp. section in the README!

### Fixes

* [#76](https://github.com/skuzzle/snapshot-tests/issues/76): Improved lazy XPath rule configuration
* [#77](https://github.com/skuzzle/snapshot-tests/issues/77): Small XPath performance optimization
* [#78](https://github.com/skuzzle/snapshot-tests/issues/78): Throw AssertionError only once
* [#79](https://github.com/skuzzle/snapshot-tests/issues/79): Filter out internal stack frames from all AssertionErrors

### Build
[#75](https://github.com/skuzzle/snapshot-tests/issues/75): Use `Project.copy` instead of `Copy` task

## Reference Documentation

[![Reference](https://img.shields.io/static/v1?label=Reference&message=1.9.2&color=orange)](https://skuzzle.github.io/snapshot-tests/reference/1.9.2)

Reference documentation for this release: https://skuzzle.github.io/snapshot-tests/reference/1.9.2


## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.9.2/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.9.2</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.9.2"))
```

## Artifacts

### Choose a test framework

ℹ️ All options include support for plain text snapshots.

If you are using **JUnit5**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit5/1.9.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-junit5/1.9.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit5</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-junit5:1.9.2")
```

If you are using **JUnit4**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit4/1.9.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-junit4/1.9.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit4</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-junit4:1.9.2")
```

### Choose a snapshot format (optional)
<details>
    <summary>Show supported snapshot format artifacts</summary>
    
If you want **JSON** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.9.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.9.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.9.2")
```

If you want **XML** based snapshots using jaxb and `javax.xml` legacy namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.9.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.9.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.9.2")
```

If you want **XML** based snapshots using jaxb new `jakarta.xml` namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.9.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.9.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb-jakarta</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-jaxb-jakarta:1.9.2")
```

If you want **HTML** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-html/1.9.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-html/1.9.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-html:1.9.2")
```
</details>

### Additional utilities

<details>
    <summary>Show utility artifacts</summary>

Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.9.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.9.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.9.2")
```

Object normalization (⚠️ Experimental⚠)

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.2&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.9.2/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.2&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.9.2)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.9.2</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.9.2")
```
</details>