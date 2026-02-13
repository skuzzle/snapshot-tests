package de.skuzzle.test.snapshots.data.text;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import de.skuzzle.difftool.DiffRenderer;
import de.skuzzle.difftool.DiffSettings;
import de.skuzzle.difftool.LineSeparator;
import de.skuzzle.difftool.StringDiff;
import de.skuzzle.difftool.UnifiedDiffRenderer;
import de.skuzzle.difftool.thirdparty.DiffUtilsDiffAlgorithm;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.validation.Arguments;

import com.github.difflib.text.DiffRow.Tag;
import com.github.difflib.text.DiffRowGenerator;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Creates a diff of 2 Strings. For internal use only. Public API is provided by
 * {@link TextSnapshot}.
 *
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL, since = "1.4.0")
public final class TextDiff {

    private final Settings settings;
    private final StringDiff diff;

    private TextDiff(Settings settings, StringDiff diff) {
        this.settings = settings;
        this.diff = diff;
    }

    public static TextDiff compare(Settings settings, String expected, String actual) {
        Arguments.requireNonNull(settings != null, "settings must not be null");
        Arguments.requireNonNull(expected, "expected String must not be null");
        Arguments.requireNonNull(actual, "actual String must not be null");

        final StringDiff diff = StringDiff.using(DiffUtilsDiffAlgorithm.create(settings.buildDiffRowGenerator()),
                expected,
                actual);
        return new TextDiff(settings, diff);
    }

    @API(status = Status.INTERNAL, since = "1.7.0")
    public static final class Settings {
        private boolean ignoreWhitespaces = false;
        private int contextLines = SnapshotTestOptions.DEFAULT_CONTEXT_LINES;
        private String inlineOpeningChangeMarker = "<<";
        private String inlineClosingChangeMarker = ">>";
        private DiffRenderer diffRenderer = UnifiedDiffRenderer.INSTANCE;

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

        Consumer<DiffRowGenerator.Builder> buildDiffRowGenerator() {
            return builder -> builder
                    .showInlineDiffs(true)
                    .lineNormalizer(Function.identity())
                    .inlineDiffByWord(true)
                    .ignoreWhiteSpaces(ignoreWhitespaces)
                    .newTag(inlineMarker())
                    .oldTag(inlineMarker());
        }
    }

    private boolean hasLinebreakDifference() {
        return diff.hasLineSeparatorDifference() && !settings.ignoreWhitespaces;
    }

    private boolean hasTextDifference() {
        return diff.hasTextDifference();
    }

    public boolean differencesDetected() {
        return hasLinebreakDifference() || hasTextDifference();
    }

    public String renderDiffWithOffsetAndContextLines(int lineNumberOffset, int contextLines) {
        final StringBuilder result = new StringBuilder();
        final boolean hasTextDifference = hasTextDifference();
        final boolean hasSignificantLinebreakDifference = hasLinebreakDifference();

        if (hasSignificantLinebreakDifference) {
            final LineSeparator expectedLineSeparator = diff.leftLineSeparator();
            final LineSeparator actualLineSeparator = diff.rightLineSeparator();

            result.append("Strings differ in linebreaks. Expected: '")
                    .append(expectedLineSeparator.displayName())
                    .append("', Actual encountered: '").append(actualLineSeparator.displayName()).append("'");

            if (hasTextDifference) {
                result.append(LineSeparator.SYSTEM)
                        .append(LineSeparator.SYSTEM);
            }
        }

        if (hasTextDifference) {
            result.append(diff.toString(settings.diffRenderer,
                    DiffSettings.withDefaultSymbols(contextLines, lineNumberOffset)));
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return renderDiffWithOffsetAndContextLines(0, settings.contextLines);
    }
}
