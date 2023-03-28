package de.skuzzle.difftool;

import java.util.List;

/**
 * Defines how a diff is rendered as a String.
 */
public interface DiffRenderer {

    /**
     * Creates a String representation of the provided diff.
     *
     * @param rows The diff.
     * @param diffSettings Additional parameters.
     * @return The String representation.
     */
    String renderDiff(List<DiffLine> rows, DiffSettings diffSettings);
}
