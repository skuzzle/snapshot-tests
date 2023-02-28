package de.skuzzle.test.snapshots.normalize;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;
import java.util.regex.Pattern;

import de.skuzzle.test.snapshots.validation.Arguments;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.EXPERIMENTAL)
public final class Generators {

    public static final Pattern UUID = Pattern.compile("[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}");

    private Generators() {
        // hidden
    }

    /**
     * Generator that produces only the given constant replacement.
     *
     * @param replacement The constant replacement string.
     * @return A function that always returns the given constant.
     */
    public static Function<Integer, String> constant(String replacement) {
        Arguments.requireNonNull(replacement, "replacement must not be null");
        return counter -> replacement;
    }

    /**
     * Generator that produces deterministic UUIDs from a counter.
     *
     * @return A function that deterministically creates a UUID from the counter passed to
     *         it.
     */
    public static Function<Integer, String> deterministicUUID() {
        return counter -> {
            final String hex = Integer.toHexString(counter);
            return "00000000-0000-0000-0000-" + "0".repeat(12 - hex.length()) + hex;
        };
    }

    /**
     * A generator that produces deterministic dates from a counter. The date will be
     * calculated by adding <code>stepdWidth</code> times <code>counter</code>
     * milliseconds to the UTC base date 1970-01-01 00:00.
     *
     * @param stepWidth Defines how to calculate a new date from a counter.
     * @return A function that calculates a deterministic date from the counter passed to
     *         it.
     */
    public static Function<Integer, ZonedDateTime> dateLike(ChronoUnit stepWidth) {
        Arguments.requireNonNull(stepWidth, "stepWidth must not be null");
        return counter -> {
            final long millis = stepWidth.getDuration().toMillis() * (long) counter;
            return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
        };
    }

    /**
     * A generator that produces deterministic date strings from a counter. The date will
     * be calculated by adding <code>stepdWidth</code> times <code>counter</code>
     * milliseconds to the UTC base date 1970-01-01 00:00.
     *
     * @param formatter For formatting the calculated date.
     * @param stepWidth Defines how to calculate a new date from a counter.
     * @return A function that calculates a deterministic date from the counter passed to
     *         it.
     */
    public static Function<Integer, String> dateLike(DateTimeFormatter formatter, ChronoUnit stepWidth) {
        Arguments.requireNonNull(formatter, "formatter must not be null");
        return dateLike(stepWidth).andThen(formatter::format);
    }
}
