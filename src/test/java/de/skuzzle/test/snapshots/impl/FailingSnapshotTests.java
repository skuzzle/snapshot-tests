package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.SnapshotAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotResult;
import de.skuzzle.test.snapshots.SnapshotStatus;

public class FailingSnapshotTests {

    @Test
    void testFailBecauseForceUpdateFromAnnotation() throws Throwable {
        new TestTest()
                .expectTestcase(FailBecauseForceUpdateFromAnnotation.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%nRemove 'updateSnapshots = true' attribute from your test class and calls to 'justUpdateSnapshot()' and run the tests again."));
    }

    @SnapshotAssertions(forceUpdateSnapshots = true)
    static class FailBecauseForceUpdateFromAnnotation {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("test").asJson().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    void testFailBecauseJustUpdate() throws Exception {
        new TestTest()
                .expectTestcase(FailBecauseJustUpdate.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%n"
                                + "Remove 'updateSnapshots = true' attribute from your test class and calls to 'justUpdateSnapshot()' and run the tests again."));
    }

    @SnapshotAssertions
    static class FailBecauseJustUpdate {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("test").asJson().justUpdateSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    void testFailBecauseSnapshotMismatch() throws Throwable {
        new TestTest()
                .expectTestcase(FailBecauseSnapshotMismatch.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Stored snapshot doesn't match actual result.%n"
                        + "Unified diff:%n"
                        + "\"+[NOT ]test\""));
    }

    @SnapshotAssertions
    static class FailBecauseSnapshotMismatch {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("NOT test").asJson().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
        }
    }

    @Test
    void testFailBecauseInitial() throws Throwable {
        new TestTest()
                .expectTestcase(FailBecauseInitial.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Snapshots have been created the first time.%n"
                        + "Run the test again and you should see it succeed."));
    }

    @SnapshotAssertions
    static class FailBecauseInitial {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("test").asJson().matchesSnapshotText();
            snapshotResult.deleteSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.CREATED_INITIALLY);
        }
    }

    @Test
    void testMultipleAssertions() throws Exception {
        new TestTest()
                .expectTestcase(MultipleAssertions.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class);
    }

    @SnapshotAssertions
    static class MultipleAssertions {
        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            snapshot.assertThat("test").asJson().matchesSnapshotText();
            snapshot.assertThat("test2").asJson().matchesSnapshotText();
        }
    }

    @Test
    void testSoftAssertions() throws Exception {
        new TestTest()
                .expectTestcase(SoftAssertions.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Stored snapshot doesn't match actual result.%n"
                        + "Unified diff:%n"
                        + "\"test+[2]\""))
                .hasSuppressedException(
                        new AssertionFailedError(String.format("Stored snapshot doesn't match actual result.%n"
                                + "Unified diff:%n"
                                + "\"test+[3]\"")));
    }

    @SnapshotAssertions(softAssertions = true)
    static class SoftAssertions {
        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            snapshot.assertThat("test2").asJson().matchesSnapshotText();
            snapshot.assertThat("test3").asJson().matchesSnapshotText();
        }
    }
}
