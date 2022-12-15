package de.skuzzle.test.snapshots.data.text;

import java.util.List;

import com.github.difflib.text.DiffRow;

final class UnifiedDiffRenderer implements DiffRenderer {

    private String padLeft(String text, int targetWidth) {
        final int missingSpaces = targetWidth - text.length();
        return " ".repeat(missingSpaces) + text;
    }

    @Override
    public String renderDiff(List<DiffRow> rows, int contextLines) {
        final int rowCountEstimate = rows.size();
        final int rowNumberWidth = Math.max(2, (int) Math.log10(rowCountEstimate) + 1) + 1;

        int expectedLine = 1;
        int actualLine = 1;
        final StringBuilder b = new StringBuilder();

        int indexOfLastDifference = -1;
        for (int i = 0; i < rows.size(); i++) {
            final DiffRow diffRow = rows.get(i);

            switch (diffRow.getTag()) {
            case EQUAL:
                final int indexOfNextDifference = DiffListLookahead.indexOfNextNonEqual(rows, i);

                final int distanceToNextDifference = indexOfNextDifference == rows.size()
                        ? Integer.MAX_VALUE
                        : indexOfNextDifference - i - 1;
                final int distanceToPrevDifference = indexOfLastDifference == -1
                        ? Integer.MAX_VALUE
                        : i - indexOfLastDifference - 1;

                if (distanceToNextDifference < contextLines || distanceToPrevDifference < contextLines) {
                    b
                            .append(padLeft("" + expectedLine, rowNumberWidth))
                            .append(padLeft("" + actualLine, rowNumberWidth))
                            .append("   ").append(diffRow.getOldLine());

                } else if (distanceToNextDifference == contextLines || distanceToPrevDifference == contextLines) {
                    b.append("[...]");
                } else {
                    expectedLine++;
                    actualLine++;
                    continue;
                }

                expectedLine++;
                actualLine++;
                break;
            case CHANGE:
                indexOfLastDifference = i;
                b
                        .append(padLeft("" + expectedLine, rowNumberWidth))
                        .append(" ".repeat(rowNumberWidth))
                        .append(" - ").append(diffRow.getOldLine()).append(LineSeparator.SYSTEM)
                        .append(" ".repeat(rowNumberWidth))
                        .append(padLeft("" + actualLine, rowNumberWidth))
                        .append(" + ")
                        .append(diffRow.getNewLine());

                expectedLine++;
                actualLine++;
                break;

            case DELETE:
                indexOfLastDifference = i;
                b
                        .append(padLeft("" + expectedLine, rowNumberWidth))
                        .append(" ".repeat(rowNumberWidth))
                        .append(" - ").append(diffRow.getOldLine());

                expectedLine++;
                break;
            case INSERT:
                indexOfLastDifference = i;
                b
                        .append(" ".repeat(rowNumberWidth))
                        .append(padLeft("" + actualLine, rowNumberWidth))
                        .append(" + ").append(diffRow.getNewLine());
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
