plugins {
    `base-conventions`
    id("jacoco-report-aggregation")
    id("com.github.kt3k.coveralls") version "2.12.0"
}
val modulesWithoutXmlLegacy = rootProject.allJavaModules()
    // filtered out because it contains classes with identical full qualified names as the non-legacy project
    .filter { projects.snapshotTestsXmlLegacy.dependencyProject != it }
    .onEach { println(it.toString()) }

dependencies {
    modulesWithoutXmlLegacy
        .map { it.path }
        .forEach { jacocoAggregation(project(it)) }
}

val coverageReportName = "testCodeCoverageReport"
reporting {
    reports {
        create<JacocoCoverageReport>(coverageReportName) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

coveralls {
    sourceDirs = modulesWithoutXmlLegacy
        .map { it.sourceSets["main"].allSource.srcDirs }
        .map { it.toString() }.onEach { println("xxx $it") }
    jacocoReportPath = "${buildDir}/reports/jacoco/${coverageReportName}/${coverageReportName}.xml"
}

tasks.named("check") {
    dependsOn(tasks.named<JacocoCoverageReport>(coverageReportName))
}

tasks.named("coveralls") {
    dependsOn("testCodeCoverageReport")
}
