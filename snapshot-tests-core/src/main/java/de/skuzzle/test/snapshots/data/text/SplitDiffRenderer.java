package de.skuzzle.test.snapshots.data.text;

import java.util.List;

import com.github.difflib.text.DiffRow;

final class SplitDiffRenderer implements DiffRenderer {

    private static final String OPERATION_CHANGE = "!";
    private static final String OPERATION_ADD = "+";
    private static final String OPERATION_DELETE = "-";
    private static final String OPERATION_EQUAL = " ";

    private String padLeft(String text, int targetWidth) {
        final int missingSpaces = targetWidth - text.length();
        return " ".repeat(missingSpaces) + text;
    }

    private String padRight(String text, int targetWidth) {
        final int missingSpaces = targetWidth - text.length();
        return text + " ".repeat(missingSpaces);
    }

    private void appendRow(StringBuilder b, int columnWidth, String paddedExpectedLineNumber, String expectedOperation,
            String expectedLine, String paddedActualLineNumber, String actualOperation, String actualLine) {
        b.append(paddedExpectedLineNumber)
                .append(" ")
                .append(expectedOperation)
                .append(" ")
                .append(padRight(expectedLine, columnWidth))
                .append(" |")
                .append(paddedActualLineNumber)
                .append(" ").append(actualOperation).append(" ").append(actualLine);

    }

    @Override
    public String renderDiff(List<DiffRow> rows, int contextLines) {
        final int columnWidth = rows.stream().map(DiffRow::getOldLine).mapToInt(String::length).max().orElse(80);
        final int rowCountEstimate = rows.size();
        final int rowNumberWidth = Math.max(2, (int) Math.log10(rowCountEstimate) + 1) + 1;

        int expectedLine = 1;
        int actualLine = 1;
        final StringBuilder b = new StringBuilder();

        int indexOfLastDifference = -1;

        for (int i = 0; i < rows.size(); i++) {
            final DiffRow diffRow = rows.get(i);

            switch (diffRow.getTag()) {
            case CHANGE:
                indexOfLastDifference = i;
                appendRow(b, columnWidth,
                        padLeft("" + expectedLine, rowNumberWidth),
                        OPERATION_CHANGE,
                        diffRow.getOldLine(),
                        padLeft("" + actualLine, rowNumberWidth),
                        OPERATION_CHANGE, diffRow.getNewLine());

                expectedLine++;
                actualLine++;
                break;
            case EQUAL:
                final int indexOfNextDifference = DiffListLookahead.indexOfNextNonEqual(rows, i);

                final int distanceToNextDifference = indexOfNextDifference == rows.size()
                        ? Integer.MAX_VALUE
                        : indexOfNextDifference - i - 1;
                final int distanceToPrevDifference = indexOfLastDifference == -1
                        ? Integer.MAX_VALUE
                        : i - indexOfLastDifference - 1;

                if (distanceToNextDifference < contextLines || distanceToPrevDifference < contextLines) {
                    appendRow(b, columnWidth,
                            padLeft("" + expectedLine, rowNumberWidth),
                            OPERATION_EQUAL,
                            diffRow.getOldLine(),
                            padLeft("" + actualLine, rowNumberWidth),
                            OPERATION_EQUAL, diffRow.getNewLine());

                } else if (distanceToNextDifference == contextLines || distanceToPrevDifference == contextLines) {
                    b.append("[...]");
                } else {
                    expectedLine++;
                    actualLine++;
                    // continue here to not print extra linebreaks for skipped lines
                    continue;
                }

                expectedLine++;
                actualLine++;
                break;
            case DELETE:
                indexOfLastDifference = i;

                appendRow(b, columnWidth,
                        padLeft("" + expectedLine, rowNumberWidth),
                        OPERATION_DELETE,
                        diffRow.getOldLine(),
                        " ".repeat(rowNumberWidth),
                        OPERATION_EQUAL, diffRow.getNewLine());

                expectedLine++;
                break;

            case INSERT:
                indexOfLastDifference = i;

                appendRow(b, columnWidth,
                        " ".repeat(rowNumberWidth),
                        OPERATION_EQUAL,
                        diffRow.getOldLine(),
                        padLeft("" + actualLine, rowNumberWidth),
                        OPERATION_ADD, diffRow.getNewLine());

                actualLine++;
                break;
            default:
                throw new IllegalStateException();

            }

            if (i < rows.size() - 1) {
                b.append(LineSeparator.SYSTEM);
            }
        }

        return b.toString();
    }
}
