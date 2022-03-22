package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;

@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrphanedSnapshotDetectionTest {

    private final MetaTest frameworkTest = new MetaTest();
    private final MockOrphanCollector orphans = MockOrphanCollector.installNewInstance();

    @AfterEach
    void uninstallMockCollector() {
        MockOrphanCollector.uninstall();
    }

    @Test
    void orphaned_file_for_deleted_test_should_correctly_be_detected() throws Throwable {
        frameworkTest.executeTestcasesIn(TestCase.class);

        orphans.results().areExactly(1, forFileWithName("testThatHasBeenDeleted_0.snapshot"));
    }

    @Test
    void orphaned_files_should_not_be_detected_for_failing_tests() throws Throwable {
        frameworkTest.executeTestcasesIn(TestCase.class);

        orphans.results().areNot(forFileWithName("failingTestMethod.snapshot"));
    }

    @EnableSnapshotTests
    static class TestCase {

        @Test
        void failingTestMethod(Snapshot snapshot) {
            MetaTest.assumeMetaTest();

            throw new RuntimeException();
        }
    }

    @Test
    void header_should_be_changed_if_test_method_has_been_renamed() throws Throwable {
        final Path snapshotFile = createArtificialSnapshotFile(
                TestCaseThatChangesHeader.class,
                "old_method_name",
                "custom_snapshot_name",
                "content");

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

    private Path createArtificialSnapshotFile(
            Class<?> testClass,
            String testMethodName,
            String snapshotName,
            String snapshot) throws IOException {

        final SnapshotHeader header = SnapshotHeader.fromMap(Map.of(
                SnapshotHeader.SNAPSHOT_NUMBER, "0",
                SnapshotHeader.TEST_METHOD, testMethodName,
                SnapshotHeader.TEST_CLASS, testClass.getName(),
                SnapshotHeader.SNAPSHOT_NAME, snapshotName));
        final SnapshotConfiguration configuration = DefaultSnapshotConfiguration
                .forTestClass(testClass);
        final Path snapshotFilePath = configuration.determineSnapshotDirectory().resolve(snapshotName + ".snapshot");
        SnapshotFile.of(header, snapshot).writeTo(snapshotFilePath);

        return snapshotFilePath;
    }

    private Condition<OrphanDetectionResult> forFileWithName(String fileName) {
        return new Condition<>(result -> result.snapshotFile().getFileName().toString().equals(fileName),
                "with filename '%s'", fileName);
    }
}
