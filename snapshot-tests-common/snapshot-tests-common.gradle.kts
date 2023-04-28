plugins {
    `published-java-component`
}

description = "Snapshot Tests Common Utilities"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.common")
}

dependencies {
    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}
