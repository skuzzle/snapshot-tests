import java.util.Base64

plugins {
    id("base-conventions")
    id("maven-publish")
    id("signing")
}

tasks.withType<Sign>().configureEach {
    onlyIf("Run on CI") { project.isCI }
}

tasks.withType<PublishToMavenLocal>().configureEach {
    dependsOn(tasks.build)
}

signing {
    // The gpg key is injected by jenkins as a base64 encoded string. That is because jenkins doesn't support
    // storing secret text credentials with newlines. Thus we need to decode the base64 string before we can sign
    val signingKey : String? = base64Decode(findProperty("base64EncodedAsciiArmoredSigningKey") as String?)
    val signingPassword : String? = findProperty("signingPassword") as String?
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}

fun base64Decode(encodedString : String?) : String? {
    if (encodedString != null && !encodedString.isEmpty()) {
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
        create<MavenPublication>("maven") {
            pom {
                name.set(provider { project.description ?: "${project.group}:${project.name}" })
                description.set(provider { project.name })
                url.set("https://github.com/skuzzle/snapshot-tests")
                scm {
                    connection.set("scm:git:git://github.com/skuzzle/snapshot-tests")
                    developerConnection.set("scm:git:git://github.com/skuzzle/snapshot-tests")
                    url.set("https://github.com/skuzzle/snapshot-tests")
                }
                licenses {
                    license {
                        name.set("The MIT License (MIT)")
                        url.set("http://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("simont")
                        name.set("Simon Taddiken")
                        email.set("simon@taddiken.net")
                    }
                }
            }
        }
    }
}

tasks.withType<GenerateMavenPom>().configureEach {
    doLast {
        val pomXml = destination.readText();
        checkThat(pomXml.indexOf("<name>") >= 0, "POM must have a name element: " + destination)
        checkThat(pomXml.indexOf("<url>") >= 0, "POM must have a url element: " + destination)
        checkThat(pomXml.indexOf("<description>") >= 0, "POM must have a description element" + destination)
        checkThat(pomXml.indexOf("<license>") >= 0, "POM must have a license element" + destination)
        checkThat(pomXml.indexOf("<scm>") >= 0, "POM must have a scm element" + destination)
    }
}

