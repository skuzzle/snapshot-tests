package de.skuzzle.test.snapshots.data.text;

import de.skuzzle.difftool.DiffRenderer;
import de.skuzzle.difftool.SplitDiffRenderer;
import de.skuzzle.difftool.UnifiedDiffRenderer;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;
import de.skuzzle.test.snapshots.data.text.TextDiff.Settings;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Take snapshots using {@link Object#toString()}. By default, whitespace changes of any
 * kind will be ignored during comparison. This behavior can be changed using
 * {@link #withIgnoreWhitespaces(boolean)}.
 * <p>
 * When rendering unified diffs as String, changes in line separators are not marked at
 * every occurrence. Instead, detected changes in line separators will result in a single
 * informative message at the beginning of the diff, but only if set
 * {@link #withIgnoreWhitespaces(boolean)} to false.
 * <p>
 * By default, rendered diffs will display 5 lines of context around a detected change.
 * This can be changed using {@link #withContextLines(int)}. Note that
 * {@link SnapshotTestOptions#textDiffContextLines()} does not apply to text snapshots.
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public final class TextSnapshot implements StructuredDataProvider {

    private final Settings settings = Settings.defaultSettings();

    /**
     * Take Snapshots using {@link Object#toString()} and compare the results using a
     * generic String diff algorithm. Comparison ignores whitespace changes of any kind.
     * <p>
     * You can create a more customized text comparison by using {@link #text()} and the
     * several customization options.
     *
     * @see #text()
     */
    public static final StructuredData text = text()
            .withIgnoreWhitespaces(true)
            .withContextLines(Integer.MAX_VALUE)
            .build();

    private TextSnapshot() {
        // hidden
    }

    /**
     * Creates a customizable {@link TextSnapshot} instance. You can also use the static
     * constant {@link #text} when you want to use only defaults.
     *
     * @return The customizable text snapshot {@link StructuredDataProvider}.
     * @since 1.4.0
     */
    @API(status = Status.STABLE, since = "1.4.0")
    public static TextSnapshot text() {
        return new TextSnapshot();
    }

    /**
     * Allows to customize the whitespace comparison behavior.
     * <p>
     * Defaults to false.
     *
     * @param ignoreWhitespaces Whether to ignore whitespaces during comparison.
     * @return This instance.
     */
    @API(status = Status.STABLE, since = "1.4.0")
    public TextSnapshot withIgnoreWhitespaces(boolean ignoreWhitespaces) {
        settings.withIgnoreWhitespaces(ignoreWhitespaces);
        return this;
    }

    /**
     * Configures the amount of contextual lines that are printed around a detected
     * change. Per default, all lines of the full diff will be printed.
     * <p>
     * Defaults to 5
     *
     * @param contextLines The amount of lines to print around a detected change in the
     *            unified diffs.
     * @return This instance.
     * @since 1.5.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.5.0")
    public TextSnapshot withContextLines(int contextLines) {
        settings.withContextLines(contextLines);
        return this;
    }

    /**
     * Specify the format of how diffs are rendered within our assertion failure messages.
     * <p>
     * Defaults to {@link DiffFormat#UNIFIED}
     *
     * @param diffFormat The diff format to use.
     * @return This instance.
     * @since 1.7.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.7.0")
    public TextSnapshot withDiffFormat(DiffFormat diffFormat) {
        settings.withDiffRenderer(diffFormat.renderer);
        return this;
    }

    /**
     * Format in which diffs will be rendered within assertion failure messages.
     *
     * @author Simon Taddiken
     * @since 1.7.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.7.0")
    public enum DiffFormat {
        /**
         * Render diffs in unified format, where changes are displayed line by line and
         * lines from expected and actual results are printed interleaved.
         */
        UNIFIED(UnifiedDiffRenderer.INSTANCE),
        /**
         * Render diff in split view format, where the lines from the expected and actual
         * result will be printed side by side.
         */
        SPLIT(SplitDiffRenderer.INSTANCE);

        private final DiffRenderer renderer;

        private DiffFormat(DiffRenderer renderer) {
            this.renderer = renderer;
        }
    }

    @Override
    public StructuredData build() {
        final StructuralAssertions structuralAssertions = new TextDiffStructuralAssertions(settings);
        return StructuredData.with(Object::toString, structuralAssertions);
    }

}
