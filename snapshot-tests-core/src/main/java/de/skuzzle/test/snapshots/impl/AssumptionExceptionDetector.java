package de.skuzzle.test.snapshots.impl;

import java.util.List;
import java.util.Optional;

/**
 * Tries to detect which type of assumption failed exception is supported on the current
 * classpath.
 * 
 * @author Simon Taddiken
 * @since 1.8.0
 */
final class AssumptionExceptionDetector {

    private static final List<String> CLASS_NAMES = List.of(
            "org.opentest4j.TestAbortedException",
            "org.junit.AssumptionViolatedException",
            "org.testng.SkipException");
    private static final Class<? extends Throwable> EXCEPTION_CLASS;
    static {
        Class<? extends Throwable> result = null;
        try {
            for (final String className : CLASS_NAMES) {
                @SuppressWarnings("unchecked")
                final Class<? extends Throwable> clazz = (Class<? extends Throwable>) tryLoad(className);
                if (clazz != null) {
                    result = clazz;
                    break;
                }
            }
        } catch (final Exception ignore) {
        }
        EXCEPTION_CLASS = result;
    }

    /**
     * Creates a throwable that will cause a testcase to be marked as 'skipped' by the
     * test framework that is in use.
     * 
     * @param message The message for the exception.
     * @return An empty optional if no supported assumption exception was found or we
     *         failed to create an instance. Otherwise returns the created exception.
     */
    static Optional<Throwable> assumptionFailed(String message) {
        if (!assumptionsSupported()) {
            return Optional.empty();
        }
        try {
            return Optional.of(EXCEPTION_CLASS.getConstructor(String.class).newInstance(message));
        } catch (final ReflectiveOperationException e) {
            return Optional.empty();
        }
    }

    private static boolean assumptionsSupported() {
        return EXCEPTION_CLASS != null;
    }

    private static Class<?> tryLoad(String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }
}
