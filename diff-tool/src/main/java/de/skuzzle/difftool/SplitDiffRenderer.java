package de.skuzzle.difftool;

import java.util.List;

/**
 * Renders the diff in "split" view.
 */
public final class SplitDiffRenderer implements DiffRenderer {

    public static final DiffRenderer INSTANCE = new SplitDiffRenderer();

    private SplitDiffRenderer() {
        // hidden
    }

    @Override
    public String renderDiff(List<DiffLine> rows, DiffSettings diffSettings) {
        final int columnWidth = rows.stream().map(DiffLine::oldLine).mapToInt(String::length).max().orElse(80);
        final int rowCountEstimate = rows.size();
        final int rowNumberWidth = Math.max(2, (int) Math.log10(rowCountEstimate) + 1) + 1;
        final int lineNumberOffset = diffSettings.lineNumberOffset();
        final int contextLines = diffSettings.contextLines();
        final DiffSettings.DiffSymbols symbols = diffSettings.symbols();

        int expectedLine = 1 + lineNumberOffset;
        int actualLine = 1 + lineNumberOffset;
        final StringBuilder b = new StringBuilder();

        int indexOfLastDifference = -1;

        for (int i = 0; i < rows.size(); i++) {
            final DiffLine diffRow = rows.get(i);

            switch (diffRow.type()) {
            case CHANGE:
                indexOfLastDifference = i;
                appendRow(b, columnWidth,
                        Util.padLeft("" + expectedLine, rowNumberWidth),
                        symbols.changedLine(),
                        diffRow.oldLine(),
                        Util.padLeft("" + actualLine, rowNumberWidth),
                        symbols.changedLine(), diffRow.newLine());

                expectedLine++;
                actualLine++;
                break;
            case EQUAL:
                final int indexOfNextDifference = Util.indexOfNextNonEqual(rows, i);

                final int distanceToNextDifference = indexOfNextDifference == rows.size()
                        ? Integer.MAX_VALUE
                        : indexOfNextDifference - i - 1;
                final int distanceToPrevDifference = indexOfLastDifference == -1
                        ? Integer.MAX_VALUE
                        : i - indexOfLastDifference - 1;

                if (distanceToNextDifference < contextLines || distanceToPrevDifference < contextLines) {
                    appendRow(b, columnWidth,
                            Util.padLeft("" + expectedLine, rowNumberWidth),
                            symbols.equalLine(),
                            diffRow.oldLine(),
                            Util.padLeft("" + actualLine, rowNumberWidth),
                            symbols.equalLine(), diffRow.newLine());

                } else if (distanceToNextDifference == contextLines || distanceToPrevDifference == contextLines) {
                    b.append(symbols.continuation());
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
                        Util.padLeft("" + expectedLine, rowNumberWidth),
                        symbols.deletedLine(),
                        diffRow.oldLine(),
                        " ".repeat(rowNumberWidth),
                        symbols.equalLine(), diffRow.newLine());

                expectedLine++;
                break;

            case INSERT:
                indexOfLastDifference = i;

                appendRow(b, columnWidth,
                        " ".repeat(rowNumberWidth),
                        symbols.equalLine(),
                        diffRow.oldLine(),
                        Util.padLeft("" + actualLine, rowNumberWidth),
                        symbols.addedLine(), diffRow.newLine());

                actualLine++;
                break;
            default:
                throw new IllegalStateException();

            }

            if (i < rows.size() - 1) {
                b.append(symbols.newLineCharacter());
            }
        }

        return b.toString();
    }

    private void appendRow(StringBuilder b, int columnWidth, String paddedExpectedLineNumber, String expectedOperation,
            String expectedLine, String paddedActualLineNumber, String actualOperation, String actualLine) {
        b.append(paddedExpectedLineNumber)
                .append(" ")
                .append(expectedOperation)
                .append(" ")
                .append(Util.padRight(expectedLine, columnWidth))
                .append(" |")
                .append(paddedActualLineNumber)
                .append(" ").append(actualOperation).append(" ").append(actualLine);

    }
}
