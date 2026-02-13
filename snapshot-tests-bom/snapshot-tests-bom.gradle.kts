plugins {
    `java-platform`
    `publishing-conventions`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        val bomProjects: List<Project> by rootProject.extra
        bomProjects.sorted()
            .map { "${it.group}:${it.name}:${it.version}" }
            .forEach { api(it) }
    }
}

publishing.publications.named<MavenPublication>("maven") {
    from(components["javaPlatform"])
    pom {
        description.set("Manages all child artifact versions in case you need to reference multiple in a client project")
    }
}
