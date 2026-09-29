import java.util.*

plugins {
    id("build-logic.base")
    id("build-logic.release-lifecycle")
    id("maven-publish")
    id("signing")
}

tasks.withType<Jar>().configureEach {
    manifest {
        // Intentionally no "Created-By" attribute — a value derived from
        // System.getProperty("java.version"/"java.vendor"/"java.vm.version") varies
        // across JVMs and would cause cache misses for any downstream tasks that
        // fingerprint the JAR's manifest.
        attributes(
            "Automatic-Module-Name" to provider {
                requireNotNull(project.findProperty("automaticModuleName")) {
                    "${project.path} must set the 'automaticModuleName' extra property"
                }
            },
            "Specification-Title" to project.name,
            "Specification-Version" to (project.version as String).substringBefore('-'),
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version
        )
    }
}

tasks.withType<Sign>().configureEach {
    onlyIf("Run on Jenkins, but not GitHub") { project.isJenkins }
}

signing {
    // The gpg key is injected by jenkins as a base64 encoded string. That is because jenkins doesn't support
    // storing secret text credentials with newlines. Thus, we need to decode the base64 string before we can sign
    val signingKey: String? = base64Decode(providers.gradleProperty("base64EncodedAsciiArmoredSigningKey").orNull)
    val signingPassword: String? = providers.gradleProperty("signingPassword").orNull
    useInMemoryPgpKeys(signingKey, signingPassword)
}

fun base64Decode(encodedString: String?): String? {
    if (!encodedString.isNullOrEmpty()) {
        try {
            val decoded = Base64.getDecoder().decode(encodedString)
            return String(decoded)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return null;
}

publishing {
    publications {
        register<MavenPublication>("maven")
        withType(MavenPublication::class) {
            signing.sign(this)
            pom {
                name = provider { project.description ?: "Module \"${project.name}\" of snapshot-tests" }
                description = provider { project.name }
                url = "https://github.com/skuzzle/snapshot-tests"
                scm {
                    connection = "scm:git:git://github.com/skuzzle/snapshot-tests"
                    developerConnection = "scm:git:git://github.com/skuzzle/snapshot-tests"
                    url = "https://github.com/skuzzle/snapshot-tests"
                }
                licenses {
                    license {
                        name = "The MIT License (MIT)"
                        url = "http://opensource.org/licenses/MIT"
                        distribution = "repo"
                    }
                }
                developers {
                    developer {
                        id = "simont"
                        name = "Simon Taddiken"
                        email = "simon@taddiken.net"
                    }
                }
            }
        }
    }
}

tasks.prepareRelease.configure { dependsOn(tasks.named("publishToSonatype")) }
