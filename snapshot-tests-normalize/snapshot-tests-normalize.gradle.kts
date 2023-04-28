plugins {
    `published-java-component`
}

description = "Snapshot Object Normalization"
ext.automaticModuleName = "de.skuzzle.test.snapshots.normalize"

dependencies {
    implementation(projects.snapshotTestsCommon)
    testImplementation(projects.snapshotTestsJunit5)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
