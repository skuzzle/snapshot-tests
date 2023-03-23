package de.skuzzle.difftool;

import java.util.stream.Collectors;

/**
 * Used to represent and detect a file's line separator character(s). The
 * {@link #toString()} method will return the plain line break characters.
 *
 * @author Simon Taddiken
 * @since 1.5.0
 */
public enum LineSeparator {
    // note: don't reorder!
    CRLF("\r\n", "\\r\\n"),
    LF("\n", "\\n"),
    CR("\r", "\\r"),
    SYSTEM(System.lineSeparator(), System.lineSeparator()
            .replace("\r", "\\r")
            .replace("\n", "\\n"));

    private final String characters;
    private final String displayName;

    LineSeparator(String characters, String displayName) {
        this.characters = characters;
        this.displayName = displayName;
    }

    /**
     * Determines the line separator used in the given String. Will return the type of the
     * first line separator found in the String.
     * <p>
     * If the String does not contain any line separators, will return
     * {@link LineSeparator#SYSTEM}.
     * </p>
     *
     * @param s The String to check.
     * @return The type of line endings.
     */
    public static LineSeparator determineFrom(String s) {
        for (final LineSeparator lineSeparator : LineSeparator.values()) {
            if (lineSeparator.existsIn(s)) {
                return lineSeparator;
            }
        }
        return SYSTEM;
    }

    private boolean existsIn(String s) {
        return s.contains(characters);
    }

    public String displayName() {
        return name() + "(" + displayName + ")";
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
