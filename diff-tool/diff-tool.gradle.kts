plugins {
    id("build-logic.published-java-component")
}

description = "Diff Tool"
extra.apply {
    set("automaticModuleName", "de.skuzzle.test.snapshots.difftool")
}

dependencies {
    api(libs.javadiffutils)
}
