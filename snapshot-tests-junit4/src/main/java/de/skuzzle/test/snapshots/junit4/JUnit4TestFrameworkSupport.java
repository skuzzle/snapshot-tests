package de.skuzzle.test.snapshots.junit4;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.skuzzle.test.snapshots.impl.TestFrameworkSupport;

import org.junit.AssumptionViolatedException;

final class JUnit4TestFrameworkSupport implements TestFrameworkSupport {
    static final TestFrameworkSupport INSTANCE = new JUnit4TestFrameworkSupport();

    private JUnit4TestFrameworkSupport() {
        // hidden
    }

    @Override
    public boolean isSnapshotTest(Class<?> testClass, Method testMethod) {
        return !Modifier.isStatic(testMethod.getModifiers())
                && !Modifier.isPrivate(testMethod.getModifiers());
    }

    @Override
    public Throwable assumptionFailed(String message) {
        return new AssumptionViolatedException(message);
    }
}
