plugins {
    `published-java-component`
}

description = "Snapshot HTML Serialization"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.html")
}

dependencies {
    api(projects.snapshotTestsCore)
    api(projects.snapshotTestsXmlunit)
    implementation(projects.snapshotTestsCommon)
    api(libs.xmlunit.assertj)
    implementation(libs.jsoup)
    implementation(libs.apiguardian)

    testImplementation(projects.snapshotTestsJunit5)
    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj.core)

}
