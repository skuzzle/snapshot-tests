
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

rootProject.name="plugins"

include("build-logic")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
