package de.skuzzle.test.snapshots.impl;

import de.skuzzle.difftool.LineSeparator;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotTestOptions.NormalizeLineEndings;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Serializer that wraps another serializer and normalizes the line endings in the output
 * of the delegate serializer.
 *
 * @since 1.10.0
 */
final class NormalizeLineEndingsSnapshotSerializer implements SnapshotSerializer {

    private final SnapshotSerializer delegate;
    private final NormalizeLineEndings normalizeLineEndings;

    private NormalizeLineEndingsSnapshotSerializer(SnapshotSerializer delegate,
            NormalizeLineEndings normalizeLineEndings) {
        this.delegate = Arguments.requireNonNull(delegate, "delegate serializer must not be null");
        this.normalizeLineEndings = Arguments.requireNonNull(normalizeLineEndings,
                "normalizeLineEndings must not be null");
    }

    static SnapshotSerializer wrap(SnapshotSerializer serializer, NormalizeLineEndings normalizeLineEndings) {
        return new NormalizeLineEndingsSnapshotSerializer(serializer, normalizeLineEndings);
    }

    @Override
    public String serialize(Object testResult) throws SnapshotException {
        final String result = delegate.serialize(testResult);
        if (normalizeLineEndings == NormalizeLineEndings.NEVER) {
            return result;
        }
        final LineSeparator lineSeparator = determineLineSeparator();
        return lineSeparator.convert(result);
    }

    private LineSeparator determineLineSeparator() {
        switch (normalizeLineEndings) {
        case SYSTEM:
            return LineSeparator.SYSTEM;
        case CRLF:
            return LineSeparator.CRLF;
        case LF:
            return LineSeparator.LF;
        case GIT:
            return LineSeparator.determineFromGitConfig();
        }
        throw new IllegalStateException("Could not determine LineSeparator for: " + normalizeLineEndings);
    }
}
