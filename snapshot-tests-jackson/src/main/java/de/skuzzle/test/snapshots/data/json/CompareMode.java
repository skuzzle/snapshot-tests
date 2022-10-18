package de.skuzzle.test.snapshots.data.json;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * Defines how strict JSONs are to be compared. Use with
 * {@link JsonSnapshot#withCompareMode(CompareMode)}. The default mode is {@link #STRICT}.
 *
 * @since 1.5.0
 * @author Simon Taddiken
 */
@API(status = Status.STABLE, since = "1.5.0")
public enum CompareMode {
    /**
     * Strict checking. Not extensible, and strict array ordering.
     */
    STRICT,
    /**
     * Lenient checking. Extensible, and non-strict array ordering.
     */
    LENIENT,
    /**
     * Non-extensible checking. Not extensible, and non-strict array ordering.
     */
    NON_EXTENSIBLE,
    /**
     * Strict order checking. Extensible, and strict array ordering.
     */
    STRICT_ORDER;

    JSONCompareMode toJSONCompareMode() {
        return JSONCompareMode.valueOf(name());
    }
}
