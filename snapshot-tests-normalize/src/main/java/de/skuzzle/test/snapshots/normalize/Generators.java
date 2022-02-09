package de.skuzzle.test.snapshots.normalize;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiFunction;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.EXPERIMENTAL)
public class Generators {

    public static BiFunction<Integer, Object, String> uuidLike() {
        return (i, match) -> {
            final String hex = Integer.toHexString(i);
            return "00000000-0000-0000-0000-" + "0".repeat(12 - hex.length()) + hex;
        };
    }

    public static BiFunction<Integer, String, String> dateLike(DateTimeFormatter formatter) {
        return (i, match) -> {
            final long millis = 365L * 24L * 60L * 60L * 1000L * (long) i;
            final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC);
            return formatter.format(zonedDateTime);
        };
    }
}
