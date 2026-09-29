plugins {
    id("build-logic.base")
    id("java-library")
    id("jacoco")
    id("jvm-test-suite")
}

// Version production code is compiled *for*, see the --release usage below
val productionCodeJavaVersion = JavaLanguageVersion.of(11)

// Version used to compile and run everything, including the tests
val buildJavaVersion = JavaLanguageVersion.of(21)

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion = buildJavaVersion
    }
}

tasks {
    compileJava {
        // Production code is compiled by a current compiler targeting productionCodeJavaVersion
        // via --release, rather than by a compiler of that version. --release emits bytecode
        // for, and limits the code to the API of, that version. Gradle also derives the
        // published variants' 'org.gradle.jvm.version' attribute from it.
        options.release = productionCodeJavaVersion.asInt()
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    javadoc {
        options {
            (this as StandardJavadocDocletOptions).apply {
                tags = listOf(
                    "apiNote:a:API Note:",
                    "implSpec:a:Implementation Requirements:",
                    "implNote:a:Implementation Note:"
                )
            }
        }
    }
}

testing.suites.named<JvmTestSuite>("test") {
    useJUnitJupiter(requiredVersionFromLibs("junit5"))
}

dependencies {
    implementation(requiredLibraryFromLibs("apiguardian"))

    testImplementation(requiredLibraryFromLibs("equalsverifier"))
    testImplementation(requiredLibraryFromLibs("assertj-core"))
}
