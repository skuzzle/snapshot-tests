package de.skuzzle.test.snapshots.data.text;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.data.text.TextDiff.Settings;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class TextDiffTest {

    private String join(String... lines) {
        return Arrays.stream(lines).collect(Collectors.joining("\n"));
    }

    private final Settings settings = Settings.defaultSettings();

    @Nested
    class WithIgnoreWhitespaces {
        {
            settings.withIgnoreWhitespaces(true);
        }

        @Test
        void testSingleLineRemoved() throws Exception {
            final TextDiff textDiff = TextDiff.compare(settings, "Just a single line", "");
            assertThat(textDiff.changesDetected()).isTrue();
            assertThat(textDiff.toString()).isEqualTo("  1    - Just a single line");
        }

        @Test
        void testSingleLineAdded() throws Exception {
            final TextDiff textDiff = TextDiff.compare(settings, "", "Just a single line");
            assertThat(textDiff.changesDetected()).isTrue();
            assertThat(textDiff.toString()).isEqualTo("     1 + Just a single line");
        }

        @Test
        void testLinebreakChangeIgnoreWhitespaces() throws Exception {
            final TextDiff textDiff = TextDiff.compare(settings, "line1\nline2", "line1\r\nline2");
            assertThat(textDiff.changesDetected()).isFalse();
            assertThat(textDiff.toString()).isEqualTo("");
        }
    }

    @Nested
    class WithObeyWhitespaces {
        {
            settings.withIgnoreWhitespaces(false);
        }

        @Test
        void testSingleLineRemoved() throws Exception {
            final TextDiff textDiff = TextDiff.compare(settings, "Just a single line", "");
            assertThat(textDiff.changesDetected()).isTrue();
            assertThat(textDiff.toString()).isEqualTo("  1    - Just a single line");
        }

        @Test
        void testSingleLineAdded() throws Exception {
            final TextDiff textDiff = TextDiff.compare(settings, "", "Just a single line");
            assertThat(textDiff.changesDetected()).isTrue();
            assertThat(textDiff.toString()).isEqualTo("     1 + Just a single line");
        }

        @Test
        void testWhitespaceChangeWithinSingleLine() throws Exception {
            final TextDiff textDiff = TextDiff.compare(settings,
                    "Just a single     line", "Just a single line");
            assertThat(textDiff.changesDetected()).isTrue();
            assertThat(textDiff.toString()).isEqualTo(LineSeparator.SYSTEM.convert(""
                    + "  1    - Just a single<<     >>line\n"
                    + "     1 + Just a single<< >>line"));
        }

        @Test
        void testLinebreakChangeObeyWhitespaces() throws Exception {
            final TextDiff textDiff = TextDiff.compare(settings,
                    "line1\nline2", "line1\r\nline2");
            assertThat(textDiff.changesDetected()).isTrue();
            assertThat(textDiff.toString())
                    .isEqualTo("Strings differ in linebreaks. Expected: 'LF(\\n)', Actual encountered: 'CRLF(\\r\\n)'");
        }
    }

    @Test
    void testRenderDiff(Snapshot snapshot) throws Exception {
        final String expected = join(
                "Some unchanged lines1",
                "Some unchanged lines2",
                "Some unchanged lines3",
                "Some unchanged lines4",
                "Some unchanged lines5",
                "Some unchanged lines6",
                "Some unchanged lines7",
                "Some unchanged lines8",
                "This is a test senctence.",
                "This is the second line.",
                "Some unchanged lines9",
                "Some unchanged lines10",
                "Some unchanged lines11",
                "Some unchanged lines12",
                "And here is the finish with way more than 80 characters and I'm very curious how this is going to be displayed in split view diff.",
                "This line is unchanged",
                "Some unchanged lines13",
                "Some unchanged lines14",
                "Some unchanged lines15",
                "Some unchanged lines16",
                "Some unchanged lines17",
                "Some unchanged lines18",
                "Some unchanged lines19",
                "Some unchanged lines20",
                "Another difference",
                "Some unchanged lines21",
                "Some unchanged lines22",
                "Some unchanged lines23",
                "Some unchanged lines24");

        final String actual = join(
                "Some unchanged lines1",
                "Some unchanged lines2",
                "Some unchanged lines3",
                "Some unchanged lines4",
                "Some unchanged lines5",
                "Some unchanged lines6",
                "Some unchanged lines7",
                "Some unchanged lines8",
                "This is a test for diffutils.",
                "This is the second line.",
                "Some unchanged lines9",
                "Some unchanged lines10",
                "Some unchanged lines11",
                "Some unchanged lines12",
                "This line is unchanged",
                "This line has been added",
                "Some unchanged lines13",
                "Some unchanged lines14",
                "Some unchanged lines15",
                "Some unchanged lines16",
                "Some unchanged lines17",
                "Some unchanged lines18",
                "Some unchanged lines19",
                "Some unchanged lines20",
                "This has changed",
                "Some unchanged lines21",
                "Some unchanged lines22",
                "Some unchanged lines23",
                "Some unchanged lines24");

        final TextDiff textDiff = TextDiff.compare(Settings.defaultSettings().withContextLines(3), expected, actual);

        System.out.println(textDiff);
        snapshot.assertThat(textDiff).asText().matchesSnapshotText();
    }
}
