package de.skuzzle.test.snapshots.normalize;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

@API(status = Status.EXPERIMENTAL, since = "1.3.0")
public final class Strings {

    /**
     * Creates a function that, when applied to a string, will replace all matches of the
     * given pattern with a value returned by the given generator function. Additionally,
     * when the same string is matched a again by the pattern, it will be replaced with
     * the same generated string as the first.
     *
     * @param pattern The pattern to find.
     * @param generator A generator that is used to create replacements for matched
     *            strings. The first parameter passed to the function is the count of
     *            different strings matched so far. The second parameter is the actually
     *            matched String.
     * @return A function that will execute above explained replacements to a string to
     *         which it is applied
     * @see Generators
     */
    public static Function<String, String> consistentlyReplace(Pattern pattern,
            BiFunction<Integer, String, String> generator) {
        Arguments.requireNonNull(pattern, "pattern must not be null");
        Arguments.requireNonNull(generator, "generator must not be null");

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

    /**
     * Creates a function that, when applied to a string, will replace all matches of the
     * given pattern with a value returned by the given generator function. Additionally,
     * when the same string is matched a again by the pattern, it will be replaced with
     * the same generated string as the first.
     *
     * @param pattern The pattern to find.
     * @param generator A generator that is used to create replacements for matched
     *            strings. The parameter passed to the function is the count of different
     *            strings matched so far.
     * @return A function that will execute above explained replacements to a string to
     *         which it is applied
     * @see Generators
     * @see #consistentlyReplace(Pattern, BiFunction)
     */
    public static Function<String, String> consistentlyReplace(Pattern pattern,
            Function<Integer, String> generator) {
        Arguments.requireNonNull(generator, "generator must not be null");
        return consistentlyReplace(pattern, (i, match) -> generator.apply(i));
    }
}
