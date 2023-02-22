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
* [#71](https://github.com/skuzzle/snapshot-tests/issues/71): Gracefully support JUnit5 nested tests
* Add recursive directory scanning via `DirectoriesFrom.recursive()`
* Deprecate `PathFilter` in favor of `TestFileFilter` and `TestDirectoryFilter`
* Improve format and information density of XPath debugging (can be enabled with `XmlSnapshot.withEnableXPathDebugging(true)`)
* Improve removal of internal stack frames from stacktraces of assertion errors
* `SnapshotException` is now an unchecked exception
* DSL no longer throws checked exceptions
* Update to jackson `2.14.2` (coming from `2.10.5`)

## Reference Documentation

[![Reference](https://img.shields.io/static/v1?label=Reference&message=@project.version@&color=orange)](https://@github.user@.github.io/@github.name@/reference/@project.version@)

Reference documentation for this release: https://@github.user@.github.io/@github.name@/reference/@project.version@


## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-bom/@project.version@/jar)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>@project.version@</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("@project.groupId@:snapshot-tests-bom:@project.version@"))
```

## Artifacts

### Choose a test framework

ℹ️ All options include support for plain text snapshots.

If you are using **JUnit5**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-junit5/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-junit5/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-junit5</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-junit5:@project.version@")
```

If you are using **JUnit4**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-junit4/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-junit4/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-junit4</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-junit4:@project.version@")
```

### Choose a snapshot format (optional)
<details>
    <summary>Show supported snapshot format artifacts</summary>
    
If you want **JSON** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-jackson/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-jackson/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-jackson</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-jackson:@project.version@")
```

If you want **XML** based snapshots using jaxb and `javax.xml` legacy namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-jaxb/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-jaxb/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-jaxb</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-jaxb:@project.version@")
```

If you want **XML** based snapshots using jaxb new `jakarta.xml` namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-jaxb-jakarta/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-jaxb-jakarta/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-jaxb-jakarta</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-jaxb-jakarta:@project.version@")
```

If you want **HTML** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-html/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-html/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-html:@project.version@")
```
</details>

### Additional utilities

<details>
    <summary>Show utility artifacts</summary>

Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-directory-params/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-directory-params/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-directory-params:@project.version@")
```

Object normalization (⚠️ Experimental⚠)

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-normalize/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-normalize/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-normalize:@project.version@")
```
</details>