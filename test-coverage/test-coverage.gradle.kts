plugins {
    `base-conventions`
    id("jacoco-report-aggregation")
    id("com.github.kt3k.coveralls") version "2.12.0"
}
val allJavaModules: List<Project> by rootProject.extra
val modulesWithoutJaxb = allJavaModules

dependencies {
    modulesWithoutJaxb
        .map({ it.path })
        .filter({ ":snapshot-tests-jaxb" != it.toString() && ":snapshot-tests-xml-legacy" != it.toString() })
        .forEach({ jacocoAggregation(it) })
}

reporting {
    reports {
        create<JacocoCoverageReport>("jacocoRootReport") {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

coveralls {
    //sourceDirs = modulesWithoutJaxb.sourceSets.main.allSource.srcDirs.flatten()
    jacocoReportPath = "${buildDir}/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml"
}

tasks.named("check") {
    dependsOn(tasks.named("testCodeCoverageReport"))
}
tasks.named("coveralls") {
    dependsOn("testCodeCoverageReport")
}
