package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.data.text.TextSnapshot;

public class FailingSnapshotTests {

    private final MetaTest frameworkTest = new MetaTest();

    @Test
    void testFailBecauseForceUpdateFromAnnotationDeprecated() throws Throwable {
        frameworkTest
                .expectTestcase(FailBecauseForceUpdateFromAnnotationDeprecated.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%nRemove 'updateSnapshots = true' attribute from your test class and calls to 'justUpdateSnapshot()' and run the tests again."));
    }

    @EnableSnapshotTests(forceUpdateSnapshots = true) // leave force true
    static class FailBecauseForceUpdateFromAnnotationDeprecated {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    void testFailBecauseForceUpdateFromAnnotationOnTestClass() throws Throwable {
        frameworkTest
                .expectTestcase(FailBecauseForceUpdateFromAnnotationOnTestClass.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%nRemove 'updateSnapshots = true' attribute from your test class and calls to 'justUpdateSnapshot()' and run the tests again."));
    }

    @EnableSnapshotTests
    @ForceUpdateSnapshots
    static class FailBecauseForceUpdateFromAnnotationOnTestClass {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    void testFailBecauseJustUpdate() throws Exception {
        frameworkTest
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

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().justUpdateSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    void testFailBecauseForceUpdateAnnotationOnTestMethod() throws Exception {
        frameworkTest.expectTestcase(FailBecauseForceUpdateAnnotationOnTestMethod.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%n"
                                + "Remove 'updateSnapshots = true' attribute from your test class and calls to 'justUpdateSnapshot()' and run the tests again."));
    }

    @EnableSnapshotTests
    static class FailBecauseForceUpdateAnnotationOnTestMethod {

        @Test
        @ForceUpdateSnapshots
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    void testFailBecauseSnapshotMismatch() throws Throwable {
        frameworkTest
                .expectTestcase(FailBecauseSnapshotMismatch.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Stored snapshot doesn't match actual result.%n"
                        + "%nSnapshot location:%n"
                        + "\t%s%n"
                        + "%n"
                        + "Full unified diff of actual result and stored snapshot:%n"
                        + "+[NOT ]test",
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTests$FailBecauseSnapshotMismatch_snapshots/testWithSnapshot_0.snapshot")));
    }

    @EnableSnapshotTests
    static class FailBecauseSnapshotMismatch {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("NOT test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
        }
    }

    @Test
    void testFailBecauseInitial() throws Throwable {
        frameworkTest
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

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            snapshotResult.deleteSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.CREATED_INITIALLY);
        }
    }

    @Test
    void testMultipleAssertions() throws Exception {
        frameworkTest
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
                        + "%nSnapshot location:%n"
                        + "\t%s%n"
                        + "%n"
                        + "Full unified diff of actual result and stored snapshot:%n"
                        + "test+[2]",
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTests$SoftAssertions_snapshots/testWithSnapshot_0.snapshot")))
                .hasSuppressedException(
                        new AssertionFailedError(String.format("Stored snapshot doesn't match actual result.%n"
                                + "%nSnapshot location:%n"
                                + "\t%s%n"
                                + "%n"
                                + "Full unified diff of actual result and stored snapshot:%n"
                                + "test+[3]",
                                Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTests$SoftAssertions_snapshots/testWithSnapshot_1.snapshot"))));
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

    @Test
    void testWhitespacesDuringTextCompare() throws Exception {
        frameworkTest
                .expectTestcase(WhitespacesDuringTextCompare.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class);
    }

    @EnableSnapshotTests
    static class WhitespacesDuringTextCompare {
        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("   test   ").as(TextSnapshot.text().withIgnoreWhitespaces(false))
                    .matchesSnapshotText();
        }
    }

    @Test
    void testWhitespacesDuringStructureTextCompare() throws Exception {
        frameworkTest
                .expectTestcase(WhitespacesDuringStructureTextCompare.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class);
    }

    @EnableSnapshotTests
    static class WhitespacesDuringStructureTextCompare {
        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("   test   ").as(TextSnapshot.text().withIgnoreWhitespaces(false))
                    .matchesSnapshotStructure();
        }
    }
}
