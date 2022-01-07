package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotResult;
import de.skuzzle.test.snapshots.SnapshotStatus;

public class FailingSnapshotTests {

    @Test
    void testFailBecauseForceUpdateFromAnnotation() throws Throwable {
        new MetaTest()
                .expectTestcase(FailBecauseForceUpdateFromAnnotation.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%nRemove 'updateSnapshots = true' attribute from your test class and calls to 'justUpdateSnapshot()' and run the tests again."));
    }

    @EnableSnapshotTests(forceUpdateSnapshots = true) // leave force true
    static class FailBecauseForceUpdateFromAnnotation {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    void testFailBecauseJustUpdate() throws Exception {
        new MetaTest()
                .expectTestcase(FailBecauseJustUpdate.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%n"
                                + "Remove 'updateSnapshots = true' attribute from your test class and calls to 'justUpdateSnapshot()' and run the tests again."));
    }

    @EnableSnapshotTests
    static class FailBecauseJustUpdate {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("test").asText().justUpdateSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    void testFailBecauseSnapshotMismatch() throws Throwable {
        new MetaTest()
                .expectTestcase(FailBecauseSnapshotMismatch.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Stored snapshot doesn't match actual result.%n"
                        + "Unified diff:%n"
                        + "+[NOT ]test"));
    }

    @EnableSnapshotTests
    static class FailBecauseSnapshotMismatch {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("NOT test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
        }
    }

    @Test
    void testFailBecauseInitial() throws Throwable {
        new MetaTest()
                .expectTestcase(FailBecauseInitial.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Snapshots have been created the first time.%n"
                        + "Run the test again and you should see it succeed."));
    }

    @EnableSnapshotTests
    static class FailBecauseInitial {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            snapshotResult.deleteSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.CREATED_INITIALLY);
        }
    }

    @Test
    void testMultipleAssertions() throws Exception {
        new MetaTest()
                .expectTestcase(MultipleAssertions.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class);
    }

    @EnableSnapshotTests
    static class MultipleAssertions {
        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("test").asText().matchesSnapshotText();
            snapshot.assertThat("test2").asText().matchesSnapshotText();
        }
    }

    @Test
    void testSoftAssertions() throws Exception {
        new MetaTest()
                .expectTestcase(SoftAssertions.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Stored snapshot doesn't match actual result.%n"
                        + "Unified diff:%n"
                        + "test+[2]"))
                .hasSuppressedException(
                        new AssertionFailedError(String.format("Stored snapshot doesn't match actual result.%n"
                                + "Unified diff:%n"
                                + "test+[3]")));
    }

    @EnableSnapshotTests(softAssertions = true)
    static class SoftAssertions {
        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("test2").asText().matchesSnapshotText();
            snapshot.assertThat("test3").asText().matchesSnapshotText();
        }
    }
}