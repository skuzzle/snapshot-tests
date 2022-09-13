package de.skuzzle.test.snapshots.data.text;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;

/**
 * Take snapshots using {@link Object#toString()}. By default, whitespace changes will be
 * ignored during comparison. This behavior can be changed using
 * {@link #withIgnoreWhitespaces(boolean)}.
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public final class TextSnapshot implements StructuredDataProvider {

    private final DiffInterpreter diffInterpreter = new DiffInterpreter();

    /**
     * Take Snapshots using {@link Object#toString()} and compare the results using a
     * generic String diff algorithm. Comparison ignores whitespace changes.
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

    @Override
    public StructuredData build() {
        final StructuralAssertions structuralAssertions = new TextDiffStructuralAssertions(diffInterpreter);
        return StructuredData.with(Object::toString, structuralAssertions);
    }

}
