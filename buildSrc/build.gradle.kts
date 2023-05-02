import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.15.0")
}

tasks {
    withType<JavaCompile>().configureEach {
        options.release.set(11)
    }
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
            allWarningsAsErrors = true
        }
    }
}
