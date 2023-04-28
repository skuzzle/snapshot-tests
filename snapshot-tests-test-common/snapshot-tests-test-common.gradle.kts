plugins {
    id("snapshot-tests.published-java-component")
}
ext.automaticModuleName = "de.skuzzle.test.snapshots.testcommon"

dependencies {
    implementation(projects.snapshotTestsCommon)

    implementation( )platform(libs.junit.bom))
    implementation("org.junit.jupiter:junit-jupiter")
    api("org.junit.platform:junit-platform-engine")
    api("org.junit.platform:junit-platform-launcher")
    api("org.junit.platform:junit-platform-testkit")

    implementation(libs.apiguardian)

    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
