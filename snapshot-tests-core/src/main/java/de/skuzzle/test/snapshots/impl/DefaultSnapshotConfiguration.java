package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.DeleteOrphanedSnapshots;
import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.directories.DirectoryResolver;
import de.skuzzle.test.snapshots.io.UncheckedIO;

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
        this.testClass = Objects.requireNonNull(testClass, "testClass must not be null");
    }

    public static SnapshotConfiguration forTestClass(Class<?> testClass) {
        return new DefaultSnapshotConfiguration(testClass);
    }

    @Override
    public Path determineSnapshotDirectory() {
        final Class<?> testClass = testClass();
        final String testDirName = snapshotDirectoryName(testClass);

        final Path testDirectory = DirectoryResolver.resolve(testDirName);
        UncheckedIO.createDirectories(testDirectory);
        return testDirectory;
    }

    private String snapshotDirectoryName(Class<?> testClass) {
        final EnableSnapshotTests snapshotAssertions = testClass
                .getAnnotation(EnableSnapshotTests.class);

        final String testDirName = snapshotAssertions.snapshotDirectory().isEmpty()
                ? testClass().getName().replace('.', '/') + "_snapshots"
                : snapshotAssertions.snapshotDirectory();
        return testDirName;
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
