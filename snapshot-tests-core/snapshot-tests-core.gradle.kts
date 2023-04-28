plugins {
    `published-java-component`
}

description = "Snapshot Tests Core"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.core")
}

dependencies {
    api(projects.snapshotTestsApi)
    api(projects.snapshotTestsText)
    implementation(projects.snapshotTestsCommon)
    implementation(projects.diffTool)
    testImplementation(projects.snapshotTestsTestCommon)
    testImplementation(projects.snapshotTestsJunit5)

    implementation(libs.opentest4j)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
