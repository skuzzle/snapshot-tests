plugins {
    id("build-logic.published-java-component")
}

description = "Snapshot XML Serialization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.data.xml")
}

dependencies {
    api(projects.snapshotTestsJaxbJakarta)
}
