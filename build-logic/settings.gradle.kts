dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }

    repositories {
        gradlePluginPortal() // so that external plugins can be resolved in dependencies section
    }
}

rootProject.name="build-logic"

include("conventions")
include("release-plugin")
include("verify-publication")
include("code-style")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
