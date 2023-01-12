package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.TestAbortedException;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.SnapshotTestOptions.DiffLineNumbers;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.data.text.TextSnapshot;

public class FailingSnapshotTests {

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
        void testIllegalReuseOfAssertThat(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("");
            snapshot.assertThat("");
        }

        @Test
        void testIllegalReuseOfNamed(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.named("");
            snapshot.named("");
        }

        @Test
        void testIllegalReuseOfIn(Snapshot snapshot) throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.in(Paths.get("egal"));
            snapshot.in(Paths.get("egal"));
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
                .hasMessage("Expected actual not to be null in order to take snapshot");
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
                .hasMessage("Expected actual not to be null in order to take snapshot");
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
                .hasMessage("Expected actual not to be null in order to take snapshot");
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
    void testFailBecauseForceUpdateFromAnnotationDeprecated() throws Throwable {
        frameworkTest
                .expectTestcase(FailBecauseForceUpdateFromAnnotationDeprecated.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%nRemove '@ForceUpdateSnapshots' annotation from your test class and calls to 'justUpdateSnapshot()' then run the tests again."));
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
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(AssertionError.class)
                        .hasMessage(String.format(
                                "Snapshots have been updated forcefully.%nRemove '@ForceUpdateSnapshots' annotation from your test class and calls to 'justUpdateSnapshot()' then run the tests again.")));
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
                                + "Remove '@ForceUpdateSnapshots' annotation from your test class and calls to 'justUpdateSnapshot()' then run the tests again."));
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
                                + "Remove '@ForceUpdateSnapshots' annotation from your test class and calls to 'justUpdateSnapshot()' then run the tests again."));
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
                        + "  7    - test%n"
                        + "     7 + NOT test",
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
    void testFailBecauseSnapshotMismatchWithRawLinenumbers() throws Throwable {
        frameworkTest
                .expectTestcase(FailBecauseSnapshotMismatchWithRawLinenumbers.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format("Stored snapshot doesn't match actual result.%n"
                        + "%nSnapshot location:%n"
                        + "\t%s%n"
                        + "%n"
                        + "Full unified diff of actual result and stored snapshot:%n"
                        + "  1    - test%n"
                        + "     1 + NOT test",
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTests$FailBecauseSnapshotMismatchWithRawLinenumbers_snapshots/testWithSnapshot_0.snapshot")));
    }

    @EnableSnapshotTests
    static class FailBecauseSnapshotMismatchWithRawLinenumbers {

        @Test
        @SnapshotTestOptions(renderLineNumbers = DiffLineNumbers.ACCORDING_TO_RAW_DATA)
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
                        + "  7    - <<line2>>%n"
                        + "     7 + <<line4>>%n"
                        + "  8    - <<line3>>%n"
                        + "     8 + <<line5>>",
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTests$FailBecauseSnapshotMismatchWithWhitespaces_snapshots/testWithSnapshot_0.snapshot")));
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
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(AssertionError.class)
                        .hasMessage(String.format("Snapshots have been created the first time.%n"
                                + "Run the test again and you should see it succeed.")));
    }

    @EnableSnapshotTests
    static class FailBecauseInitial {

        @Test
        void testWithSnapshot(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            assertThat(snapshotResult.contextFiles().snapshotFile()).exists();
            snapshotResult.deleteSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.CREATED_INITIALLY);
        }

        @Test
        void testDisabledAssertionAndInitialAssertion(Snapshot snapshot) throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("xyz").asText().disabled();
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
                        + "  7    - <<test>>%n"
                        + "     7 + <<test2>>",
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTests$SoftAssertions_snapshots/testWithSnapshot_0.snapshot")))
                .hasSuppressedException(
                        new AssertionFailedError(String.format("Stored snapshot doesn't match actual result.%n"
                                + "%nSnapshot location:%n"
                                + "\t%s%n"
                                + "%n"
                                + "Full unified diff of actual result and stored snapshot:%n"
                                + "  7    - <<test>>%n"
                                + "     7 + <<test3>>",
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

    @Test
    void testFailWithAssumptionFailed() throws Exception {
        frameworkTest.expectTestcase(ClassWithDisabledTest.class)
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(TestAbortedException.class));
    }

    @EnableSnapshotTests
    static class ClassWithDisabledTest {
        @Test
        void testWithDisabledAssertion(Snapshot snapshot) throws Exception {
            snapshot.assertThat("xyz").asText().disabled();
        }

        @Test
        void testWithDisabledAndSuccessfulAssertion(Snapshot snapshot) throws Exception {
            snapshot.assertThat("xyz").asText().disabled();
            snapshot.assertThat("xyz").asText().matchesSnapshotText();
        }
    }
}
