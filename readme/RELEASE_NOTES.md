**Migration Info**:

> **Warning**
> This is a new major version which comes with breaking changes.
> Please read the notable changes section in the reference documentation for details about upgrading from `1.x` versions.


### Changes

* [#17](https://github.com/skuzzle/snapshot-tests/issues/17): Remove JUnit 5 dependency from core package
* [#62](https://github.com/skuzzle/snapshot-tests/issues/62): Detect when a snapshot is overridden in same test execution
* [#91](https://github.com/skuzzle/snapshot-tests/issues/91): Normalize to `git` line endings by default
* [#98](https://github.com/skuzzle/snapshot-tests/issues/98): Fix `Automatic-Module-Name` of `-html` module
* Remove deprecated modules (see reference docs for details)
* Remove deprecated methods (see reference docs for details)
* Deprecate `ContextFiles.delete()` in favor of `.deleteAll()` and `.deleteContextFiles()`


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

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-json/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-jackson/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-json</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-json:@project.version@")
```

If you want **XML** based snapshots using jaxb and legacy `javax.xml` namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-xml-legacy/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-jaxb/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-xml-legacy</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-xml-legacy:@project.version@")
```

If you want **XML** based snapshots using jaxb new `jakarta.xml` namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/snapshot-tests-xml/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/snapshot-tests-jaxb-jakarta/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>snapshot-tests-xml</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:snapshot-tests-xml:@project.version@")
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

Diff-Tool

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=@project.version@&color=blue)](https://search.maven.org/artifact/@project.groupId@/diff-tool/@project.version@/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=@project.version@&color=orange)](http://www.javadoc.io/doc/@project.groupId@/diff-tool/@project.version@)

```xml
<dependency>
    <groupId>@project.groupId@</groupId>
    <artifactId>diff-tool</artifactId>
    <version>@project.version@</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("@project.groupId@:diff-tool:@project.version@")
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
