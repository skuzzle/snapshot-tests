package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import de.skuzzle.test.snapshots.io.DirectoryResolver;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;
import de.skuzzle.test.snapshots.testcommons.MetaTest;

@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrphanedSnapshotDetectionTest {

    private final MetaTest frameworkTest = MetaTest.junit5();
    private final MockOrphanCollector orphans = MockOrphanCollector.installNewInstance();

    @AfterEach
    void uninstallMockCollector() {
        MockOrphanCollector.uninstall();
    }

    @Test
    void orphaned_file_for_changed_snapshot_directory_should_correctly_be_detected() throws IOException {
        final Path localTemp = DirectoryResolver.resolve("localtemp");
        final String fileName = "someSnapshotTest_0.snapshot";
        final Path snapshotFile = localTemp.resolve(fileName);

        try {
            Files.createDirectories(localTemp);
            createArtificialSnapshotFile(TestCase.class, "someSnapshotTest",
                    "someSnapshotTest", "content", false).writeTo(snapshotFile);

            frameworkTest.executeTestcasesIn(TestCase.class);

            orphans.results().areAtLeast(1, forFileWithName(fileName));

        } finally {
            Files.delete(snapshotFile);
            Files.delete(localTemp);
        }
    }

    @Test
    void orphaned_file_for_deleted_test_should_correctly_be_detected() throws Throwable {
        frameworkTest.executeTestcasesIn(TestCase.class);

        orphans.results().areExactly(1, forFileWithName("testThatHasBeenDeleted_0.snapshot"));
    }

    @Test
    void snapshot_for_test_that_is_disabled_should_not_be_detected_as_orphan() throws Throwable {
        frameworkTest.executeTestcasesIn(TestCase.class);

        orphans.results().areNot(forFileWithName("disabledTest_0.snapshot"));
    }

    @Test
    void disabled_assertion_should_not_be_detected_as_orphan() throws IOException {
        frameworkTest.executeTestcasesIn(TestCase.class);

        orphans.results().areNot(forFileWithName("testWithDisabledAssertion_0.snapshot"));
    }

    @Test
    void snapshot_for_existing_test_in_different_class_that_was_not_executed_should_not_be_detected_as_orphan()
            throws Throwable {
        frameworkTest.executeTestcasesIn(AnotherTestClass.class);

        orphans.results().areNot(forFileWithName("disabledTest_0.snapshot"));
    }

    @Test
    void orphaned_files_should_not_be_detected_for_failing_tests() throws Throwable {
        frameworkTest.executeTestcasesIn(TestCase.class);

        orphans.results().areNot(forFileWithName("failingTestMethod.snapshot"));
    }

    @Test
    void orphaned_file_for_disabled_assertion_within_testshould_correctly_be_detected() throws Throwable {
        frameworkTest.executeTestcasesIn(TestCase.class);

        orphans.results().areExactly(1, forFileWithName("testWithChangedAssertions_1.snapshot"));
    }

    @EnableSnapshotTests
    static class TestCase {

        @Test
        void testWithChangedAssertions(Snapshot snapshot) {
            MetaTest.assumeMetaTest();
            snapshot.assertThat("1").asText().matchesSnapshotText();
            // snapshot.assertThat("2").asText().matchesSnapshotText();
        }

        @Test
        void someSnapshotTest(Snapshot snapshot) {
            MetaTest.assumeMetaTest();
        }

        @Test
        @Disabled
        void disabledTest(Snapshot snapshot) {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("1").asText().matchesSnapshotText();
        }

        @Test
        void testWithDisabledAssertion(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("1").asText().disabled();
        }

        @Test
        void failingTestMethod(Snapshot snapshot) {
            MetaTest.assumeMetaTest();

            throw new RuntimeException();
        }

    }

    @EnableSnapshotTests
    static class AnotherTestClass {
        @Test
        void anotherWorkingTest(Snapshot snapshot) {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("1").asText().matchesSnapshotText();
        }
    }

    @Test
    void header_should_be_changed_if_test_method_has_been_renamed() throws Throwable {
        final Path snapshotFile = storeArtificialSnapshotFile(
                TestCaseThatChangesHeader.class,
                "old_method_name",
                "custom_snapshot_name",
                "content",
                false);

        try {
            frameworkTest.executeTestcasesIn(TestCaseThatChangesHeader.class);

            final SnapshotHeader updatedHeader = SnapshotFile.fromSnapshotFile(snapshotFile).header();
            assertThat(updatedHeader.get(SnapshotHeader.TEST_METHOD)).isEqualTo("new_method_name");
        } finally {
            Files.delete(snapshotFile);
        }
    }

    @EnableSnapshotTests
    static class TestCaseThatChangesHeader {

        @Test
        void new_method_name(Snapshot snapshot) {
            MetaTest.assumeMetaTest();

            snapshot.named("custom_snapshot_name").assertThat("content").asText().matchesSnapshotText();
        }
    }

    private Path determineDefaultSnapshotDirectory(Class<?> testClass) throws IOException {
        final Path snapshotDirectory = DefaultSnapshotConfiguration.forTestClass(testClass)
                .determineSnapshotDirectory();
        Files.createDirectories(snapshotDirectory);
        return snapshotDirectory;
    }

    private SnapshotFile createArtificialSnapshotFile(
            Class<?> testClass,
            String testMethodName,
            String snapshotName,
            String snapshot,
            boolean dynamicDirectory) throws IOException {

        final SnapshotHeader header = SnapshotHeader.fromMap(Map.of(
                SnapshotHeader.SNAPSHOT_NUMBER, "0",
                SnapshotHeader.TEST_METHOD, testMethodName,
                SnapshotHeader.TEST_CLASS, testClass.getName(),
                SnapshotHeader.SNAPSHOT_NAME, snapshotName,
                SnapshotHeader.DYNAMIC_DIRECTORY, "" + dynamicDirectory));
        return SnapshotFile.of(header, snapshot);
    }

    private Path storeArtificialSnapshotFile(
            Class<?> testClass,
            String testMethodName,
            String snapshotName,
            String snapshot,
            boolean dynamicDirectory) throws IOException {

        final Path snapshotFilePath = determineDefaultSnapshotDirectory(testClass)
                .resolve(snapshotName + ".snapshot");
        createArtificialSnapshotFile(testClass, testMethodName, snapshotName, snapshot, dynamicDirectory)
                .writeTo(snapshotFilePath);

        return snapshotFilePath;
    }

    private Condition<OrphanDetectionResult> forFileWithName(String fileName) {
        return new Condition<>(result -> result.snapshotFile().getFileName().toString().equals(fileName),
                "with filename '%s'", fileName);
    }
}
