plugins {
    id("build-logic.published-java-component")
}

description = "Directory Contents Parameters"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.directoryparams")
}

dependencies {
    implementation(projects.snapshotTestsCommon)
    testImplementation(projects.snapshotTestsCore)

    implementation(platform(libs.junit.bom))
    implementation("org.junit.platform:junit-platform-commons")
    implementation("org.junit.jupiter:junit-jupiter-params")
    implementation("org.junit.jupiter:junit-jupiter")
}
