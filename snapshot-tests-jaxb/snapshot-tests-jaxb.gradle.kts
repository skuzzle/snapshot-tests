plugins {
    id("build-logic.published-java-component")
}

description = "Snapshot XML Serialization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.xml.jaxb")
}

dependencies {
    api(projects.snapshotTestsCore)
    api(projects.snapshotTestsXmlunit)
    implementation(projects.snapshotTestsCommon)

    api(libs.jaxb.api.legacy)
    implementation(libs.jaxb.runtime.legacy)
    implementation(libs.xmlunit.core)
    api(libs.xmlunit.assertj)
}
