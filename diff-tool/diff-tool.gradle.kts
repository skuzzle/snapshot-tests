plugins {
    `published-java-component`
}

description = "Diff Tool"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.difftool")
}

dependencies {
    api(libs.javadiffutils)

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.assertj.core)
}
