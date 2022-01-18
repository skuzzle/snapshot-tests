package de.skuzzle.test.snapshots.normalize;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Normalize {

    /**
     * Returns a function that "consistently" replaces the matches of a Pattern within a
     * string. The replacement for each match is obtained from the provided generator
     * function. If an exact string is matched twice, it will be replaced with the value
     * that has been returned by the generator function the first time it was matched.
     *
     * @param pattern The pattern to search for.
     * @param generator A function that provides a replacement for a match. It gets passed
     *            the number of times the pattern matched so far and the actually matched
     *            string.
     * @return
     */
    public static Function<String, String> consistentlyReplace(Pattern pattern,
            BiFunction<Integer, String, String> generator) {
        return input -> {
            final Map<String, String> replacements = new HashMap<>();
            final StringBuilder result = new StringBuilder(input.length());
            final Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                final String matched = matcher.group();
                final String replacement = replacements.computeIfAbsent(matched,
                        key -> generator.apply(replacements.size(), matched));
                matcher.appendReplacement(result, replacement);
            }
            matcher.appendTail(result);
            return result.toString();
        };
    }

}
