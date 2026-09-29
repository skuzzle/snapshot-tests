plugins {
    id("build-logic.published-java-component")
}

description = "Snapshot XML Serialization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.xml.jaxbjakarta")
}

dependencies {
    api(projects.snapshotTestsCore)
    api(projects.snapshotTestsXmlunit)
    implementation(projects.snapshotTestsCommon)

    api(libs.jaxb.api.jakarta)
    implementation(libs.jaxb.runtime.jakarta)
    implementation(libs.xmlunit.jakarta.jaxb.impl)
    implementation(libs.xmlunit.core)
    api(libs.xmlunit.assertj)
}
