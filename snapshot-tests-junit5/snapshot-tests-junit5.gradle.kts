plugins {
    `published-java-component`
}

description = "Snapshot Tests JUnit5 support"
ext.automaticModuleName = "de.skuzzle.test.snapshots.junit5"

dependencies {
    api(projects.snapshotTestsApi)
    implementation(projects.snapshotTestsCore)
    implementation(projects.snapshotTestsCommon)
    api(platform(libs.junit.bom))
    api("org.junit.jupiter:junit-jupiter")
    testImplementation(projects.snapshotTestsTestCommon)

    implementation(libs.apiguardian)

    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
