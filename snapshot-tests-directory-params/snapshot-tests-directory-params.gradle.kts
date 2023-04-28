plugins {
    `published-java-component`
}

description = "Directory Contents Parameters"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.directoryparams")
}

dependencies {
    implementation(projects.snapshotTestsCommon)
    testImplementation(projects.snapshotTestsCore)
    testImplementation(projects.snapshotTestsJunit5)

    testImplementation(platform(libs.junit.bom))
    implementation("org.junit.platform:junit-platform-commons")
    implementation("org.junit.jupiter:junit-jupiter-params")
    implementation("org.junit.jupiter:junit-jupiter")

    implementation(libs.apiguardian)

    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
