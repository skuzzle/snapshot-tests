package de.skuzzle.test.snapshots.validation;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Internal parameter validation utility.
 *
 * @author Simon Taddiken
 * @since 1.2.0
 */
@API(status = Status.INTERNAL, since = "1.2.0")
public final class Arguments {

    private Arguments() {
        // hidden
    }

    public static <T> T requireNonNull(T object) {
        return requireNonNull(object, "parameter is expected to be non null");
    }

    public static <T> T requireNonNull(T object, String message) {
        check(object != null, message);
        return object;
    }

    public static <T> T requireNonNull(T object, String message, Object... params) {
        check(object != null, message, params);
        return object;
    }

    public static void check(boolean condition) {
        check(condition, "Argument validation failed with no explicit message");
    }

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void check(boolean condition, String message, Object... params) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(message, params));
        }
    }
}
