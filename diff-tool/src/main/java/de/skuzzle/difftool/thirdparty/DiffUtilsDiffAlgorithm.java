package de.skuzzle.difftool.thirdparty;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;
import de.skuzzle.difftool.DiffAlgorithm;
import de.skuzzle.difftool.DiffLine;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Based on <em>java-diff-utils</em>.
 */
public final class DiffUtilsDiffAlgorithm implements DiffAlgorithm {

    /**
     * A default instance with sensible defaults for comparing texts.
     */
    public static final DiffAlgorithm INSTANCE = create(builder -> builder
            .showInlineDiffs(true)
            .lineNormalizer(Function.identity())
            .inlineDiffByWord(true)
            .ignoreWhiteSpaces(true)
            .newTag(DiffUtilsDiffAlgorithm::inlineMarker)
            .oldTag(DiffUtilsDiffAlgorithm::inlineMarker));

    private static String inlineMarker(DiffRow.Tag tag, boolean isOpening) {
        if (tag != DiffRow.Tag.CHANGE) {
            return "";
        }
        return isOpening ? "<<" : ">>";
    }

    private final DiffRowGenerator generator;

    private DiffUtilsDiffAlgorithm(DiffRowGenerator generator) {
        this.generator = generator;
    }

    public static DiffUtilsDiffAlgorithm using(DiffRowGenerator diffRowGenerator) {
        return new DiffUtilsDiffAlgorithm(Objects.requireNonNull(diffRowGenerator));
    }

    public static DiffUtilsDiffAlgorithm create(Consumer<DiffRowGenerator.Builder> configure) {
        final DiffRowGenerator.Builder builder = DiffRowGenerator.create();
        Objects.requireNonNull(configure).accept(builder);
        return new DiffUtilsDiffAlgorithm(builder.build());
    }

    @Override
    public List<DiffLine> diffOf(List<String> left, List<String> right) {
        return generator.generateDiffRows(left, right).stream()
                .map(row -> new DiffLine(translateTag(row.getTag()), row.getOldLine(), row.getNewLine()))
                .collect(Collectors.toList());
    }

    private DiffLine.Type translateTag(DiffRow.Tag tag) {
        return DiffLine.Type.valueOf(tag.name());
    }
}
