plugins {
    `published-java-component`
}
description = "Snapshot XML Serialization"
ext.automaticModuleName = "de.skuzzle.test.snapshots.data.xml"

dependencies {
    api(projects.snapshotTestsCore)
    api(projects.snapshotTestsXmlunit)
    implementation(projects.snapshotTestsCommon)

    api(libs.jaxb.api.jakarta)
    implementation(libs.jaxb.runtime.jakarta)
    implementation(libs.xmlunit.jakarta.jaxb.impl)
    implementation(libs.xmlunit.core)
    api(libs.xmlunit.assertj)
    testImplementation(projects.snapshotTestsJunit5)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
