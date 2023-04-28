plugins {
    id("snapshot-tests.published-java-component")
}
ext.automaticModuleName = "de.skuzzle.test.snapshots.xmlunit"

dependencies {
    api(projects.snapshotTestsCore)
    implementation(projects.snapshotTestsCommon)

    implementation(libs.xmlunit.core)
    implementation(libs.xmlunit.assertj)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
