plugins {
    id("base-conventions")
    id("java-library")
    id("jacoco")
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs + "-parameters"
}

tasks.javadoc {
    options {
        (this as StandardJavadocDocletOptions).apply {
            tags = listOf(
                "apiNote:a:API Note:",
                "implSpec:a:Implementation Requirements:",
                "implNote:a:Implementation Note:")
        }
    }
}

repositories {
    mavenCentral()
}

if (project.getPluginManager().hasPlugin("maven-publish")) {
    configure<PublishingExtension> {
        publications {
            named<MavenPublication>("maven") {
                from(components["java"])
                pom {
                    description .set("Module \"${project.name}\" of snapshot-tests")
                }
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.register("testAgainstJava17", Test::class) {
    description = "Runs all tests using a Java 17 toolchain"
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    })
}
