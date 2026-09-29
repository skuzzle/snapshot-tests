plugins {
    // NOTE: plugin application order is relevant here!
    id("build-logic.publishing-conventions")
    id("build-logic.java-component")
    id("build-logic.verify-publication")
}

publishing.publications.named<MavenPublication>("maven") {
    from(components["java"])
}

verifyPublication {
    expectPublishedArtifact(project.name) {
        withClassifiers("", "javadoc", "sources")
        withPomFileMatchingMavenCentralRequirements()
        withJarContaining {
            aFile("META-INF/MANIFEST.MF") {
                matching("Should declare an Automatic-Module-Name") { content ->
                    content.contains("Automatic-Module-Name: ")
                }
            }
        }
    }
}
