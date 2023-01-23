package de.skuzzle.test.snapshots.impl;

import static java.util.stream.Collectors.reducing;

import java.util.stream.Stream;

import de.skuzzle.test.snapshots.reflection.StackTraces;

/**
 * Internal utility to work with exceptions. Kind of related to {@link StackTraces} in the
 * common package.
 * 
 * @author Simon Taddiken
 * @see StackTraces
 */
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
}
