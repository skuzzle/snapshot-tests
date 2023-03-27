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
     * Tries to determine the line separator from GIT according to its
     * <code>core.autocrlf</code> and <code>core.eol</code> config values.
     * <p>
     * WARNING: In order to determine the config values, this method will invoke a system
     * process to call <code>git config ...</code>. This will only be done once and the
     * results will be cached in memory for the lifetime of the VM.
     * </p>
     * <p>
     * If this method fails to determine the line separator from GIT config it will return
     * {@link LineSeparator#SYSTEM} as fall back. Otherwise the line separator will be
     * determined according to the following table:
     * <table>
     * <tr>
     * <th>core.autocrlf</th>
     * <th>core.eol</th>
     * <th>Result</th>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>-</code></td>
     * <td>{@link LineSeparator#CRLF}</td>
     * </tr>
     * <tr>
     * <td><code>input</code></td>
     * <td><code>-</code></td>
     * <td>{@link LineSeparator#LF}</td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>crlf</code></td>
     * <td>{@link LineSeparator#CRLF}</td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>lf</code></td>
     * <td>{@link LineSeparator#LF}</td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>native</code></td>
     * <td>{@link LineSeparator#SYSTEM}</td>
     * </tr>
     * <caption>Logic of how to determine the LineSeparator from GIT</caption>
     * </table>
     *
     * @return The {@link LineSeparator}
     */
    public static LineSeparator determineFromGitConfig() {
        return GitLineSeparator.GIT_LINE_SEPARATOR;
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
        final int r = s.indexOf('\r');
        if (r >= 0) {
            if (s.length() > r + 1) {
                final int n = s.charAt(r + 1);
                if (n == '\n') {
                    return CRLF;
                }
            }
            return CR;
        }
        final int n = s.indexOf('\n');
        if (n >= 0) {
            return LF;
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
        if (existsIn(s)) {
            return s;
        }
        return s.lines().collect(Collectors.joining(characters));
    }

    @Override
    public String toString() {
        return characters;
    }
}
