package de.skuzzle.difftool;

/**
 * Defines parameters that are used when rendering a diff to String using a
 * {@link DiffRenderer}.
 */
public final class DiffSettings {

    public static final DiffSettings DEFAULT = new DiffSettings(5, 0, DiffSymbols.DEFAULT);

    private final int contextLines;
    private final int lineNumberOffset;
    private final DiffSymbols symbols;

    public DiffSettings(int contextLines, int lineNumberOffset, DiffSymbols symbols) {
        this.contextLines = contextLines;
        this.lineNumberOffset = lineNumberOffset;
        this.symbols = symbols;
    }

    public static DiffSettings withDefaultSymbols(int contextLines, int lineNumberOffset) {
        return new DiffSettings(contextLines, lineNumberOffset, DiffSymbols.DEFAULT);
    }

    public int contextLines() {
        return contextLines;
    }

    public int lineNumberOffset() {
        return lineNumberOffset;
    }

    public DiffSymbols symbols() {
        return symbols;
    }

    public static final class DiffSymbols {

        public static DiffSymbols DEFAULT = new DiffSymbols("!", "+", "-", " ", LineSeparator.SYSTEM, "[...]");

        private final String changedLine;
        private final String addedLine;
        private final String deletedLine;
        private final String equalLine;
        private final LineSeparator newLineCharacter;
        private final String continuation;

        public DiffSymbols(String changedLine, String addedLine, String deletedLine, String equalLine,
                LineSeparator newLineCharacter, String continuation) {
            this.changedLine = changedLine;
            this.addedLine = addedLine;
            this.deletedLine = deletedLine;
            this.equalLine = equalLine;
            this.newLineCharacter = newLineCharacter;
            this.continuation = continuation;
        }

        public String changedLine() {
            return changedLine;
        }

        public String addedLine() {
            return addedLine;
        }

        public String deletedLine() {
            return deletedLine;
        }

        public String equalLine() {
            return equalLine;
        }

        public LineSeparator newLineCharacter() {
            return newLineCharacter;
        }

        public String continuation() {
            return continuation;
        }
    }
}
