package de.skuzzle.difftool;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import de.skuzzle.difftool.thirdparty.DiffUtilsDiffAlgorithm;

/**
 * Represents the difference between two Strings. The <em>difference</em> is calculated
 * row wise by a {@link DiffAlgorithm} and is internally represented as a List of
 * {@link DiffLine}.
 * <p>
 * Differences in line breaks are not part of the actual diff but will be determined as
 * well.
 * </p>
 *
 * Whether other whitespaces are subject to comparison depends on the used
 * {@linkplain DiffAlgorithm}.
 * <p>
 * A {@linkplain StringDiff} can be rendered as a nice human readable String using a
 * {@link DiffRenderer}. Rendering behavior can be fine tuned by providing a
 * {@link DiffSettings} instance.
 * </p>
 */
public final class StringDiff {

    private static final DiffAlgorithm DEFAULT_ALGORITHM = DiffUtilsDiffAlgorithm.INSTANCE;
    private static final DiffRenderer DEFAULT_RENDERER = UnifiedDiffRenderer.INSTANCE;
    private static final DiffSettings DEFAULT_SETTINGS = DiffSettings.DEFAULT;

    private final List<DiffLine> diff;
    private final LineSeparator leftLineSeparator;
    private final LineSeparator rightLineSeparator;

    private StringDiff(List<DiffLine> diff, LineSeparator leftLineSeparator, LineSeparator rightLineSeparator) {
        this.diff = Objects.requireNonNull(List.copyOf(diff));
        this.leftLineSeparator = Objects.requireNonNull(leftLineSeparator);
        this.rightLineSeparator = Objects.requireNonNull(rightLineSeparator);
    }

    public static StringDiff simple(String left, String right) {
        return using(DEFAULT_ALGORITHM, left, right);
    }

    public static StringDiff using(DiffAlgorithm algorithm, List<String> left, List<String> right) {
        final List<DiffLine> diff = algorithm.diffOf(left, right);
        return new StringDiff(diff, LineSeparator.SYSTEM, LineSeparator.SYSTEM);
    }

    public static StringDiff using(DiffAlgorithm algorithm, String left, String right) {
        final LineSeparator leftLineSeparator = LineSeparator.determineFrom(left);
        final LineSeparator rightLineSeparator = LineSeparator.determineFrom(right);

        final List<DiffLine> diff = algorithm.diffOf(
                left.lines().collect(Collectors.toUnmodifiableList()),
                right.lines().collect(Collectors.toUnmodifiableList()));

        return new StringDiff(diff, leftLineSeparator, rightLineSeparator);
    }

    /**
     * Returns the internal list of differences (read-only).
     *
     * @return The list of differences.
     */
    public List<DiffLine> textDifferences() {
        return diff;
    }

    /**
     * Returns the {@link LineSeparator} for the left file of the comparison.
     *
     * @return The line separator.
     */
    public LineSeparator leftLineSeparator() {
        return leftLineSeparator;
    }

    /**
     * Returns the {@link LineSeparator} of the right file of the comparison.
     *
     * @return The line separator.
     */
    public LineSeparator rightLineSeparator() {
        return rightLineSeparator;
    }

    /**
     * Whether the compared Strings differ in either line separators or at least one text
     * difference was found. Equivalent to
     *
     * <pre>
     * hasDifference = hasLineSeparatorDifference() || hasTextDifference();
     * </pre>
     *
     * @return Whether any difference has been found.
     */
    public boolean hasDifference() {
        return hasLineSeparatorDifference() || hasTextDifference();
    }

    /**
     * Whether at least one text difference has been found.
     *
     * @return Whether at least one difference has been found.
     */
    public boolean hasTextDifference() {
        return diff.stream().map(DiffLine::type).anyMatch(type -> type != DiffLine.Type.EQUAL);
    }

    /**
     * Whether compared Strings differed in line separators.
     *
     * @return Whether compared Strings differed in line separators.
     */
    public boolean hasLineSeparatorDifference() {
        return !leftLineSeparator.equals(rightLineSeparator);
    }

    /**
     * Creates a String representation of this diff via the given {@link DiffRenderer}
     * using given {@link DiffSettings}.
     *
     * @param diffRenderer The renderer.
     * @param diffSettings The settings that will be passed to the renderer.
     * @return The String representation.
     */
    public String toString(DiffRenderer diffRenderer, DiffSettings diffSettings) {
        return diffRenderer.renderDiff(diff, diffSettings);
    }

    public String toString(DiffRenderer diffRenderer) {
        return toString(diffRenderer, DEFAULT_SETTINGS);
    }

    /**
     * Creates a unified rendered diff of this StringDiff using default
     * {@link DiffSettings}.
     *
     * @return A String representation of this diff.
     */
    @Override
    public String toString() {
        return toString(DEFAULT_RENDERER, DEFAULT_SETTINGS);
    }
}
