import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("snapshot-tests.base-conventions")
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
            val libs = project.rootProject.extensions.getByType(VersionCatalogsExtension.class).named("libs")
            filter(ReplaceTokens, tokens: [
                    "project.version": project.version,
                    "project.groupId": project.group,
                    "version.junit"  : libs.findLibrary("junit-bom").get().get().getVersion(),
                    "version.junit4" : libs.findLibrary("junit4").get().get().getVersion(),
                    "github.user"    : githubUser,
                    "github.name"    : githubRepo
            ])
        }
    }
}
