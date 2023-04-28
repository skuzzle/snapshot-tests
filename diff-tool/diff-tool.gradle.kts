plugins {
    `snapshot-tests.published-java-component`
}

description = "Diff Tool"
ext.automaticModuleName = "de.skuzzle.test.snapshots.difftool"

dependencies {
    api(libs.javadiffutils)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
