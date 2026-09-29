import de.skuzzle.buildlogic.CopyAndFilterReadmeTask

plugins {
    id("build-logic.base")
    id("build-logic.release-lifecycle")
}

val generateReadmeAndReleaseNotes = tasks.register<CopyAndFilterReadmeTask>("generateReadmeAndReleaseNotes") {
    group = "documentation"
    description = "Copies the readme and release notes file into the root directory, replacing all placeholders"
    sourceDir = project.projectDir
    targetDir = project.rootDir
    replaceTokens = mapOf(
        "project.version" to project.version as String,
        "project.groupId" to project.group as String,
        "version.junit" to libs.versions.junit5.get(),
        "version.junit4" to libs.versions.junit4.get(),
        "github.user" to providers.gradleProperty("githubUser"),
        "github.name" to providers.gradleProperty("githubRepo")
    )
}

tasks.prepareRelease.configure { dependsOn(generateReadmeAndReleaseNotes) }
