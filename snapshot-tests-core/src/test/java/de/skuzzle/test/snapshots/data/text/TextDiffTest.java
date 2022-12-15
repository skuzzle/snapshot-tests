package de.skuzzle.test.snapshots.data.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TextDiffTest {

    private final DiffInterpreter diffInterpreter = new DiffInterpreter();

    @Nested
    class IgnoringWhitespaceChanges {
        {
            diffInterpreter.withIgnoreWhitespaceChanges(true);
        }

        @Test
        void testHasDifferenceFalseIfOnlyWhitespaceChanges() throws Exception {
            final TextDiff diffOf = TextDiff.diffOf(diffInterpreter, "    ", "  ");
            assertThat(diffOf.hasDifference()).isFalse();
        }

        @Test
        void testHasDifferenceFalseIfOnlyLineSeparatorChanges() throws Exception {
            final TextDiff diffOf = TextDiff.diffOf(diffInterpreter, "a\rb", "a\nb");
            assertThat(diffOf.hasDifference()).isFalse();
        }

        @Test
        void testHasDifferenceFalseIfLineSeparatorAdded() throws Exception {
            final TextDiff diffOf = TextDiff.diffOf(diffInterpreter, "ab", "a\nb");
            assertThat(diffOf.hasDifference()).isFalse();
        }

        @Test
        void testHasDifferenceFalseIfLineSeparatorRemoved() throws Exception {
            final TextDiff diffOf = TextDiff.diffOf(diffInterpreter, "a\nb", "ab");
            assertThat(diffOf.hasDifference()).isFalse();
        }

        @Test
        void testDiffOfEmptyStrings() throws Exception {
            final TextDiff diff = TextDiff.diffOf(diffInterpreter, "", "");
            assertThat(diff.toString()).isEmpty();
            assertThat(diff.hasDifference()).isFalse();
        }

        @Nested
        class With5ContextLines {
            {
                diffInterpreter.withContextLines(5);
            }

            @Test
            void testDiffWithHugeEqualBlockAtTheStart() throws Exception {
                final TextDiff diff = TextDiff.diffOf(diffInterpreter,
                        "1\n2\n3\n4\n5\n6\n7\nline1",
                        "1\n2\n3\n4\n5\n6\n7\nlineX");
                assertThat(diff.toString()).isEqualTo(
                        LineSeparator.SYSTEM.convert(
                                "[...]\n4\n5\n6\n7\nline-[1]+[X]"));
                assertThat(diff.hasDifference()).isTrue();
            }

            @Test
            void testDiffWithHugeEqualBlockAtTheEnd() throws Exception {
                final TextDiff diff = TextDiff.diffOf(diffInterpreter,
                        "line1\n1\n2\n3\n4\n5\n6\n7",
                        "lineX\n1\n2\n3\n4\n5\n6\n7");
                assertThat(diff.toString()).isEqualTo(
                        LineSeparator.SYSTEM.convert(
                                "line-[1]+[X]\n1\n2\n3\n4\n[...]"));
                assertThat(diff.hasDifference()).isTrue();
            }

            @Test
            void testDiffWithHugeEqualBlockAtTheStartAndTheEnd() throws Exception {
                final TextDiff diff = TextDiff.diffOf(diffInterpreter,
                        "1\n2\n3\n4\n5\n6\n7\nline1\n8\n9\n10\n11\n12\n13\n14",
                        "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14");
                assertThat(diff.toString()).isEqualTo(
                        LineSeparator.SYSTEM.convert(
                                "[...]\n3\n4\n5\n6\n7\n-[line1]\n8\n9\n10\n11\n12\n[...]"));
                assertThat(diff.hasDifference()).isTrue();
            }

            @Test
            void testDiffWithHugeEqualBlockInTheMiddle() throws Exception {
                final TextDiff diff = TextDiff.diffOf(diffInterpreter,
                        "line1\n1\n2\n3\n4\n5\n6\n7\nline2\n8\n9\n10\n11\n12\n13\n14\nline3",
                        "line2\n1\n2\n3\n4\n5\n6\n7\nline2\n8\n9\n10\n11\n12\n13\n14\nline4");
                assertThat(diff.toString()).isEqualTo(
                        LineSeparator.SYSTEM.convert(
                                "line-[1]+[2]\n1\n2\n3\n4\n[...]\n11\n12\n13\n14\nline-[3]+[4]"));
                assertThat(diff.hasDifference()).isTrue();
            }
        }
    }

    @Nested
    class ObeyingWhitespaceChanges {
        {
            diffInterpreter.withIgnoreWhitespaceChanges(false);
        }

        @Test
        void testHasDifferenceTrueIfOnlyWhitespaceChanges() throws Exception {
            final TextDiff diffOf = TextDiff.diffOf(diffInterpreter, "    ", "  ");
            assertThat(diffOf.hasDifference()).isTrue();
        }

        @Test
        void testHasDifferenceTrueIfOnlyLineSeparatorChanges() throws Exception {
            final TextDiff diffOf = TextDiff.diffOf(diffInterpreter, "a\rb", "a\n\nb");
            assertThat(diffOf.hasDifference()).isTrue();
            assertThat(diffOf.toString()).isEqualTo(LineSeparator.SYSTEM.convert(
                    "Strings differ in linebreaks. Expected: 'CR(\\r)', Actual encountered: 'LF(\\n)'"));
        }

        @Test
        void testHasDifferenceTrueIfLineSeparatorAdded() throws Exception {
            final TextDiff diffOf = TextDiff.diffOf(diffInterpreter, "ab", "a\nb");
            assertThat(diffOf.hasDifference()).isTrue();
            assertThat(diffOf.toString()).isEqualTo(LineSeparator.SYSTEM.convert(
                    "Strings differ in linebreaks. Expected: 'NONE()', Actual encountered: 'LF(\\n)'\n\na+\nb"));
        }

        @Test
        void testHasDifferenceTrueIfLineSeparatorRemoved() throws Exception {
            final TextDiff diffOf = TextDiff.diffOf(diffInterpreter, "a\nb", "ab");
            assertThat(diffOf.hasDifference()).isTrue();
            assertThat(diffOf.toString()).isEqualTo(LineSeparator.SYSTEM.convert(
                    "Strings differ in linebreaks. Expected: 'LF(\\n)', Actual encountered: 'NONE()'\n\na-\nb"));
        }

        @Nested
        class With5ContextLines {
            {
                diffInterpreter.withContextLines(5);
            }

            @Test
            void testDiffOnlyInLinebreaks() throws Exception {
                final TextDiff diff = TextDiff.diffOf(diffInterpreter,
                        "line1\n1\n2\n3\n4\n5\n6\n7", "line1\r1\r2\r3\r4\r5\r6\r7");
                assertThat(diff.toString())
                        .isEqualTo("Strings differ in linebreaks. Expected: 'LF(\\n)', Actual encountered: 'CR(\\r)'");
            }

            @Test
            void testDiffInLinebreaks() throws Exception {
                final TextDiff diff = TextDiff.diffOf(diffInterpreter,
                        "line1\n1\n2\n3\n4\n5\n6\n7", "lineX\r1\r2\r3\r4\r5\r6\r7");

                assertThat(diff.toString()).isEqualTo(LineSeparator.SYSTEM.convert(
                        "Strings differ in linebreaks. Expected: 'LF(\\n)', Actual encountered: 'CR(\\r)'\n\nline-[1]+[X]\r1\r2\r3\r4\r[...]"));
                assertThat(diff.hasDifference()).isTrue();
            }
        }

        @Nested
        class WithMoreContextLinesThanEqualDiffLines {
            {
                diffInterpreter.withContextLines(50);
            }

            @Test
            void testDiffWithLinebreaksAndTrailingLineBreak() throws Exception {
                final TextDiff diff = TextDiff.diffOf(diffInterpreter,
                        "1\n2\n3\n4\n5\n6\n7\nline1\n", "1\n2\n3\n4\n5\n6\n7\nlineX\n");
                assertThat(diff.toString()).isEqualTo(
                        LineSeparator.SYSTEM.convert(
                                "1\n2\n3\n4\n5\n6\n7\nline-[1]+[X]\n"));
                assertThat(diff.hasDifference()).isTrue();
            }

            @Test
            void testDiffWithLinebreaksAndWithoutTrailingLineBreak() throws Exception {
                final TextDiff diff = TextDiff.diffOf(diffInterpreter,
                        "1\n2\n3\n4\n5\n6\n7\nline1", "1\n2\n3\n4\n5\n6\n7\nlineX");
                assertThat(diff.toString()).isEqualTo(
                        LineSeparator.SYSTEM.convert(
                                "1\n2\n3\n4\n5\n6\n7\nline-[1]+[X]"));
                assertThat(diff.hasDifference()).isTrue();
            }
        }
    }

    @Test
    void testSimpleDiffWithNoLinebreaks() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter(),
                "line1line1line1line1line1",
                "line1line1line2line1line1");

        assertThat(diff.toString()).isEqualTo("line1line1line-[1]+[2]line1line1");
    }

}
