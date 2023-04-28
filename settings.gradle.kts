pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        id("com.gradle.enterprise") version "3.13"
        id("com.gradle.common-custom-user-data-gradle-plugin") version "1.8.2"
        id("net.researchgate.release") version "3.0.2"
        id("io.github.gradle-nexus.publish-plugin") version "1.2.0"
        id("com.github.breadmoirai.github-release") version "2.4.1"
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
    }
}

plugins {
    id("com.gradle.enterprise")
    id("com.gradle.common-custom-user-data-gradle-plugin")
}

var isCi = System.getenv("CI")?.toBoolean() ?: false

gradleEnterprise {
    buildScan {
        publishAlways()
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        isUploadInBackground = !isCi
        capture {
            isTaskInputFiles = true
        }
    }
}

buildCache {
    local {
        isEnabled = true
    }
    remote(HttpBuildCache::class) {
        isEnabled = true
        url = uri("https://build-cache.taddiken.net/cache")
        if (isCi) {
            isPush = true
            credentials {
                username = System.getenv("BUILD_CACHE_USR")?.ifEmpty { null }
                password = System.getenv("BUILD_CACHE_PSW")?.ifEmpty { null }
            }
        }
    }
}

rootProject.name = "snapshot-tests"

include("snapshot-tests-api")
include("snapshot-tests-core")
include("snapshot-tests-text")
include("snapshot-tests-bom")

// Internal utilities
include("snapshot-tests-xmlunit")
include("snapshot-tests-common")
include("snapshot-tests-test-common")

// Test Framework support
include("snapshot-tests-junit5")
include("snapshot-tests-junit4")

// Supported data formats
include("snapshot-tests-html")
include("snapshot-tests-xml-legacy")
include("snapshot-tests-xml")
include("snapshot-tests-json")

// External utilities
include("snapshot-tests-directory-params")
include("snapshot-tests-normalize")
include("diff-tool")

// Docs and build support
include("snapshot-tests-documentation")
include("readme")
include("test-coverage")

// check that every subproject has a custom build file
// based on the project name
rootProject.children.forEach { project ->
    project.buildFileName = "${project.name}.gradle"
    if (!project.buildFile.isFile) {
        project.buildFileName = "${project.name}.gradle.kts"
    }
    require(project.buildFile.isFile) {
        "${project.buildFile} must exist"
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("xmlunit", "2.9.1")
            library("xmlunit-core", "org.xmlunit", "xmlunit-core").versionRef("xmlunit")
            library("xmlunit-assertj", "org.xmlunit", "xmlunit-assertj").versionRef("xmlunit")
            library("xmlunit-jakarta-jaxb-impl", "org.xmlunit", "xmlunit-jakarta-jaxb-impl").versionRef("xmlunit")

            version("jaxb-api-legacy", "2.3.1")
            version("jaxb-runtime-legacy", "2.3.3")
            library("jaxb-api-legacy", "javax.xml.bind", "jaxb-api").versionRef("jaxb-api-legacy")
            library("jaxb-runtime-legacy", "org.glassfish.jaxb", "jaxb-runtime").versionRef("jaxb-runtime-legacy")

            version("jaxb-api", "4.0.0")
            version("jaxb-runtime", "4.0.1")
            library("jaxb-api-jakarta", "jakarta.xml.bind", "jakarta.xml.bind-api").versionRef("jaxb-api")
            library("jaxb-runtime-jakarta", "org.glassfish.jaxb", "jaxb-runtime").versionRef("jaxb-runtime")

            version("junit5", "5.9.2")
            library("junit-bom", "org.junit", "junit-bom").versionRef("junit5")

            version("jackson", "2.14.2")
            library("jackson-bom", "com.fasterxml.jackson", "jackson-bom").versionRef("jackson")

            version("assertj", "3.24.2")
            library("assertj-core", "org.assertj", "assertj-core").versionRef("assertj")

            version("equalsverifier", "3.9")
            library("equalsverifier", "nl.jqno.equalsverifier", "equalsverifier").versionRef("equalsverifier")

            version("apiguardian", "1.1.2")
            library("apiguardian", "org.apiguardian", "apiguardian-api").versionRef("apiguardian")

            version("javadiffutils", "4.12")
            library("javadiffutils", "io.github.java-diff-utils", "java-diff-utils").versionRef("javadiffutils")

            version("bytebuddy", "1.12.22")
            library("bytebuddy", "net.bytebuddy", "byte-buddy").versionRef("bytebuddy")

            version("jsonassert", "1.5.1")
            library("jsonassert", "org.skyscreamer", "jsonassert").versionRef("jsonassert")

            version("opentest4j", "1.2.0")
            library("opentest4j", "org.opentest4j", "opentest4j").versionRef("opentest4j")

            version("jsoup", "1.15.3")
            library("jsoup", "org.jsoup", "jsoup").versionRef("jsoup")

            version("junit4", "4.13.2")
            library("junit4", "junit", "junit").versionRef("junit4")
        }
    }
}

