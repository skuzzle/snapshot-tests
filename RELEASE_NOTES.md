**Migration Info**:

> **Note**
> The following modules have been deprecated: `snapshot-tests-jackson`, `snapshot-tests-jaxb` and
> `snapshot-tests-jaxb-jakarta`. New drop-in replacements are provided via `snapshot-tests-json`,
> `snapshpt-tests-xml-legacy` and `snapshot-tests-xml`. (See also:
> [#67](https://github.com/skuzzle/snapshot-tests/issues/67))
>
> Important: These new modules come with slightly different Automatic-Module-Name. If you are using JPMS you need to
> adjust your `module-info.java` when upgrading.
>
> The deprecated modules will be removed with the next major version!

> **Note**
> When upgrading from a version prior to `1.8.0`, instead of depending on `snapshot-tests-core` you should now
> either depend on `snapshot-tests-junit5` or `snapshot-tests-junit4`.
> This will become mandatory with the next major version!


### Changes

* [#67](https://github.com/skuzzle/snapshot-tests/issues/67): Fix inconsistent naming of structured data modules
* [#60](https://github.com/skuzzle/snapshot-tests/issues/60): Internal API for plugging in test-framework specific behavior
* [#80](https://github.com/skuzzle/snapshot-tests/issues/80): Use spotless and reformat whole code base
* [#87](https://github.com/skuzzle/snapshot-tests/issues/87): Allow to configure diff format
* [#88](https://github.com/skuzzle/snapshot-tests/issues/88): Extract diff creation into own module
* [#44](https://github.com/skuzzle/snapshot-tests/issues/44)/[#90](https://github.com/skuzzle/snapshot-tests/issues/90): Add possibility to normalize line endings (according to local git config)

* Update to jsonassert `1.5.1` (coming from `1.5.0`)


* List [notable changes](https://skuzzle.github.io/snapshot-tests/reference/latest/#_notable_changes_in_this_release) since last release in reference documentation


## Reference Documentation

[![Reference](https://img.shields.io/static/v1?label=Reference&message=1.10.0&color=orange)](https://skuzzle.github.io/snapshot-tests/reference/1.10.0)

Reference documentation for this release: https://skuzzle.github.io/snapshot-tests/reference/1.10.0


## BOM Artifact
Manages the versions of all modules in case you are using multiple in your project

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-bom/1.10.0/jar)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-bom</artifactId>
    <version>1.10.0</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```

```
testImplementation(platform("de.skuzzle.test:snapshot-tests-bom:1.10.0"))
```

## Artifacts

### Choose a test framework

ℹ️ All options include support for plain text snapshots.

If you are using **JUnit5**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit5/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-junit5/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit5</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-junit5:1.10.0")
```

If you are using **JUnit4**:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-junit4/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-junit4/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-junit4</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-junit4:1.10.0")
```

### Choose a snapshot format (optional)
<details>
    <summary>Show supported snapshot format artifacts</summary>

If you want **JSON** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-json/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jackson/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-json</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-json:1.10.0")
```

If you want **XML** based snapshots using jaxb and legacy `javax.xml` namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-xml-legacy/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-xml-legacy</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-xml-legacy:1.10.0")
```

If you want **XML** based snapshots using jaxb new `jakarta.xml` namespaces:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-xml/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-jaxb-jakarta/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-xml</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-xml:1.10.0")
```

If you want **HTML** based snapshots:

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-html/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-html/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-html</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-html:1.10.0")
```
</details>

### Additional utilities

<details>
    <summary>Show utility artifacts</summary>

Directory Params

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-directory-params/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-directory-params/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-directory-params</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-directory-params:1.10.0")
```

Diff-Tool

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/diff-tool/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/diff-tool/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>diff-tool</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:diff-tool:1.10.0")
```

Object normalization (⚠️ Experimental⚠)

[![Maven Central](https://img.shields.io/static/v1?label=MavenCentral&message=1.10.0&color=blue)](https://search.maven.org/artifact/de.skuzzle.test/snapshot-tests-normalize/1.10.0/jar) [![JavaDoc](https://img.shields.io/static/v1?label=JavaDoc&message=1.10.0&color=orange)](http://www.javadoc.io/doc/de.skuzzle.test/snapshot-tests-normalize/1.10.0)

```xml
<dependency>
    <groupId>de.skuzzle.test</groupId>
    <artifactId>snapshot-tests-normalize</artifactId>
    <version>1.10.0</version>
    <scope>test</scope>
</dependency>
```

```
testImplementation("de.skuzzle.test:snapshot-tests-normalize:1.10.0")
```
</details>
