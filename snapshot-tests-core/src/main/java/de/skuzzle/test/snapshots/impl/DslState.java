package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.validation.State;

/**
 * Tracks the state of single DSL usage to detect whether always a terminal operation has
 * been called.
 *
 * @author Simon Taddiken
 * @since 1.5.0
 */
final class DslState {

    public static final int NAME_CHOSEN = 2;
    public static final int ACTUAL_CHOSEN = 2 << 1;
    public static final int DIRECTORY_CHOSEN = 2 << 2;

    private int dslState = 0;

    private DslState() {
        // hidden
    }

    public static DslState initial() {
        return new DslState();
    }

    public boolean has(int state) {
        return (dslState & state) != 0;
    }

    public DslState append(int state) {
        State.check(!has(state),
                "Detected illegal reuse of a DSL stage. Please always finish a DSL invocation completely until the end, "
                        + "without calling the same DSL method twice in between. ",
                state);
        this.dslState |= state;
        return this;
    }

    public boolean isInitial() {
        return this.dslState == 0;
    }

    public DslState reset() {
        this.dslState = 0;
        return this;
    }
}
