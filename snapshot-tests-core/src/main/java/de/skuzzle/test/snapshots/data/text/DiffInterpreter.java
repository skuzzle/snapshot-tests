package de.skuzzle.test.snapshots.data.text;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import de.skuzzle.test.snapshots.data.text.diff_match_patch.Diff;
import de.skuzzle.test.snapshots.data.text.diff_match_patch.Operation;

final class DiffInterpreter {

    private static final Pattern WHITESPACE_ONLY = Pattern.compile("\\s+");

    private boolean ignoreWhitespaceChanges = true;
    private int contextLines = Integer.MAX_VALUE;

    public DiffInterpreter withContextLines(int contextLines) {
        this.contextLines = contextLines;
        return this;
    }

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
        if (diff.operation == Operation.EQUAL) {
            return false;
        }
        if (isWhitespace(diff)) {
            return !ignoreWhitespaceChanges;
        }
        return true;
    }

    boolean hasLineSeparatorDifference(LineSeparator l1, LineSeparator l2) {
        if (ignoreWhitespaceChanges) {
            return false;
        }
        return l1 != l2;
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

    public String getDisplayDiffOfEqualDiffAtTheStart(String diffText) {
        final int totalLines = (int) diffText.lines().count();

        final StringBuilder b = new StringBuilder();
        boolean appendOnce = true;
        final Iterator<String> lineIterator = diffText.lines().iterator();
        for (int lineNr = 0; lineNr < totalLines; ++lineNr) {
            final String nextLine = lineIterator.next();

            if (lineNr >= totalLines - contextLines) {
                b.append(nextLine);
                if (lineNr < totalLines - 1) {
                    b.append(LineSeparator.SYSTEM);
                }
            } else if (appendOnce) {
                b.append("[...]").append(LineSeparator.SYSTEM);
                appendOnce = false;
            }
        }
        return b.toString();
    }

    public String getDisplayDiffOfEqualDiffBetween2Changes(String diffText) {
        final int totalLines = (int) diffText.lines().count();

        final StringBuilder b = new StringBuilder();
        boolean appendOnce = true;
        final Iterator<String> lineIterator = diffText.lines().iterator();
        for (int lineNr = 0; lineNr < totalLines; ++lineNr) {
            final String nextLine = lineIterator.next();

            if (lineNr < contextLines || lineNr >= totalLines - contextLines) {
                b.append(nextLine);
                if (lineNr < totalLines - 1) {
                    b.append(LineSeparator.SYSTEM);
                }
            } else if (appendOnce) {
                b.append("[...]").append(LineSeparator.SYSTEM);
                appendOnce = false;
            }
        }
        return b.toString();
    }

    public String getDisplayDiffOfEqualDiffAtTheEnd(String diffText) {
        final int totalLines = (int) diffText.lines().count();

        final StringBuilder b = new StringBuilder();
        boolean appendOnce = true;
        final Iterator<String> lineIterator = diffText.lines().iterator();
        for (int lineNr = 0; lineNr < totalLines; ++lineNr) {
            final String nextLine = lineIterator.next();

            if (lineNr < contextLines) {
                b.append(nextLine);
                if (lineNr < totalLines - 1) {
                    b.append(LineSeparator.SYSTEM);
                }
            } else if (appendOnce) {
                b.append("[...]");
                appendOnce = false;
            }
        }
        return b.toString();
    }

    private String einklammern(String text) {
        final String lineSeparator = LineSeparator.SYSTEM.toString();

        if (text.endsWith(lineSeparator)) {
            return "[" + text.substring(0, text.length() - lineSeparator.length()) + "]" + lineSeparator;
        } else {
            return "[" + text + "]";
        }
    }
}
