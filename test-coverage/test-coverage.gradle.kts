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

coveralls {
    sourceDirs = modulesWithoutJaxb
        .flatMap { it.extensions.getByType<SourceSetContainer>()["main"].allSource.srcDirs }
        .map { it.toString() }
    jacocoReportPath =
        "${layout.buildDirectory.asFile.get().absolutePath}/reports/jacoco/${coverageReportName}/${coverageReportName}.xml"
}

tasks.named("check") {
    dependsOn(tasks.named(coverageReportName))
}

tasks.named("coveralls") {
    dependsOn(coverageReportName)
}
