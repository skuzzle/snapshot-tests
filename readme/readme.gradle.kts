import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `base-conventions`
}

// Don't use 'Copy' Task here, see https://github.com/skuzzle/snapshot-tests/issues/75
tasks.register("generateReadmeAndReleaseNotes") {
    group = "documentation"
    description = "Copies the readme and release notes file into the root directory, replacing all placeholders"

    doLast {
        copy {
            from(project.projectDir) {
                include("*.md")
            }
            into(project.rootDir)
            filter(ReplaceTokens::class, "tokens" to mapOf(
                "project.version" to project.version as String,
                "project.groupId" to project.group as String,
                "version.junit" to project.requiredVersionFromLibs("junit5"),
                "version.junit4" to project.requiredVersionFromLibs("junit4"),
                "github.user" to project.property("githubUser") as String,
                "github.name" to project.property("githubRepo") as String
            ))
        }
    }
}
