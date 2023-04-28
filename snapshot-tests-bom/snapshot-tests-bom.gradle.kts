plugins {
    id("java-platform")
    id("snapshot-tests.publishing-conventions")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        bomProjects
                .collect({ it.dependencyProject })
                .collect({ "${it.group}:${it.name}:${it.version}" })
                .each { api(it) }
    }
}

publishing {
    publications.named("maven") {
        from(components.javaPlatform)
        pom {
            packaging("pom")
            description("Manages all child artifact versions in case you need to reference multiple in a client project")
        }
    }
}
