plugins {
    `published-java-component`
}

description = "Snapshot HTML Serialization"
ext.automaticModuleName = "de.skuzzle.test.snapshots.html"

dependencies {
    api(projects.snapshotTestsCore)
    api(projects.snapshotTestsXmlunit)
    implementation(projects.snapshotTestsCommon)
    testImplementation(projects.snapshotTestsJunit5)
    api(libs.xmlunit.assertj)
    implementation(libs.jsoup)
    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)

}
