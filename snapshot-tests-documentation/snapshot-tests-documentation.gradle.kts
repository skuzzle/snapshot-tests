plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    `java-conventions`
}

description = "Snapshot Tests Documentation"

dependencies {
    testImplementation(projects.snapshotTestsJunit5)
    testImplementation(projects.snapshotTestsJson)
    testImplementation(projects.snapshotTestsXmlLegacy)
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation(libs.apiguardian)

    testImplementation(platform(libs.junit.bom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.equalsverifier)
    testImplementation(libs.assertj.core)
}

tasks.asciidoctor {
    sources {
        include("**/index.adoc")
    }

    attributes(mapOf(
        "revnumber" to project.version.toString(),
        "groupId" to project.group,
        "source-highlighter" to "coderay",
        "toc" to "left",
        "icons" to "font",
        "setanchors" to "true",
        "docinfo1" to "true",
        "testIncludes" to sourceSets["test"].java.srcDirs.first()
    ))
}
val repositoryDeployPathLatest = project.rootProject.file("docs/reference/latest")
val repositoryDeployPathCurrent = project.rootProject.file("docs/reference/" + project.version)

tasks.register("deployDocsToRepositoryRoot") {
    group = "release"
    description = "Generates AsciiDoc documentation and copies it to the respective folders in the project root"
    dependsOn("deployDocsToRepositoryRootLatest", "deployDocsToRepositoryRootCurrent")
}

// Don't use 'Delete' Task here, see https://github.com/skuzzle/snapshot-tests/issues/75
tasks.register("wipeLatestDocsFolder") {
    description = "Wipes out the 'latest' documentation folder. To be used before copying the updated contents"
    doLast {
        delete(repositoryDeployPathLatest)
    }
}

// Don't use 'Copy' Task here, see https://github.com/skuzzle/snapshot-tests/issues/75
tasks.register("deployDocsToRepositoryRootLatest") {
    dependsOn("asciidoctor", "wipeLatestDocsFolder")
    doLast {
        copy {
            from(asciidoctor.get().outputs.files)
            into(repositoryDeployPathLatest)
        }
    }
}

// Don't use 'Copy' Task here, see https://github.com/skuzzle/snapshot-tests/issues/75
tasks.register("deployDocsToRepositoryRootCurrent") {
    dependsOn("asciidoctor")
    doLast {
        copy {
            from(asciidoctor.get().outputs.files)
            into(repositoryDeployPathCurrent)
        }
    }
}
