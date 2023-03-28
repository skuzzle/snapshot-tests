package de.skuzzle.difftool;

import java.util.List;

final class Util {
    private Util() {
        // hidden
    }

    public static int indexOfNextNonEqual(List<DiffLine> fullDiff, int startIndex) {
        for (int i = startIndex; i < fullDiff.size(); ++i) {
            if (fullDiff.get(i).type() != DiffLine.Type.EQUAL) {
                return i;
            }
        }
        return fullDiff.size();
    }

    public static String padLeft(String text, int targetWidth) {
        final int missingSpaces = targetWidth - text.length();
        return " ".repeat(missingSpaces) + text;
    }

    public static String padRight(String text, int targetWidth) {
        final int missingSpaces = targetWidth - text.length();
        return text + " ".repeat(missingSpaces);
    }
}
