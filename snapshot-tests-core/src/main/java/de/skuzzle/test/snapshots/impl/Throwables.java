package de.skuzzle.test.snapshots.impl;

import static java.util.stream.Collectors.reducing;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

final class Throwables {

    private Throwables() {
        // hidden
    }

    public static void throwIfNotNull(Throwable throwable) throws Exception {
        if (throwable == null) {
            return;
        }
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        if (throwable instanceof Exception) {
            throw (Exception) throwable;
        }
        throw new RuntimeException(throwable);
    }

    public static Throwable flattenThrowables(Stream<? extends Throwable> results) {
        return results
                .collect(reducing(Throwables::combine))
                .orElse(null);
    }

    public static Throwable combine(Throwable t, Throwable other) {
        if (t == null) {
            return other;
        }
        if (other != null) {
            t.addSuppressed(other);
        }
        return t;
    }

    public static void filterStackTrace(Throwable throwable, Predicate<StackTraceElement> elementsToRemove) {
        if (throwable == null) {
            return;
        }
        final StackTraceElement[] original = throwable.getStackTrace();
        final StackTraceElement[] filtered = Arrays.stream(original)
                .filter(elementsToRemove.negate())
                .toArray(StackTraceElement[]::new);
        throwable.setStackTrace(filtered);

        filterStackTrace(throwable.getCause(), elementsToRemove);
        for (final Throwable suppressed : throwable.getSuppressed()) {
            filterStackTrace(suppressed, elementsToRemove);
        }
    }
}
