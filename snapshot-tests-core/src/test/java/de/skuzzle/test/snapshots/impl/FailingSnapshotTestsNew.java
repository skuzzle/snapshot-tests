package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.data.text.TextSnapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

public class FailingSnapshotTestsNew {

    private final MetaTest frameworkTest = new MetaTest();

    @Test
    void testDetectIncompleteDSLReuse() throws Exception {
        frameworkTest.expectTestcase(DetectIncompleteDslReuse.class)
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("Detected illegal reuse of a DSL stage"));
    }

    @EnableSnapshotTests
    static class DetectIncompleteDslReuse {

        @Test
        void testIllegalReuse(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("");
            snapshot.assertThat("");
        }
    }

    @Test
    void testDetectIncompleteDSLUsage() throws Exception {
        frameworkTest.expectTestcase(DetectIncompleteDslUsage.class)
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("Detected incomplete DSL usage"));
    }

    @EnableSnapshotTests
    static class DetectIncompleteDslUsage {

        @Test
        void testOnlyAssert(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("");
        }

        @Test
        void testOnlyDirectory(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.in(Paths.get("/"));
        }

        @Test
        void testOnlyName(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.named("whatever");
        }

        @Test
        void testNoTerminalOp(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.in(Paths.get("/"))
                    .named("whatever")
                    .assertThat("")
                    .as(Object::toString);
        }
    }

    @Test
    void testFailBecauseOfNullInputSnapshotAlreadyExists() throws Throwable {
        frameworkTest.expectTestcase(FailBecauseOfNullInputSnapshotAlreadyExists.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage("Expected actual not to be null but to match stored snapshot:\n\nsnapshot text");
    }

    @EnableSnapshotTests
    static class FailBecauseOfNullInputSnapshotAlreadyExists {

        @Test
        void testPassNullToSnapshot(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat(null).asText().matchesSnapshotText();
        }
    }

    @Test
    void testFailBecauseOfNullInputJustUpdateSnapshot() throws Throwable {
        frameworkTest.expectTestcase(FailBecauseOfNullInputJustUpdateSnapshot.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage("Expected actual not to be null in order to take initial snapshot");
    }

    @EnableSnapshotTests
    static class FailBecauseOfNullInputJustUpdateSnapshot {

        @Test
        void testPassNullToSnapshot(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat(null).asText().justUpdateSnapshot();
        }
    }

    @Test
    void testFailBecauseOfNullInputInitialSnapshot() throws Throwable {
        frameworkTest.expectTestcase(FailBecauseOfNullInputInitialSnapshot.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage("Expected actual not to be null in order to take initial snapshot");
    }

    @EnableSnapshotTests
    static class FailBecauseOfNullInputInitialSnapshot {

        @Test
        void testPassNullToSnapshot(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat(null).asText().matchesSnapshotText();
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
                        + "  1    - test%n"
                        + "     1 + NOT test",
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTestsNew$FailBecauseSnapshotMismatch_snapshots/testWithSnapshot_0.snapshot")));
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
    void testFailBecauseSnapshotMismatchWithWhitespaces() throws Throwable {
        frameworkTest
                .expectTestcase(FailBecauseSnapshotMismatchWithWhitespaces.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Stored snapshot doesn't match actual result.%n"
                        + "%nSnapshot location:%n"
                        + "\t%s%n"
                        + "%n"
                        + "Full unified diff of actual result and stored snapshot:%n"
                        + "Strings differ in linebreaks. Expected: 'CRLF(\\r\\n)', Actual encountered: 'LF(\\n)'%n"
                        + "%n"
                        + "  1    - <<line2>>%n"
                        + "     1 + <<line4>>%n"
                        + "  2    - <<line3>>%n"
                        + "     2 + <<line5>>",
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTestsNew$FailBecauseSnapshotMismatchWithWhitespaces_snapshots/testWithSnapshot_0.snapshot")));
    }

    @EnableSnapshotTests
    static class FailBecauseSnapshotMismatchWithWhitespaces {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("line4\nline5")
                    .as(TextSnapshot.text().withIgnoreWhitespaces(false).withContextLines(5))
                    .matchesSnapshotText();
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
