plugins {
    id("snapshot-tests.base-conventions")
    id("jacoco-report-aggregation")
    id("com.github.kt3k.coveralls") version "2.12.0"
}

// TODO: check if still works
val modulesWithoutJaxb = allJavaModules
        .filter({ "project ':snapshot-tests-jaxb'" != it.toString() && "project ':snapshot-tests-xml-legacy'" != it.toString() })


dependencies {
    modulesWithoutJaxb
            .each({ jacocoAggregation it })
}

reporting {
    reports {
        testCodeCoverageReport(JacocoCoverageReport) {
            testType = TestSuiteType.UNIT_TEST
        }
    }
}

coveralls {
    sourceDirs = modulesWithoutJaxb.sourceSets.main.allSource.srcDirs.flatten()
    jacocoReportPath = "${buildDir}/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml"
}

tasks.named("check") {
    dependsOn tasks.named("testCodeCoverageReport", JacocoReport)
}
tasks.named("coveralls") {
    dependsOn("testCodeCoverageReport")
}
