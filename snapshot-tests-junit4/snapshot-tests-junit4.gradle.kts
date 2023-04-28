plugins {
    `published-java-component`
}

description = "Snapshot Tests JUnit4 support"
ext.automaticModuleName = "de.skuzzle.test.snapshots.junit4"

dependencies {
    api(projects.snapshotTestsApi)
    implementation(projects.snapshotTestsCore)
    implementation(projects.snapshotTestsCommon)
    testImplementation(projects.snapshotTestsTestCommon)
    api(libs.junit4)

    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
