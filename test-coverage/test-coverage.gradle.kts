plugins {
    id("build-logic.base")
    id("jacoco-report-aggregation")
    alias(libs.plugins.coveralls)
}

// filtered out because they contain classes with identical full qualified names as their non-legacy counterparts
val modulesWithoutJaxb = rootProject.allJavaModules()
    .filter { it.path != ":snapshot-tests-jaxb" && it.path != ":snapshot-tests-xml-legacy" }

dependencies {
    modulesWithoutJaxb
        .map { it.path }
        .forEach { jacocoAggregation(project(it)) }
}

val coverageReportName = "testCodeCoverageReport"
reporting {
    reports {
        create<JacocoCoverageReport>(coverageReportName) {
            testSuiteName = "test"
        }
    }
}

coverallsJacoco {
    reportPath = layout.buildDirectory.file("reports/jacoco/${coverageReportName}/${coverageReportName}.xml").get().asFile.absolutePath
    reportSourceSets = modulesWithoutJaxb
        .flatMap { it.extensions.getByType<SourceSetContainer>()["main"].allSource.srcDirs }
    dryRun = providers.gradleProperty("coverallsDryRun").isPresent
}

tasks.named("check") {
    dependsOn(tasks.named(coverageReportName))
}

tasks.named("coverallsJacoco") {
    dependsOn(coverageReportName)
}

// Keeps the task name that CI invokes stable
tasks.register("coveralls") {
    group = "verification"
    description = "Uploads the aggregated coverage report to coveralls.io"
    dependsOn("coverallsJacoco")
}
