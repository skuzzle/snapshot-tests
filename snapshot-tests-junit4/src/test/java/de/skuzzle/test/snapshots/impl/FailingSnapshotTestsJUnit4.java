package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.AssumptionViolatedException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.SnapshotTestOptions.DiffLineNumbers;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.data.text.TextSnapshot;
import de.skuzzle.test.snapshots.junit4.SnapshotRule;
import de.skuzzle.test.snapshots.testcommons.MetaTest;

public class FailingSnapshotTestsJUnit4 {

    private final MetaTest frameworkTest = MetaTest.junit4();

    @Test
    public void testDetectIncompleteDSLReuse() throws Exception {
        frameworkTest.expectTestcase(DetectIncompleteDslReuse.class)
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining(
                                "Detected incomplete DSL usage. Please always call a terminal operation (see JavaDoc of the Snapshot class for details). If you want to temporarily disable a snapshot assertion, use the disabled() terminal operation."));
    }

    public static class DetectIncompleteDslReuse {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testIllegalReuseOfAssertThat() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("");
            snapshot.assertThat("");
        }

        @Test
        public void testIllegalReuseOfNamed() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.named("");
            snapshot.named("");
        }

        @Test
        public void testIllegalReuseOfIn() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.in(Paths.get("egal"));
            snapshot.in(Paths.get("egal"));
        }
    }

    @Test
    public void testDetectIncompleteDSLUsage() throws Exception {
        frameworkTest.expectTestcase(DetectIncompleteDslUsage.class)
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("Detected incomplete DSL usage"));
    }

    public static class DetectIncompleteDslUsage {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testOnlyAssert() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("");
        }

        @Test
        public void testOnlyDirectory() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.in(Paths.get("/"));
        }

        @Test
        public void testOnlyName() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.named("whatever");
        }

        @Test
        public void testNoTerminalOp() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.in(Paths.get("/"))
                    .named("whatever")
                    .assertThat("")
                    .as(Object::toString);
        }
    }

    @Test
    public void testFailBecauseOfNullInputSnapshotAlreadyExists() throws Throwable {
        frameworkTest.expectTestcase(FailBecauseOfNullInputSnapshotAlreadyExists.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage("Expected actual not to be null in order to take snapshot");
    }

    public static class FailBecauseOfNullInputSnapshotAlreadyExists {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testPassNullToSnapshot() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat(null).asText().matchesSnapshotText();
        }
    }

    @Test
    public void testFailBecauseOfNullInputJustUpdateSnapshot() throws Throwable {
        frameworkTest.expectTestcase(FailBecauseOfNullInputJustUpdateSnapshot.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage("Expected actual not to be null in order to take snapshot");
    }

    public static class FailBecauseOfNullInputJustUpdateSnapshot {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testPassNullToSnapshot() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat(null).asText().justUpdateSnapshot();
        }
    }

    @Test
    public void testFailBecauseOfNullInputInitialSnapshot() throws Throwable {
        frameworkTest.expectTestcase(FailBecauseOfNullInputInitialSnapshot.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage("Expected actual not to be null in order to take snapshot");
    }

    public static class FailBecauseOfNullInputInitialSnapshot {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testPassNullToSnapshot() throws Exception {
            MetaTest.assumeMetaTest();

            snapshot.assertThat(null).asText().matchesSnapshotText();
        }
    }

    @Test
    public void testFailBecauseForceUpdateFromAnnotationOnTestClass() throws Throwable {
        frameworkTest
                .expectTestcase(FailBecauseForceUpdateFromAnnotationOnTestClass.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%nRemove '@ForceUpdateSnapshots' annotation from your test class and calls to 'justUpdateSnapshot()' then run the tests again."));
    }

    @ForceUpdateSnapshots
    public static class FailBecauseForceUpdateFromAnnotationOnTestClass {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    public void testFailBecauseJustUpdate() throws Exception {
        frameworkTest
                .expectTestcase(FailBecauseJustUpdate.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%n"
                                + "Remove '@ForceUpdateSnapshots' annotation from your test class and calls to 'justUpdateSnapshot()' then run the tests again."));
    }

    public static class FailBecauseJustUpdate {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().justUpdateSnapshot();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    public void testFailBecauseForceUpdateAnnotationOnTestMethod() throws Exception {
        frameworkTest.expectTestcase(FailBecauseForceUpdateAnnotationOnTestMethod.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class)
                .hasMessage(String.format(
                        "Snapshots have been updated forcefully.%n"
                                + "Remove '@ForceUpdateSnapshots' annotation from your test class and calls to 'justUpdateSnapshot()' then run the tests again."));
    }

    public static class FailBecauseForceUpdateAnnotationOnTestMethod {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        @ForceUpdateSnapshots
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.UPDATED_FORCEFULLY);
        }
    }

    @Test
    public void testFailBecauseSnapshotMismatch() throws Throwable {
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
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTestsJUnit4$FailBecauseSnapshotMismatch_snapshots/testWithSnapshot_0.snapshot")));
    }

    public static class FailBecauseSnapshotMismatch {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("NOT test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
        }
    }

    @Test
    public void testFailBecauseSnapshotMismatchWithRawLinenumbers() throws Throwable {
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
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTestsJUnit4$FailBecauseSnapshotMismatchWithRawLinenumbers_snapshots/testWithSnapshot_0.snapshot")));
    }

    public static class FailBecauseSnapshotMismatchWithRawLinenumbers {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        @SnapshotTestOptions(renderLineNumbers = DiffLineNumbers.ACCORDING_TO_RAW_DATA)
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("NOT test").asText().matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
        }
    }

    @Test
    public void testFailBecauseSnapshotMismatchWithWhitespaces() throws Throwable {
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
                        Path.of("src/test/resources/de/skuzzle/test/snapshots/impl/FailingSnapshotTestsJUnit4$FailBecauseSnapshotMismatchWithWhitespaces_snapshots/testWithSnapshot_0.snapshot")));
    }

    public static class FailBecauseSnapshotMismatchWithWhitespaces {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("line4\nline5")
                    .as(TextSnapshot.text().withIgnoreWhitespaces(false).withContextLines(5))
                    .matchesSnapshotText();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.ASSERTED);
        }
    }

    @Test
    public void testFailBecauseInitial() throws Throwable {
        frameworkTest
                .expectTestcase(FailBecauseInitial.class)
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(AssertionError.class)
                        .hasMessage(String.format("Snapshots have been created the first time.%n"
                                + "Run the test again and you should see it succeed.")));
    }

    public static class FailBecauseInitial {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            snapshotResult.contextFiles().deleteFiles();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.CREATED_INITIALLY);
        }

        @Test
        public void testDisabledAssertionAndInitialAssertion() throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("xyz").asText().disabled();
            final SnapshotTestResult snapshotResult = snapshot.assertThat("test").asText().matchesSnapshotText();
            assertThat(snapshotResult.contextFiles().snapshotFile()).exists();
            snapshotResult.contextFiles().deleteFiles();
            assertThat(snapshotResult.status()).isEqualTo(SnapshotStatus.CREATED_INITIALLY);
        }
    }

    @Test
    public void testMultipleAssertions() throws Exception {
        frameworkTest
                .expectTestcase(MultipleAssertions.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class);
    }

    public static class MultipleAssertions {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("test").asText().matchesSnapshotText();
            snapshot.assertThat("test2").asText().matchesSnapshotText();
        }
    }

    @Test
    public void testWhitespacesDuringTextCompare() throws Exception {
        frameworkTest
                .expectTestcase(WhitespacesDuringTextCompare.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class);
    }

    public static class WhitespacesDuringTextCompare {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("   test   ").as(TextSnapshot.text().withIgnoreWhitespaces(false))
                    .matchesSnapshotText();
        }
    }

    @Test
    public void testWhitespacesDuringStructureTextCompare() throws Exception {
        frameworkTest
                .expectTestcase(WhitespacesDuringStructureTextCompare.class)
                .toFailWithExceptionWhich()
                .isInstanceOf(AssertionError.class);
    }

    public static class WhitespacesDuringStructureTextCompare {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithSnapshot() throws Throwable {
            MetaTest.assumeMetaTest();

            snapshot.assertThat("   test   ").as(TextSnapshot.text().withIgnoreWhitespaces(false))
                    .matchesSnapshotStructure();
        }
    }

    @Test
    public void testFailWithAssumptionFailed() throws Exception {
        frameworkTest.expectTestcase(ClassWithDisabledTest.class)
                .toAllFailWithExceptionWhich(matches -> matches
                        .isInstanceOf(AssumptionViolatedException.class));
    }

    public static class ClassWithDisabledTest {

        @Rule
        @ClassRule
        public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

        @Test
        public void testWithDisabledAssertion() throws Exception {
            snapshot.assertThat("xyz").asText().disabled();
        }

        @Test
        public void testWithDisabledAndSuccessfulAssertion() throws Exception {
            snapshot.assertThat("xyz").asText().disabled();
            snapshot.assertThat("xyz").asText().matchesSnapshotText();
        }
    }
}
