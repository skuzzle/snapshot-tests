package de.skuzzle.test.snapshots.data.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TextDiffTest {

    @Test
    void testDiffOfEmptyStrings() throws Exception {
        final TextDiff diff = TextDiff.diffOf("", "");
        assertThat(diff.toString()).isEmpty();
    }

    @Test
    void testDiffOnlyInLinebreaks() throws Exception {
        final TextDiff diff = TextDiff.diffOf("line1\n1\n2\n3\n4\n5\n6\n7", "line1\r1\r2\r3\r4\r5\r6\r7");
        assertThat(diff.toString())
                .isEqualTo("Strings differ in linebreaks. Expected: 'LF(\\n)', Actual encountered: 'CR(\\r)'");
    }

    @Test
    void testDiffInLinebreaks() throws Exception {
        final TextDiff diff = TextDiff.diffOf("line1\n1\n2\n3\n4\n5\n6\n7", "lineX\r1\r2\r3\r4\r5\r6\r7");
        assertThat(diff.toString())
                .isEqualTo(LineSeparator.SYSTEM.convert(
                        "Strings differ in linebreaks. Expected: 'LF(\\n)', Actual encountered: 'CR(\\r)'\n\nline-[1]+[X]\r1\r2\r3\r4\r[...]"));
    }

    @Test
    void testDiffWithHugeEqualBlockAtTheEnd() throws Exception {
        final TextDiff diff = TextDiff.diffOf("line1\n1\n2\n3\n4\n5\n6\n7", "lineX\n1\n2\n3\n4\n5\n6\n7");
        assertThat(diff.toString()).isEqualTo(
                LineSeparator.SYSTEM.convert(
                        "line-[1]+[X]\n1\n2\n3\n4\n[...]"));
    }
}
