package de.skuzzle.test.snapshots;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

public class FailingSnapshotTests {

    @Test
    void testFailBecauseForceUpdateFromAnnotation() throws Throwable {
        new TestTest()
                .expectTestcase(FailBecauseForceUpdateFromAnnotation.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(
                        "Snapshots have been updated forcefully.%nRemove 'updateSnapshots = true' attribute from your test class %s and calls to 'justUpdateSnapshot()' and run the tests again.",
                        FailBecauseForceUpdateFromAnnotation.class.getName());
    }

    @Test
    void testFailBecauseForceUpdate() throws Exception {
        new TestTest()
                .expectTestcase(FailBecauseJustUpdate.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(
                        "Snapshots have been updated forcefully.%n"
                                + "Remove 'updateSnapshots = true' attribute from your test class %s and calls to 'justUpdateSnapshot()' and run the tests again.",
                        FailBecauseJustUpdate.class.getName());
    }

    @Test
    void testFailBecauseSnapshotMismatch() throws Throwable {
        new TestTest()
                .expectTestcase(FailBecauseSnapshotMismatch.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("[Stored snapshot doesn't match actual result.%n"
                        + "Unified diff:%n"
                        + "\"+[NOT ]test\"] %n"
                        + "Expecting:%n"
                        + " <\"\"NOT test\"\">%n"
                        + "to be equal to:%n"
                        + " <\"\"test\"\">%n"
                        + "but was not."));
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

    @SnapshotAssertions(forceUpdateSnapshots = true)
    static class FailBecauseForceUpdateFromAnnotation {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("test").asJson().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @SnapshotAssertions
    static class FailBecauseJustUpdate {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("test").asJson().justUpdateSnapshot();
            snapshotResult.deleteSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
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

    @SnapshotAssertions
    static class FailBecauseSnapshotMismatch {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            TestTest.assumeMetaTest();

            final SnapshotResult snapshotResult = snapshot.assertThat("NOT test").asJson().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
        }
    }
}
