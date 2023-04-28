plugins {
    `published-java-component`
}

description = "Snapshot Tests Public API"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.api")
}

dependencies {
    implementation(projects.snapshotTestsCommon)

    implementation(libs.opentest4j)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
