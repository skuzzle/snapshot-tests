package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.validation.Arguments;
import org.apiguardian.api.API;

import java.lang.annotation.Annotation;

/**
 * Internal wrapper around the test class. Allows to query it for annotations by also traversing all enclosing classes.
 * This allows to source configuration options not only from the direct test class but also from any of its enclosing
 * parents.
 *
 * @since 1.9.0
 */
@API(status = API.Status.INTERNAL, since = "1.9.0")
final class TestClass {
    private final Class<?> testClass;

    private TestClass(Class<?> testClass) {
        this.testClass = Arguments.requireNonNull(testClass);
    }

    public static TestClass wrap(Class<?> testClass) {
        return new TestClass(testClass);
    }

    public Class<?> testClass() {
        return testClass;
    }

    String getName() {
        return testClass.getName();
    }

    <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        Class<?> current = testClass;
        while (current != null) {
            T annotation = current.getAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
            current = current.getEnclosingClass();
        }
        return null;
    }

    boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return getAnnotation(annotationType) != null;
    }
}
