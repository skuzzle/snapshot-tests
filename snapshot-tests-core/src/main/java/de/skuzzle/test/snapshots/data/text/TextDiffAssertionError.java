package de.skuzzle.test.snapshots.data.text;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Internal exception that is used to communicate the {@link TextDiff} instance created by
 * the {@link TextDiffStructuralAssertions} to the snapshot test.
 *
 * @author Simon Taddiken
 * @since 1.5.0
 */
@API(status = Status.INTERNAL, since = "1.5.0")
public final class TextDiffAssertionError extends AssertionError {

    private static final long serialVersionUID = -8116699092500765619L;

    private final TextDiff textDiff;

    TextDiffAssertionError(String message, TextDiff textDiff) {
        super(message);
        this.textDiff = Arguments.requireNonNull(textDiff, "textDiff must not be null");
    }

    public TextDiff textDiff() {
        return textDiff;
    }

}
