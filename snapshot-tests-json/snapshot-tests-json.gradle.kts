plugins {
    `published-java-component`
}

description = "Snapshot JSON Serialization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.data.json")
}

dependencies {
    api(projects.snapshotTestsCore)
    implementation(projects.snapshotTestsCommon)

    api(platform(libs.jackson.bom))
    api("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    api(libs.jsonassert)
    testImplementation(projects.snapshotTestsJunit5)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj.core)
}
