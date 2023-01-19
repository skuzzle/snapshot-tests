package de.skuzzle.test.snapshots.reflection;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Utility for working with stacktraces.
 * 
 * @author Simon Taddiken
 * @since 1.9.0
 */
@API(status = Status.INTERNAL, since = "1.9.0")
public final class StackTraces {

    /**
     * Finds the immediate caller of the method which calls this method. Useful for
     * logging purposes.
     * 
     * @return An object describing the caller.
     * @since 1.9.0
     */
    public static Caller findImmediateCaller() {
        // Example stack trace
        // @formatter:off
        // 0: java.base/java.lang.Thread.getStackTrace(Thread.java:1602)
        // 1: de.skuzzle.test.snapshots.reflection.StackTraces.findImmediateCaller(StackTraces.java:12)
        // 2: de.skuzzle.test.snapshots.reflection.StackTracesTest.methodThatWantsToKnowItsCaller(StackTracesTest.java:16)
        // 3: de.skuzzle.test.snapshots.reflection.StackTracesTest.testGetCaller(StackTracesTest.java:11) <-- Element we are interested in
        // ...
        // @formatter:on
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length < 4) {
            return Caller.unknown();
        }
        return Caller.of(stackTrace[3]);
    }

    public static final class Caller {
        private static final Caller UNKNOWN = new Caller(null);

        private final StackTraceElement callerElement;

        private Caller(StackTraceElement callerElement) {
            this.callerElement = callerElement;
        }

        public static Caller unknown() {
            return UNKNOWN;
        }

        public static Caller of(StackTraceElement callerElement) {
            return new Caller(Arguments.requireNonNull(callerElement));
        }

        @Override
        public String toString() {
            if (callerElement == null) {
                return "unknown";
            }
            return callerElement.toString();
        }
    }
}
