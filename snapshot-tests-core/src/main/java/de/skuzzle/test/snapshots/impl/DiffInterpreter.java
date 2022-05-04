package de.skuzzle.test.snapshots.impl;

import java.util.Collection;
import java.util.regex.Pattern;

import de.skuzzle.test.snapshots.impl.diff_match_patch.Diff;
import de.skuzzle.test.snapshots.impl.diff_match_patch.Operation;

final class DiffInterpreter {

    private static final Pattern WHITESPACE_ONLY = Pattern.compile("\\s+");
    private boolean ignoreWhitespaceChanges = false;

    public DiffInterpreter withIgnoreWhitespaceChanges(boolean ignoreWhitespaceChanges) {
        this.ignoreWhitespaceChanges = ignoreWhitespaceChanges;
        return this;
    }

    private boolean isWhitespace(Diff diff) {
        return WHITESPACE_ONLY.matcher(diff.text).matches();
    }

    public boolean hasFailures(Collection<Diff> diffs) {
        return diffs.stream().anyMatch(this::isFailureDifference);
    }

    private boolean isFailureDifference(Diff diff) {
        return diff.operation != Operation.EQUAL
                && (!ignoreWhitespaceChanges || !isWhitespace(diff));
    }

    public String getDisplayDiff(Diff diff) {
        if (isFailureDifference(diff)) {
            switch (diff.operation) {
            case DELETE:
                return "-" + einklammern(diff.text);
            case INSERT:
                return "+" + einklammern(diff.text);
            default:
                throw new IllegalStateException();
            }
        } else {
            return diff.text;
        }
    }

    private String einklammern(String text) {
        if (text.endsWith("\r\n")) {
            return "[" + text.substring(0, text.length() - 2) + "]\r\n";
        } else if (text.endsWith("\r")) {
            return "[" + text.substring(0, text.length() - 1) + "]\r";
        } else if (text.endsWith("\n")) {
            return "[" + text.substring(0, text.length() - 1) + "]\n";
        } else {
            return "[" + text + "]";
        }
    }
}
