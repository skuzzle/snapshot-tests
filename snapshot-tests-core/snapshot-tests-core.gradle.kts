plugins {
    id("build-logic.published-java-component")
}

description = "Snapshot Tests Core"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.core")
}

dependencies {
    implementation(projects.snapshotTestsCommon)
    implementation(projects.diffTool)
    testImplementation(projects.snapshotTestsTestCommon)

    api(platform(libs.junit.bom))
    api("org.junit.jupiter:junit-jupiter")

    implementation(libs.javadiffutils)
    implementation(libs.opentest4j)
}
