package de.skuzzle.test.snapshots.validation;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Internal utility for validating states.
 *
 * @since 1.5.0
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL, since = "1.5.0")
public final class State {

    private State() {
        // hidden
    }

    public static void check(boolean condition) {
        check(condition, "Unexpected state occurred");
    }

    public static void check(boolean condition, String message, Object... params) {
        if (!condition) {
            throw new IllegalStateException(String.format(message, params));
        }
    }
}
