plugins {
    `published-java-component`
}

description = "Snapshot Tests Text Snapshot"

extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.data.text")
}

dependencies {
    implementation(projects.snapshotTestsApi)
    implementation(projects.snapshotTestsCommon)
    implementation(projects.diffTool)
    testImplementation(projects.snapshotTestsJunit5)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj.core)
}