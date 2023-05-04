plugins {
    id("net.researchgate.release")
    id("io.github.gradle-nexus.publish-plugin")
    id("com.github.breadmoirai.github-release")
    `base-conventions`
}

tasks.named("afterReleaseBuild").configure {
    dependsOn(provider {
        subprojects
            .filter { it.pluginManager.hasPlugin("snapshot-tests.publishing-conventions") }
            .map { it.tasks.named("publishToSonatype") }
    })
    dependsOn("addFilesToGit")
}

tasks.register("addFilesToGit") {
    onlyIf("not a snapshot version") { !project.isSnapshot }
    dependsOn()
    group = "release"
    description = "Commit changed/generated files during release"
    doLast {
        // NOTE: .execute() extension function defined in buildSrc
        "git add README.md RELEASE_NOTES.md".execute()
        "git add --force docs/*".execute()
    }
}

release {
    pushReleaseVersionBranch.set("main")
    tagTemplate.set("v$version")
    git {
        requireBranch.set("dev")
    }
}

githubRelease {
    token(provider { property("ghToken") as String? })
    owner.set(property("githubUser").toString())
    repo.set(property("githubRepo").toString())
    draft.set(true)
    body(provider { file("RELEASE_NOTES.md").readText(Charsets.UTF_8) })
}

nexusPublishing.repositories {
    sonatype {
        username.set(property("sonatype_USR").toString())
        password.set(property("sonatype_PSW").toString())
    }
}

val bomProjects by extra(listOf(
    projects.snapshotTestsApi,
    projects.snapshotTestsText,
    projects.snapshotTestsCore,
    projects.snapshotTestsJunit4,
    projects.snapshotTestsJunit5,
    projects.snapshotTestsHtml,
    projects.snapshotTestsJson,
    projects.snapshotTestsXmlLegacy,
    projects.snapshotTestsXml,
    projects.snapshotTestsNormalize,
    projects.snapshotTestsDirectoryParams,
    projects.diffTool
).map { it.dependencyProject }
)
