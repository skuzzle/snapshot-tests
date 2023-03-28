package de.skuzzle.difftool;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LineSeparatorTest {

    @Test
    void testFindLfLineSeparator() {
        final String s = "test\ntest";
        assertThat(LineSeparator.determineFrom(s)).isEqualTo(LineSeparator.LF);
    }

    @Test
    void testFindLfLineSeparatorAtEndOfString() {
        final String s = "test\n";
        assertThat(LineSeparator.determineFrom(s)).isEqualTo(LineSeparator.LF);
    }

    @Test
    void testFinxCrLfLineSeparator() {
        final String s = "test\r\ntest";
        assertThat(LineSeparator.determineFrom(s)).isEqualTo(LineSeparator.CRLF);
    }

    @Test
    void testFinxCrLfLineSeparatorAtEndOfString() {
        final String s = "test\r\n";
        assertThat(LineSeparator.determineFrom(s)).isEqualTo(LineSeparator.CRLF);
    }

    @Test
    void testFindCrLineSeparator() {
        final String s = "test\rtest";
        assertThat(LineSeparator.determineFrom(s)).isEqualTo(LineSeparator.CR);
    }

    @Test
    void testFindCrLineSeparatorAtEndOfString() {
        final String s = "test\r";
        assertThat(LineSeparator.determineFrom(s)).isEqualTo(LineSeparator.CR);
    }
}
