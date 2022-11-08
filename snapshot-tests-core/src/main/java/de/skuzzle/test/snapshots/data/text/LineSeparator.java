package de.skuzzle.test.snapshots.data.text;

import java.util.stream.Collectors;

/**
 * Used to represent a file's line separator.
 *
 * @since 1.5.0
 * @author Simon Taddiken
 */
enum LineSeparator {
    // note: don't reorder!
    CRLF("\r\n", "\\r\\n"),
    LF("\n", "\\n"),
    CR("\r", "\\r"),
    DEFAULT("\n", "\\n"),
    SYSTEM(System.lineSeparator(), System.lineSeparator().replace("\r", "\\r").replace("\n", "\\n"));

    private final String characters;
    private final String displayName;

    private LineSeparator(String characters, String displayName) {
        this.characters = characters;
        this.displayName = displayName;
    }

    public static LineSeparator determineFrom(String s) {
        for (final LineSeparator lineSeparator : LineSeparator.values()) {
            if (lineSeparator.existsIn(s)) {
                return lineSeparator;
            }
        }
        return DEFAULT;
    }

    private boolean existsIn(String s) {
        return s.indexOf(characters) >= 0;
    }

    public String displayName() {
        return name() + "(" + displayName + ")";
    }

    public boolean endsWith(String s) {
        return s.endsWith(this.characters);
    }

    public boolean startsWith(String s) {
        return s.startsWith(this.characters);
    }

    /**
     * Converts all line separators in the given String to the line separator represented
     * by the enum constant on which this method is called.
     *
     * @param s The string in which to convert the line separators.
     * @return The string with converted line separators.
     */
    public String convert(String s) {
        return s.lines().collect(Collectors.joining(characters));
    }

    @Override
    public String toString() {
        return characters;
    }
}
