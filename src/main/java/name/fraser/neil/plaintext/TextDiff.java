package name.fraser.neil.plaintext;

import java.util.LinkedList;

import name.fraser.neil.plaintext.diff_match_patch.Diff;

public final class TextDiff {

    private final DiffInterpreter diffInterpreter = new DiffInterpreter().withIgnoreWhitespaceChanges(true);
    private final LinkedList<Diff> diffs;

    private TextDiff(LinkedList<Diff> diffs) {
        this.diffs = diffs;
    }

    public static TextDiff diffOf(String expected, String actual) {
        final diff_match_patch diff_match_patch = new diff_match_patch();
        final LinkedList<Diff> diffs = diff_match_patch.diff_main(expected, actual);
        diff_match_patch.diff_cleanupEfficiency(diffs);
        return new TextDiff(diffs);
    }

    public boolean hasDifference() {
        return !diffInterpreter.hasFailures(diffs);
    }

    @Override
    public String toString() {
        final StringBuilder message = new StringBuilder();
        for (final Diff diff : diffs) {
            message.append(diffInterpreter.getDisplayDiff(diff));
        }
        return message.toString();
    }
}
