package de.skuzzle.test.snapshots.reflection;

import java.util.Arrays;
import java.util.function.Predicate;

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
     * Deeply cleans the stack trace by removing all {@link StackTraceElement elements}
     * which are matched by the provided predicate.
     * <p>
     * This method recurses into both all causes and all suppressed exceptions of the
     * provided root exception.
     * <p>
     * It is allowed to pass null as throwable. In this case, nothing happens.
     * 
     * @param throwable The throwable for which to clean the stack trace. Can be null.
     * @param elementsToRemove A predicate matching the elements to remove.
     * @since 1.9.0
     */
    public static void filterStackTrace(Throwable throwable, Predicate<StackTraceElement> elementsToRemove) {
        if (throwable == null) {
            return;
        }
        final StackTraceElement[] original = throwable.getStackTrace();
        if (original != null) {
            final StackTraceElement[] filtered = Arrays.stream(original)
                    .filter(elementsToRemove.negate())
                    .toArray(StackTraceElement[]::new);
            throwable.setStackTrace(filtered);
        }

        filterStackTrace(throwable.getCause(), elementsToRemove);
        for (final Throwable suppressed : throwable.getSuppressed()) {
            filterStackTrace(suppressed, elementsToRemove);
        }
    }

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

    /**
     * Represents the call site of a method in which
     * {@link StackTraces#findImmediateCaller()} has been invoked.
     * 
     * @author Simon Taddiken
     */
    @API(status = Status.INTERNAL, since = "1.9.0")
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
