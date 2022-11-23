package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.DeleteOrphanedSnapshots;
import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Relevant configuration for executing snapshot tests in a test class that is annotated
 * with {@link EnableSnapshotTests}.
 *
 * @author Simon Taddiken
 * @since 1.1.0
 */
@API(status = Status.INTERNAL, since = "1.1.0")
final class DefaultSnapshotConfiguration implements SnapshotConfiguration {

    private static final String FORCE_UPDATE_SYSTEM_PROPERTY = "forceUpdateSnapshots";
    private static final String DELETE_ORPHANS_SYSTEM_PROPERTY = "deleteOrphanedSnapshots";

    private final Class<?> testClass;

    private DefaultSnapshotConfiguration(Class<?> testClass) {
        this.testClass = Arguments.requireNonNull(testClass, "testClass must not be null");
    }

    public static SnapshotConfiguration forTestClass(Class<?> testClass) {
        return new DefaultSnapshotConfiguration(testClass);
    }

    @Override
    public Path determineSnapshotDirectory() {
        return DetermineSnapshotDirectory.forTestclass(testClass);
    }

    @Override
    public Class<?> testClass() {
        return testClass;
    }

    @Override
    public boolean isDeleteOrphanedSnapshots() {
        return testClass().isAnnotationPresent(DeleteOrphanedSnapshots.class)
                || System.getProperties().keySet().stream()
                        .map(Object::toString)
                        .anyMatch(DELETE_ORPHANS_SYSTEM_PROPERTY::equalsIgnoreCase);
    }

    @Override
    public boolean isForceUpdateSnapshotsGlobal() {
        // Annotation on test class
        final boolean valueFromLegacyAnnotation = testClass()
                .getAnnotation(EnableSnapshotTests.class)
                .forceUpdateSnapshots();
        if (valueFromLegacyAnnotation
                || testClass().isAnnotationPresent(ForceUpdateSnapshots.class)) {
            return true;
        }

        // System property
        return System.getProperties().keySet().stream()
                .map(Object::toString)
                .anyMatch(FORCE_UPDATE_SYSTEM_PROPERTY::equalsIgnoreCase);
    }

    @Override
    public boolean isForceUpdateSnapshotsLocal(Method testMethod) {
        // Annotation on test method
        if (testMethod.isAnnotationPresent(ForceUpdateSnapshots.class)) {
            return true;
        }

        return isForceUpdateSnapshotsGlobal();
    }

    @Override
    public boolean isSoftAssertions() {
        return testClass()
                .getAnnotation(EnableSnapshotTests.class)
                .softAssertions();
    }
}
