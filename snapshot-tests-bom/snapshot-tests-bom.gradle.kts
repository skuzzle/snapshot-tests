plugins {
    `java-platform`
    id("build-logic.publishing-conventions")
    id("build-logic.verify-publication")
}

description = "Manages all child artifact versions in case you need to reference multiple in a client project"

javaPlatform {
    allowDependencies()
}

val bomProjects = listOf(
    projects.snapshotTestsCore,
    projects.snapshotTestsJunit4,
    projects.snapshotTestsJunit5,
    projects.snapshotTestsHtml,
    projects.snapshotTestsJackson,
    projects.snapshotTestsJson,
    projects.snapshotTestsJaxb,
    projects.snapshotTestsXmlLegacy,
    projects.snapshotTestsXml,
    projects.snapshotTestsJaxbJakarta,
    projects.snapshotTestsNormalize,
    projects.snapshotTestsDirectoryParams,
    projects.diffTool
)

dependencies {
    constraints {
        bomProjects.forEach { api(it) }
    }
}

publishing.publications.named<MavenPublication>("maven") {
    from(components["javaPlatform"])
}

verifyPublication {
    expectPublishedArtifact(project.name) {
        withPomFileMatchingMavenCentralRequirements()
        withPomFileContentMatching("Should have packaging 'pom'") { content -> content.contains("<packaging>pom</packaging>") }
        bomProjects.map { it.name }.forEach { artifactId ->
            withPomFileContentMatching("Should manage $artifactId") { content ->
                content.contains("<artifactId>$artifactId</artifactId>")
            }
        }
    }
}
