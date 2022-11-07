package de.skuzzle.test.snapshots.data.text;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;

/**
 * Take snapshots using {@link Object#toString()}. By default, whitespace changes of any
 * kind will be ignored during comparison. This behavior can be changed using
 * {@link #withIgnoreWhitespaces(boolean)}.
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public final class TextSnapshot implements StructuredDataProvider {

    private final DiffInterpreter diffInterpreter = new DiffInterpreter();

    /**
     * Take Snapshots using {@link Object#toString()} and compare the results using a
     * generic String diff algorithm. Comparison ignores whitespace changes of any kind.
     */
    public static final StructuredData text = text()
            .withIgnoreWhitespaces(true)
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
     *
     * @param ignoreWhitespaces Whether to ignore whitespaces during comparison.
     * @return This instance.
     */
    @API(status = Status.STABLE, since = "1.4.0")
    public TextSnapshot withIgnoreWhitespaces(boolean ignoreWhitespaces) {
        this.diffInterpreter.withIgnoreWhitespaceChanges(ignoreWhitespaces);
        return this;
    }

    /**
     * Configures the amount of contextual lines that are printed around a detected
     * change. Per default, all lines of the full diff will be printed. If you have huge
     * diffs with only little changes, it might be beneficial to reduce this value to a
     * low value like 5 or 10.
     *
     * @param contextLines The amount of lines to print around a detected change in the
     *            unified diffs.
     * @return This instance.
     * @since 1.5.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.5.0")
    public TextSnapshot withContextLines(int contextLines) {
        this.diffInterpreter.withContextLines(contextLines);
        return this;
    }

    @Override
    public StructuredData build() {
        final StructuralAssertions structuralAssertions = new TextDiffStructuralAssertions(diffInterpreter);
        return StructuredData.with(Object::toString, structuralAssertions);
    }

}
