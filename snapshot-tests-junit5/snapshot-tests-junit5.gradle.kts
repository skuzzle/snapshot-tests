plugins {
    `published-java-component`
}

description = "Snapshot Tests JUnit5 support"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.junit5")
}

dependencies {
    api(projects.snapshotTestsApi)
    implementation(projects.snapshotTestsCore)
    implementation(projects.snapshotTestsCommon)
    api(platform(libs.junit.bom))
    api("org.junit.jupiter:junit-jupiter")

    implementation(libs.apiguardian)

    testImplementation(libs.assertj.core)
    testImplementation(projects.snapshotTestsTestCommon)
}
