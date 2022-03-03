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
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
        return object;
    }

    public static <T> T requireNonNull(T object, String message, Object... params) {
        if (object == null) {
            throw new IllegalArgumentException(String.format(message, params));
        }
        return object;
    }
}
