plugins {
    id("build-logic.published-java-component")
}

description = "Snapshot HTML Serialization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.html")
}

dependencies {
    api(projects.snapshotTestsCore)
    api(projects.snapshotTestsXmlunit)
    implementation(projects.snapshotTestsCommon)
    api(libs.xmlunit.assertj)
    implementation(libs.jsoup)
}
