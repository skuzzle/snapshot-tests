package de.skuzzle.test.snapshots.data.text;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.data.text.DiffInterpreter.EqualDiffPosition;
import de.skuzzle.test.snapshots.data.text.diff_match_patch.Diff;
import de.skuzzle.test.snapshots.data.text.diff_match_patch.Operation;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Creates a unified diff of 2 Strings using the popular Neil Fraser diff_match_patch
 * implementation under the hood.
 *
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL, since = "1.4.0")
public final class TextDiff {

    private final DiffInterpreter diffInterpreter;
    private final List<Diff> diffs;
    private final LineSeparator expectedLineSeparator;
    private final LineSeparator actualLineSeparator;

    private TextDiff(DiffInterpreter diffInterpreter, List<Diff> diffs,
            LineSeparator expectedLineSeparator, LineSeparator actualLineSeparator) {
        this.diffInterpreter = diffInterpreter;
        this.diffs = diffs;
        this.expectedLineSeparator = expectedLineSeparator;
        this.actualLineSeparator = actualLineSeparator;
    }

    public static TextDiff diffOf(String expected, String actual, int contextLines) {
        return diffOf(new DiffInterpreter()
                .withIgnoreWhitespaceChanges(false)
                .withContextLines(contextLines),
                expected, actual);
    }

    static TextDiff diffOf(DiffInterpreter diffInterpreter, String expected, String actual) {
        Arguments.requireNonNull(expected, "expected String must not be null");
        Arguments.requireNonNull(actual, "actual String must not be null");

        final LineSeparator expectedLineSeparator = LineSeparator.determineFrom(expected);
        final LineSeparator actualLineSeparator = LineSeparator.determineFrom(actual);

        final diff_match_patch diff_match_patch = new diff_match_patch();

        final LinkedList<Diff> diffs = diff_match_patch.diff_main(
                sanitizeLineSeparators(expected),
                sanitizeLineSeparators(actual));
        diff_match_patch.diff_cleanupSemanticLossless(diffs);

        return new TextDiff(diffInterpreter, diffs, expectedLineSeparator, actualLineSeparator);
    }

    private static String sanitizeLineSeparators(String s) {
        return LineSeparator.SYSTEM.convert(s);
    }

    public boolean hasDifference() {
        return diffInterpreter.hasFailures(diffs)
                || diffInterpreter.hasLineSeparatorDifference(expectedLineSeparator, actualLineSeparator);
    }

    @Override
    public String toString() {
        if (diffs.isEmpty()) {
            return "";
        }

        final StringBuilder message = new StringBuilder();

        if (diffInterpreter.hasLineSeparatorDifference(expectedLineSeparator, actualLineSeparator)) {
            message.append(String.format(
                    "Strings differ in linebreaks. Expected: '%s', Actual encountered: '%s'",
                    expectedLineSeparator.displayName(), actualLineSeparator.displayName()));

            if (diffs.size() == 1 && diffs.get(0).operation == Operation.EQUAL) {
                return message.toString();
            }

            message.append(LineSeparator.SYSTEM).append(LineSeparator.SYSTEM);
        }

        final ListIterator<Diff> cursor = diffs.listIterator();
        int lineNr = 1;
        while (cursor.hasNext()) {
            final boolean isBeginning = cursor.hasPrevious();

            final Diff current = cursor.next();

            if (current.operation == Operation.EQUAL) {
                if (!isBeginning) {
                    // equal operation at the beginning of the whole diff
                    message.append(diffInterpreter.renderEqualsDiff(current.text, EqualDiffPosition.START, lineNr));
                } else if (cursor.hasNext()) {
                    // equal operation between 2 changes
                    message.append(diffInterpreter.renderEqualsDiff(current.text, EqualDiffPosition.MIDDLE, lineNr));
                } else {
                    // equal diff at the end
                    message.append(diffInterpreter.renderEqualsDiff(current.text, EqualDiffPosition.END, lineNr));
                }
            } else {
                message.append(diffInterpreter.renderFailureDiff(current));
            }

            lineNr += diffInterpreter.countLinebreaks(current);
        }
        return message.toString();
    }
}
