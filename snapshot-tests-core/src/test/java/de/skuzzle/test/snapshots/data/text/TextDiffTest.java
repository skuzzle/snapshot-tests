package de.skuzzle.test.snapshots.data.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TextDiffTest {

    @Test
    void testHasDifferenceFalseIfIgnoreWhitespacesAndOnlyWhitespaceChanges() throws Exception {
        final TextDiff diffOf = TextDiff.diffOf(new DiffInterpreter().withIgnoreWhitespaceChanges(true), "    ", "  ");
        assertThat(diffOf.hasDifference()).isFalse();
    }

    @Test
    void testHasDifferenceFalseIfIgnoreWhitespacesAndOnlyLineSeparatorChanges() throws Exception {
        final TextDiff diffOf = TextDiff.diffOf(new DiffInterpreter().withIgnoreWhitespaceChanges(true), "a\rb",
                "a\nb");
        assertThat(diffOf.hasDifference()).isFalse();
    }

    @Test
    void testDiffOfEmptyStrings() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter(), "", "");
        assertThat(diff.toString()).isEmpty();
        assertThat(diff.hasDifference()).isFalse();
    }

    @Test
    void testDiffWithLinebreaksAndLessThanContextLinesWithTrailingLineBreak() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter().withIgnoreWhitespaceChanges(false)
                .withContextLines(50),
                "1\n2\n3\n4\n5\n6\n7\nline1\n", "1\n2\n3\n4\n5\n6\n7\nlineX\n");
        assertThat(diff.toString()).isEqualTo(
                LineSeparator.SYSTEM.convert(
                        "1\n2\n3\n4\n5\n6\n7\nline-[1]+[X]\n"));
        assertThat(diff.hasDifference()).isTrue();
    }

    @Test
    void testDiffWithLinebreaksAndLessThanContextLinesWithoutTrailingLineBreak() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter().withIgnoreWhitespaceChanges(false)
                .withContextLines(50),
                "1\n2\n3\n4\n5\n6\n7\nline1", "1\n2\n3\n4\n5\n6\n7\nlineX");
        assertThat(diff.toString()).isEqualTo(
                LineSeparator.SYSTEM.convert(
                        "1\n2\n3\n4\n5\n6\n7\nline-[1]+[X]"));
        assertThat(diff.hasDifference()).isTrue();
    }

    @Test
    void testDiffOnlyInLinebreaks() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter()
                .withContextLines(5)
                .withIgnoreWhitespaceChanges(false),
                "line1\n1\n2\n3\n4\n5\n6\n7", "line1\r1\r2\r3\r4\r5\r6\r7");
        assertThat(diff.toString())
                .isEqualTo("Strings differ in linebreaks. Expected: 'LF(\\n)', Actual encountered: 'CR(\\r)'");
    }

    @Test
    void testDiffInLinebreaks() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter()
                .withContextLines(5)
                .withIgnoreWhitespaceChanges(false),
                "line1\n1\n2\n3\n4\n5\n6\n7", "lineX\r1\r2\r3\r4\r5\r6\r7");

        assertThat(diff.toString())
                .isEqualTo(LineSeparator.SYSTEM.convert(
                        "Strings differ in linebreaks. Expected: 'LF(\\n)', Actual encountered: 'CR(\\r)'\n\nline-[1]+[X]\r1\r2\r3\r4\r[...]"));
        assertThat(diff.hasDifference()).isTrue();
    }

    @Test
    void testDiffWithHugeEqualBlockAtTheStart() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter().withContextLines(5),
                "1\n2\n3\n4\n5\n6\n7\nline1", "1\n2\n3\n4\n5\n6\n7\nlineX");
        assertThat(diff.toString()).isEqualTo(
                LineSeparator.SYSTEM.convert(
                        "[...]\n4\n5\n6\n7\nline-[1]+[X]"));
        assertThat(diff.hasDifference()).isTrue();
    }

    @Test
    void testDiffWithHugeEqualBlockAtTheEnd() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter().withContextLines(5),
                "line1\n1\n2\n3\n4\n5\n6\n7", "lineX\n1\n2\n3\n4\n5\n6\n7");
        assertThat(diff.toString()).isEqualTo(
                LineSeparator.SYSTEM.convert(
                        "line-[1]+[X]\n1\n2\n3\n4\n[...]"));
        assertThat(diff.hasDifference()).isTrue();
    }

    @Test
    void testDiffWithHugeEqualBlockAtTheStartAndTheEnd() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter().withContextLines(5),
                "1\n2\n3\n4\n5\n6\n7\nline1\n8\n9\n10\n11\n12\n13\n14",
                "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14");
        assertThat(diff.toString()).isEqualTo(
                LineSeparator.SYSTEM.convert(
                        "[...]\n3\n4\n5\n6\n7\n-[line1]\n8\n9\n10\n11\n12\n[...]"));
        assertThat(diff.hasDifference()).isTrue();
    }

    @Test
    void testDiffWithHugeEqualBlockInTheMiddle() throws Exception {
        final TextDiff diff = TextDiff.diffOf(new DiffInterpreter().withContextLines(5),
                "line1\n1\n2\n3\n4\n5\n6\n7\nline2\n8\n9\n10\n11\n12\n13\n14\nline3",
                "line2\n1\n2\n3\n4\n5\n6\n7\nline2\n8\n9\n10\n11\n12\n13\n14\nline4");
        assertThat(diff.toString()).isEqualTo(
                LineSeparator.SYSTEM.convert(
                        "line-[1]+[2]\n1\n2\n3\n4\n[...]\n11\n12\n13\n14\nline-[3]+[4]"));
        assertThat(diff.hasDifference()).isTrue();
    }
}
