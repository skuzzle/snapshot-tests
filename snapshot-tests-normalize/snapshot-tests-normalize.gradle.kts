plugins {
    id("build-logic.published-java-component")
}

description = "Snapshot Object Normalization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.normalize")
}

dependencies {
    implementation(projects.snapshotTestsCommon)
    testImplementation(projects.snapshotTestsJunit5)
}
