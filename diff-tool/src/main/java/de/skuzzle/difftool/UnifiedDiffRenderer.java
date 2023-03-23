package de.skuzzle.difftool;

import java.util.List;

public final class UnifiedDiffRenderer implements DiffRenderer {

    public static final DiffRenderer INSTANCE = new UnifiedDiffRenderer();

    private UnifiedDiffRenderer() {
        // hidden
    }

    @Override
    public String renderDiff(List<DiffLine> rows, DiffSettings diffSettings) {
        final int rowCountEstimate = rows.size();
        final int rowNumberWidth = Math.max(2, (int) Math.log10(rowCountEstimate) + 1) + 1;
        final int lineNumberOffset = diffSettings.lineNumberOffset();
        final int contextLines = diffSettings.contextLines();
        final DiffSettings.DiffSymbols symbols = diffSettings.symbols();

        final String equalLine = " " + symbols.equalLine() + " ";
        final String deletedLine = " " + symbols.deletedLine() + " ";
        final String addedLine = " " + symbols.addedLine() + " ";

        int expectedLine = 1 + lineNumberOffset;
        int actualLine = 1 + lineNumberOffset;
        final StringBuilder b = new StringBuilder();

        int indexOfLastDifference = -1;
        for (int i = 0; i < rows.size(); i++) {
            final DiffLine diffRow = rows.get(i);

            switch (diffRow.type()) {
            case EQUAL:
                final int indexOfNextDifference = Util.indexOfNextNonEqual(rows, i);

                final int distanceToNextDifference = indexOfNextDifference == rows.size()
                        ? Integer.MAX_VALUE
                        : indexOfNextDifference - i - 1;
                final int distanceToPrevDifference = indexOfLastDifference == -1
                        ? Integer.MAX_VALUE
                        : i - indexOfLastDifference - 1;

                if (distanceToNextDifference < contextLines || distanceToPrevDifference < contextLines) {
                    b
                            .append(Util.padLeft("" + expectedLine, rowNumberWidth))
                            .append(Util.padLeft("" + actualLine, rowNumberWidth))
                            .append(equalLine).append(diffRow.oldLine());

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
            case CHANGE:
                indexOfLastDifference = i;
                b
                        .append(Util.padLeft("" + expectedLine, rowNumberWidth))
                        .append(" ".repeat(rowNumberWidth))
                        .append(deletedLine).append(diffRow.oldLine()).append(symbols.newLineCharacter())
                        .append(" ".repeat(rowNumberWidth))
                        .append(Util.padLeft("" + actualLine, rowNumberWidth))
                        .append(addedLine)
                        .append(diffRow.newLine());

                expectedLine++;
                actualLine++;
                break;

            case DELETE:
                indexOfLastDifference = i;
                b
                        .append(Util.padLeft("" + expectedLine, rowNumberWidth))
                        .append(" ".repeat(rowNumberWidth))
                        .append(deletedLine).append(diffRow.oldLine());

                expectedLine++;
                break;
            case INSERT:
                indexOfLastDifference = i;
                b
                        .append(" ".repeat(rowNumberWidth))
                        .append(Util.padLeft("" + actualLine, rowNumberWidth))
                        .append(addedLine).append(diffRow.newLine());
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

}
