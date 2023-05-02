plugins {
    `base-conventions`
    id("jacoco-report-aggregation")
    id("com.github.kt3k.coveralls") version "2.12.0"
}
val modulesWithoutXmlLegacy = rootProject.allJavaModules()
    // filtered out because it contains classes with identical full qualified names as the non-legacy project
    .filter { projects.snapshotTestsXmlLegacy.dependencyProject != it }

dependencies {
    modulesWithoutXmlLegacy
        .map { it.path }
        .forEach { jacocoAggregation(project(it)) }
}

reporting {
    reports {
        create<JacocoCoverageReport>("testCodeCoverageReport") {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

coveralls {
    sourceDirs = modulesWithoutXmlLegacy
        .map { it.sourceSets["main"].allSource.srcDirs }
        .map { it.toString() }
    jacocoReportPath = "${buildDir}/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml"
}

tasks.named("check") {
    dependsOn(tasks.named("testCodeCoverageReport"))
}

tasks.named("coveralls") {
    dependsOn("testCodeCoverageReport")
}
