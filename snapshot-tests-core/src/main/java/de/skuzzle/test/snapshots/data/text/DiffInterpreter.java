package de.skuzzle.test.snapshots.data.text;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

import de.skuzzle.test.snapshots.data.text.diff_match_patch.Diff;
import de.skuzzle.test.snapshots.data.text.diff_match_patch.Operation;
import de.skuzzle.test.snapshots.validation.Arguments;

final class DiffInterpreter {

    private static final Pattern WHITESPACE_ONLY = Pattern.compile("\\s+");
    private static final Pattern HORIZONTAL_WHITESPACE_ONLY = Pattern.compile("\\h+");

    private boolean ignoreWhitespaceChanges = true;
    private int contextLines = Integer.MAX_VALUE;

    public DiffInterpreter withContextLines(int contextLines) {
        Arguments.check(contextLines >= 0, "contextLines must be > 0 but was %d", contextLines);
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

    private boolean isHorizontalWhitespace(Diff diff) {
        return HORIZONTAL_WHITESPACE_ONLY.matcher(diff.text).matches();
    }

    public boolean hasFailures(Collection<Diff> diffs) {
        return diffs.stream().anyMatch(this::isFailureDifference);
    }

    public int countLinebreaks(Diff diff) {
        final int rawLines = (int) diff.text.lines().count();
        return diff.operation == Operation.DELETE
                ? -rawLines
                : (int) rawLines;
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

    public String renderFailureDiff(Diff diff) {
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
        // invariant: Diff instances always use System line separators
        final String lineSeparator = LineSeparator.SYSTEM.toString();

        if (text.equals(lineSeparator)) {
            return text;
        } else if (text.endsWith(lineSeparator)) {
            return "[" + text.substring(0, text.length() - lineSeparator.length()) + "]" + lineSeparator;
        } else {
            return "[" + text + "]";
        }
    }

    public String renderEqualsDiff(String diffText, EqualDiffPosition position, int lineNumber) {
        return position.render(diffText, contextLines, lineNumber);
    }

    enum EqualDiffPosition {
        START {

            @Override
            protected String render(String diffText, int contextLines, int lineNumber) {
                final int totalLines = (int) diffText.lines().count();

                final StringBuilder b = new StringBuilder();
                boolean appendOnce = true;
                final Iterator<String> lineIterator = diffText.lines().iterator();
                for (int lineNr = 0; lineNr < totalLines; ++lineNr) {
                    final String nextLine = lineIterator.next();

                    if (lineNr >= totalLines - contextLines) {
                        b.append(nextLine);
                        if (lineNr < totalLines - 1 || LineSeparator.SYSTEM.endsWith(diffText)) {
                            b.append(LineSeparator.SYSTEM);
                        }
                    } else if (appendOnce) {
                        b.append("[...]").append(LineSeparator.SYSTEM);
                        appendOnce = false;
                    }
                }
                return b.toString();
            }

        },
        MIDDLE {
            @Override
            protected String render(String diffText, int contextLines, int lineNumber) {
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
        },
        END {
            @Override
            protected String render(String diffText, int contextLines, int lineNumber) {
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
        };

        protected abstract String render(String diffText, int contextLines, int lineNumber);
    }

}
