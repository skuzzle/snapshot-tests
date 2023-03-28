package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;

import org.apiguardian.api.API;

/**
 * Abstraction to provide test framework specific behavior to the snapshot-test core
 *
 * @since 1.10.0
 */
@API(status = API.Status.INTERNAL, since = "1.10.0")
public interface TestFrameworkSupport {

    /**
     * Tries to determine whether the given method is (still) a snapshot test. This is
     * used during static orphan detection in order to determine whether the method
     * mentioned in an existing snapshot's header pertains to an existing snapshot test
     * method.
     * <p>
     * It might not always be possible to statically determine whether a test method uses
     * snapshot assertions. In this case the method should return true in order to prevent
     * false positives during orphan detection.
     * </p>
     *
     * @param testClass The test class.
     * @param testMethod The test method.
     * @return Whether the given test method uses snapshot assertions.
     */
    boolean isSnapshotTest(Class<?> testClass, Method testMethod);

    /**
     * Creates a throwable that will mark the repsective test as skipped.
     *
     * @param message The message for the throwable.
     * @return The throwable.
     */
    Throwable assumptionFailed(String message);
}
