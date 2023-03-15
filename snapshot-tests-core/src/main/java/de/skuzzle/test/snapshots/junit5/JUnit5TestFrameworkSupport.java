package de.skuzzle.test.snapshots.junit5;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.impl.TestFrameworkSupport;

import org.opentest4j.TestAbortedException;

final class JUnit5TestFrameworkSupport implements TestFrameworkSupport {

    static final TestFrameworkSupport INSTANCE = new JUnit5TestFrameworkSupport();

    private JUnit5TestFrameworkSupport() {
        // hidden
    }

    @Override
    public boolean isSnapshotTest(Class<?> testClass, Method testMethod) {
        return !Modifier.isStatic(testMethod.getModifiers())
                && !Modifier.isPrivate(testMethod.getModifiers())
                && Arrays.stream(testMethod.getParameterTypes()).anyMatch(Snapshot.class::isAssignableFrom);
    }

    @Override
    public Throwable assumptionFailed(String message) {
        return new TestAbortedException(message);
    }
}
