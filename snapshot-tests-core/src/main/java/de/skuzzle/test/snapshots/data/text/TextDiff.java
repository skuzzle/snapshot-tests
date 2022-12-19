package de.skuzzle.test.snapshots.data.text;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRow.Tag;
import com.github.difflib.text.DiffRowGenerator;

import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Creates a diff of 2 Strings. For internal use only. Public API is provided by
 * {@link TextSnapshot}.
 *
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL, since = "1.4.0")
public final class TextDiff {

    private final Settings settings;
    private final List<DiffRow> diffRows;
    private final LineSeparator expectedLineSeparator;
    private final LineSeparator actualLineSeparator;

    private TextDiff(Settings settings, List<DiffRow> diffRows, LineSeparator expectedLineSeparator,
            LineSeparator actualLineSeparator) {
        this.settings = settings;
        this.diffRows = diffRows;
        this.expectedLineSeparator = expectedLineSeparator;
        this.actualLineSeparator = actualLineSeparator;
    }

    public static TextDiff compare(Settings settings, String expected, String actual) {
        Arguments.requireNonNull(settings != null, "settings must not be null");
        Arguments.requireNonNull(expected, "expected String must not be null");
        Arguments.requireNonNull(actual, "actual String must not be null");

        final LineSeparator expectedLineSeparator = LineSeparator.determineFrom(expected);
        final LineSeparator actualLineSeparator = LineSeparator.determineFrom(actual);

        final List<DiffRow> diffRows = settings.buildDiffRowGenerator().generateDiffRows(
                expected.lines().collect(toList()),
                actual.lines().collect(toList()));
        return new TextDiff(settings, diffRows, expectedLineSeparator, actualLineSeparator);
    }

    @API(status = Status.INTERNAL, since = "1.7.0")
    public static final class Settings {
        private boolean ignoreWhitespaces = false;
        private int contextLines = SnapshotTestOptions.DEFAULT_CONTEXT_LINES;
        private String inlineOpeningChangeMarker = "<<";
        private String inlineClosingChangeMarker = ">>";
        private DiffRenderer diffRenderer = new UnifiedDiffRenderer();

        private Settings() {
            // hidden
        }

        public static Settings defaultSettings() {
            return new Settings();
        }

        public Settings withIgnoreWhitespaces(boolean ignoreWhitespaces) {
            this.ignoreWhitespaces = ignoreWhitespaces;
            return this;
        }

        public Settings withContextLines(int contextLines) {
            Arguments.check(contextLines >= 0, "contextLines must be a positive integer");
            this.contextLines = contextLines;
            return this;
        }

        public Settings withInlineOpeningChangeMarker(String inlineOpeningChangeMarker) {
            this.inlineOpeningChangeMarker = Arguments.requireNonNull(inlineOpeningChangeMarker,
                    "opening marker must not be null");
            ;
            return this;
        }

        public Settings withInlineClosingChangeMarker(String inlineClosingChangeMarker) {
            this.inlineClosingChangeMarker = Arguments.requireNonNull(inlineClosingChangeMarker,
                    "closing marker must not be null");
            return this;
        }

        public Settings withDiffRenderer(DiffRenderer renderer) {
            this.diffRenderer = Arguments.requireNonNull(renderer, "renderer must not be null");
            return this;
        }

        private BiFunction<Tag, Boolean, String> inlineMarker() {
            return (tag, isOpening) -> {
                if (tag != Tag.CHANGE) {
                    return "";
                }
                return isOpening ? inlineOpeningChangeMarker : inlineClosingChangeMarker;
            };
        }

        private DiffRowGenerator buildDiffRowGenerator() {
            return DiffRowGenerator.create()
                    .showInlineDiffs(true)
                    .lineNormalizer(Function.identity())
                    .inlineDiffByWord(true)
                    .ignoreWhiteSpaces(ignoreWhitespaces)
                    .newTag(inlineMarker())
                    .oldTag(inlineMarker())
                    .build();
        }
    }

    private boolean hasLinebreakDifference() {
        return expectedLineSeparator != actualLineSeparator && !settings.ignoreWhitespaces;
    }

    private boolean hasTextDifference() {
        return diffRows.stream().map(DiffRow::getTag).anyMatch(tag -> tag != Tag.EQUAL);
    }

    public boolean differencesDetected() {
        return hasLinebreakDifference() || hasTextDifference();
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        final boolean hasTextDifference = hasTextDifference();
        final boolean hasLinebreakDifference = hasLinebreakDifference();

        if (hasLinebreakDifference) {
            result.append("Strings differ in linebreaks. Expected: '")
                    .append(expectedLineSeparator.displayName())
                    .append("', Actual encountered: '").append(actualLineSeparator.displayName()).append("'");

            if (hasTextDifference) {
                result.append(LineSeparator.SYSTEM)
                        .append(LineSeparator.SYSTEM);
            }
        }

        if (hasTextDifference) {
            result.append(settings.diffRenderer.renderDiff(diffRows, settings.contextLines));
        }
        return result.toString();
    }
}
