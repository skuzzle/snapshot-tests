pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("build-logic.settings-conventions")
}

rootProject.name = "snapshot-tests"

include("snapshot-tests-core")
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

// deprecated data module
include("snapshot-tests-jaxb")
include("snapshot-tests-jaxb-jakarta")
include("snapshot-tests-jackson")

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
