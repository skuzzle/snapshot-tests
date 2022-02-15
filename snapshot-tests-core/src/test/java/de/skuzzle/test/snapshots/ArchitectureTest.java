package de.skuzzle.test.snapshots;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.conditions.ArchConditions.dependOnClassesThat;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = ArchitectureTest.class)
public class ArchitectureTest {

    @ArchTest
    final static ArchRule CORE_SHOULD_NOT_DEPEND_ON_JUNIT = noClasses().that()
            .resideInAnyPackage("de.skuzzle.test.snapshots.impl")
            .should(dependOnClassesThat(resideInAPackage("org.junit.*"))
                    .as("use Junit"));
}
