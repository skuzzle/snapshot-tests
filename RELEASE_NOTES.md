**Migration Info**: 

> **Note**
> When upgrading from a version prior to `1.8.0`, instead of depending on `snapshot-tests-core` you should now 
> either depend on `snapshot-tests-junit5` or `snapshot-tests-junit4`. 
> This will become mandatory with the next major version!
> 
> Check the resp. section in the README!

**All Changes**: 

* [#49](https://github.com/skuzzle/snapshot-tests/issues/49): Delete context files as well when deleting orphaned snapshots
* [#57](https://github.com/skuzzle/snapshot-tests/issues/57): Support XML namespaces in XPaths when using custom comparison rules
* [#61](https://github.com/skuzzle/snapshot-tests/issues/61): `@SnapshotDirectory` is not allowed anymore on test methods, only on test class
* [#64](https://github.com/skuzzle/snapshot-tests/issues/64): Migrate build to gradle
* [#66](https://github.com/skuzzle/snapshot-tests/issues/66): XmlSnapshot.withEnableXPathDebugging: Boolean parameter 'enabled' not taken into account
* Add recursive directory scanning via `DirectoriesFrom.recursive()`
* Deprecate `PathFilter` in favor of `TestFileFilter` and `TestDirectoryFilter`
* Improve format and information density of XPath debugging (can be enabled with `XmlSnapshot.withEnableXPathDebugging(true)`)
* Improve removal of internal stack frames from stacktraces of assertion errors
* `SnapshotException` is now an unchecked exception
* DSL no longer throws checked exceptions
* Update to jackson `2.14.2` (coming from `2.10.5`)

## Reference Documentation

[![Reference](https://img.shields.io/static/v1?label=Reference&message=1.9.0-SNAPSHOT&color=orange)](https://skuzzle.github.io/snapshot-tests/reference/1.9.0-SNAPSHOT)

Reference documentation for this release: https://skuzzle.github.io/snapshot-tests/reference/1.9.0-SNAPSHOT


## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.9.0-SNAPSHOT/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.9.0-SNAPSHOT"))
```

## Artifacts

### Choose a test framework

â„¹ï¸? All options include support for plain text snapshots.

If you are using **JUnit5**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit5/1.9.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-junit5/1.9.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit5</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-junit5:1.9.0-SNAPSHOT")
```

If you are using **JUnit4**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit4/1.9.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-junit4/1.9.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit4</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-junit4:1.9.0-SNAPSHOT")
```

### Choose a snapshot format (optional)
<details>
    <summary>Show supported snapshot format artifacts</summary>
    
If you want **JSON** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jackson/1.9.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.9.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-jackson:1.9.0-SNAPSHOT")
```

If you want **XML** based snapshots using jaxb and `javax.xml` legacy namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb/1.9.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.9.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-jaxb:1.9.0-SNAPSHOT")
```

If you want **XML** based snapshots using jaxb new `jakarta.xml` namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.9.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.9.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-jaxb-jakarta</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-jaxb-jakarta:1.9.0-SNAPSHOT")
```

If you want **HTML** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-html/1.9.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-html/1.9.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-html:1.9.0-SNAPSHOT")
```
</details>

### Additional utilities

<details>
    <summary>Show utility artifacts</summary>

Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.9.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.9.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.9.0-SNAPSHOT")
```

Object normalization (âš ï¸? Experimentalâš )

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.9.0-SNAPSHOT&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.9.0-SNAPSHOT/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.9.0-SNAPSHOT&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.9.0-SNAPSHOT)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.9.0-SNAPSHOT</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.9.0-SNAPSHOT")
```
</details>