plugins {
    id("build-logic.published-java-component")
}

description = "Snapshot JSON Serialization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.data.json")
}

dependencies {
    api(projects.snapshotTestsJackson)
}
