package de.skuzzle.test.snapshots.data.text;

import java.util.Iterator;
import java.util.List;

import com.github.difflib.text.DiffRow;

final class SplitDiffRenderer implements DiffRenderer {

    private static final String OPERATION_ADD = "+";
    private static final String OPERATION_DELETE = "-";
    private static final String OPERATION_CHANGE_EQUAL = " ";

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
        final int rowNumberWidth = Math.max(2, (int) Math.log10(rowCountEstimate) + 1);

        int expectedLine = 1;
        int actualLine = 1;
        final StringBuilder b = new StringBuilder();
        for (final Iterator<DiffRow> iterator = rows.iterator(); iterator.hasNext();) {
            final DiffRow diffRow = iterator.next();

            switch (diffRow.getTag()) {
            case CHANGE:
            case EQUAL:
                appendRow(b, columnWidth,
                        padLeft("" + expectedLine, rowNumberWidth),
                        OPERATION_CHANGE_EQUAL,
                        diffRow.getOldLine(),
                        padLeft("" + actualLine, rowNumberWidth),
                        OPERATION_CHANGE_EQUAL, diffRow.getNewLine());

                expectedLine++;
                actualLine++;
                break;
            case DELETE:
                appendRow(b, columnWidth,
                        padLeft("" + expectedLine, rowNumberWidth),
                        OPERATION_DELETE,
                        diffRow.getOldLine(),
                        " ".repeat(rowNumberWidth),
                        OPERATION_CHANGE_EQUAL, diffRow.getNewLine());

                expectedLine++;
                break;

            case INSERT:
                appendRow(b, columnWidth,
                        " ".repeat(rowNumberWidth),
                        OPERATION_CHANGE_EQUAL,
                        diffRow.getOldLine(),
                        padLeft("" + actualLine, rowNumberWidth),
                        OPERATION_ADD, diffRow.getNewLine());

                actualLine++;
                break;
            default:
                throw new IllegalStateException();

            }

            if (iterator.hasNext()) {
                b.append(LineSeparator.SYSTEM);
            }
        }

        return b.toString();
    }
}
