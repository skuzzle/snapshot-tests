plugins {
    `published-java-component`
}
description = "Snapshot XML Serialization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.data.xml")
}

dependencies {
    api(projects.snapshotTestsCore)
    api(projects.snapshotTestsXmlunit)
    implementation(projects.snapshotTestsCommon)

    api(libs.jaxb.api.legacy)
    implementation(libs.jaxb.runtime.legacy)
    implementation(libs.xmlunit.core)
    api(libs.xmlunit.assertj)
    testImplementation(projects.snapshotTestsJunit5)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj.core)
}