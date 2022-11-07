package de.skuzzle.test.snapshots.data.text;

import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiffInterpreterTest {

    private String lines(int offset, int n) {
        return IntStream.range(0, n)
                .mapToObj(line -> "line" + (line + offset))
                .collect(joining(LineSeparator.SYSTEM.toString()));
    }

    @Test
    void testContextAsLongAsInput() throws Exception {
        final DiffInterpreter subject = new DiffInterpreter().withContextLines(4);
        final String inputString = lines(0, 4);
        final String formattedString = subject.getDisplayDiffOfEqualDiffBetween2Changes(inputString);

        assertThat(formattedString).isEqualTo(formattedString);
    }

    @Test
    void testtobenamed() throws Exception {
        final DiffInterpreter subject = new DiffInterpreter().withContextLines(3);
        final String inputString = lines(0, 4);
        final String formattedString = subject.getDisplayDiffOfEqualDiffAtTheEnd(inputString);

        final String expectedString = lines(0, 3) + LineSeparator.SYSTEM + "[...]";
        assertThat(formattedString).isEqualTo(expectedString);
    }

    @Test
    void testContextLongerThanInput() throws Exception {
        final DiffInterpreter subject = new DiffInterpreter().withContextLines(10);
        final String inputString = lines(0, 4);
        final String formattedString = subject.getDisplayDiffOfEqualDiffBetween2Changes(inputString);

        assertThat(formattedString).isEqualTo(inputString);
    }

    @ParameterizedTest
    @CsvSource(delimiterString = ",", useHeadersInDisplayName = true, value = {
            "input lines, context lines",
            "6, 2",
            "5, 2",
    })
    void test(int inputLines, int contextLines) {
        final DiffInterpreter subject = new DiffInterpreter().withContextLines(contextLines);
        final String inputString = lines(0, inputLines);

        final String formattedString = subject.getDisplayDiffOfEqualDiffBetween2Changes(inputString);

        final String expectedString = lines(0, contextLines) + LineSeparator.SYSTEM
                + "[...]" + LineSeparator.SYSTEM +
                lines(inputLines - contextLines, contextLines);

        assertThat(formattedString).isEqualTo(expectedString);
    }

}
