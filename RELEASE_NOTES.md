**Migration Info**: 

> **Note**
> When upgrading from a version prior to `1.8.0`, instead of depending on `snapshot-tests-core` you should now 
> either depend on `snapshot-tests-junit5` or `snapshot-tests-junit4`. 
> This will become mandatory with the next major version!
> 
> Check the resp. section in the README!

**All Changes**: 

* [#52](https://github.com/skuzzle/snapshot-tests/issues/52): Test methods that contain at least on `disabled` assertion and no failed assertions will properly be marked as 'skipped' by the test framework
* [#54](https://github.com/skuzzle/snapshot-tests/pull/54): Add support for JUnit4
* Move JUnit5 support into separate module
* Build against JUnit `5.9.2` (coming from `5.9.1`)
* Add `ContextFiles` class which groups the paths to all generated files
* Deprecate `SnapshotTestResult.targetFile()`, `SnapshotTestResult.actualResultFile()` and  `SnapshotTestResult.rawActualResultFile()` in favor of `SnapshotTestResult.contextFiles()`
* Add `disabledBecause(String)` terminal DSL operation. The string can be used to leave an informative message to readers so they know why the assertion is disabled
* Improve formatting of orphaned snapshot warning
* Streamline internal implementation
* Add new reference documentation at 

## Reference Documentation

[![Reference](https://img.shields.io/static/v1?label=Reference&message=1.8.0-SNAPSHOT&color=orange)](https://skuzzle.github.io/snapshot-tests/docs/1.8.0-SNAPSHOT)

Reference documentation for this release: https://skuzzle.github.io/snapshot-tests/docs/1.8.0-SNAPSHOT


## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.8.0-SNAPSHOT/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.8.0-SNAPSHOT"))
```

## Artifacts

### Choose a test framework

ℹ️ All options include support for plain text snapshots.

If you are using **JUnit5**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit5/1.8.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.8.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-junit5/1.8.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit5</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-junit5:1.8.0-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-junit5:1.8.0-SNAPSHOT")
```

If you are using **JUnit4**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit4/1.8.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.8.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-junit4/1.8.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit4</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-junit4:1.8.0-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-junit4:1.8.0-SNAPSHOT")
```

### Choose a snapshot format (optional)
<details>
    <summary>Show supported snapshot format artifacts</summary>
    
If you want **JSON** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.8.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.8.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.8.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jackson:1.8.0-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.8.0-SNAPSHOT")
```

If you want **XML** based snapshots using jaxb and `javax.xml` legacy namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.8.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.8.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.8.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb:1.8.0-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.8.0-SNAPSHOT")
```

If you want **XML** based snapshots using jaxb new `jakarta.xml` namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.8.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.8.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.8.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb-jakarta</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-jaxb-jakarta:1.8.0-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-jaxb-jakarta:1.8.0-SNAPSHOT")
```

If you want **HTML** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-html/1.8.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.8.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-html/1.8.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-html:1.8.0-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-html:1.8.0-SNAPSHOT")
```
</details>

### Additional utilities

<details>
    <summary>Show utility artifacts</summary>

Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.8.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.8.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.8.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-directory-params:1.8.0-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.8.0-SNAPSHOT")
```

Object normalization (⚠️ Experimental⚠)

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.8.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.8.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.8.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.8.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.8.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation 'de.skuzzle.test:snapshot-tests-normalize:1.8.0-SNAPSHOT'
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.8.0-SNAPSHOT")
```
</details>