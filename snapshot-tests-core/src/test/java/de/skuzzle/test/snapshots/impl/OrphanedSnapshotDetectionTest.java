package de.skuzzle.test.snapshots.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.impl.MockSnapshotLoggerFactory.MockSnapshotLogger;

@DisplayNameGeneration(ReplaceUnderscores.class)
public class OrphanedSnapshotDetectionTest {

    private final MetaTest frameworkTest = new MetaTest();

    @AfterEach
    void uninstallMockLogger() {
        MockSnapshotLoggerFactory.uninstall();
    }

    @Test
    void orphaned_file_for_deleted_test_should_correctly_be_detected() throws Throwable {
        final MockSnapshotLogger logger = MockSnapshotLoggerFactory.install();

        frameworkTest.expectTestcase(TestCase.class);

        logger.assertContainsExactlyOneEventWhere(logEvent -> logEvent.hasLevel("warn")
                && logEvent.messageMatches(message -> message.startsWith("Found orphaned snapshot file."))
                && logEvent
                        .containsParamWhere(param -> param.toString().contains("testThatHasBeenDeleted_0.snapshot")));
    }

    @Test
    void orphaned_files_should_not_be_detected_for_failing_tests() throws Throwable {
        final MockSnapshotLogger logger = MockSnapshotLoggerFactory.install();

        frameworkTest.expectTestcase(TestCase.class);

        logger.assertContainsNoEventWhere(
                logEvent -> logEvent
                        .containsParamWhere(param -> param.toString().contains("failingTestMethod.snapshot")));
    }

    @EnableSnapshotTests
    static class TestCase {

        @Test
        void failingTestMethod() {
            throw new RuntimeException();
        }

    }
}
