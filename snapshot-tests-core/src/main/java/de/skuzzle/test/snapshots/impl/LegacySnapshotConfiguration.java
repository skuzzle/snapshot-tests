package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.EnableSnapshotTests;

/**
 *
 * @author Simon Taddiken
 * @since 1.7.0
 * @deprecated Since 1.7.0 - Only introduced for compatibility reasons. Will be removed
 *             with 2.0.0
 */
@Deprecated(since = "1.7.0", forRemoval = true)
@API(status = Status.DEPRECATED, since = "1.7.0")
final class LegacySnapshotConfiguration implements SnapshotConfiguration {

    private final SnapshotConfiguration delegate;

    private LegacySnapshotConfiguration(Class<?> testClass) {
        this.delegate = DefaultSnapshotConfiguration.forTestClass(testClass);
    }

    public static SnapshotConfiguration forTestClass(Class<?> testClass) {
        return new LegacySnapshotConfiguration(testClass);
    }

    @Override
    public Path determineSnapshotDirectory() {
        return delegate.determineSnapshotDirectory();
    }

    @Override
    public Class<?> testClass() {
        return delegate.testClass();
    }

    @Override
    public boolean isDeleteOrphanedSnapshots() {
        return delegate.isDeleteOrphanedSnapshots();
    }

    @Override
    public boolean isForceUpdateSnapshotsGlobal() {
        // Annotation on test class
        final boolean valueFromLegacyAnnotation = testClass()
                .getAnnotation(EnableSnapshotTests.class)
                .forceUpdateSnapshots();
        if (valueFromLegacyAnnotation) {
            return true;
        }
        return delegate.isForceUpdateSnapshotsGlobal();
    }

    @Override
    public boolean isForceUpdateSnapshotsLocal(Method testMethod) {
        return delegate.isForceUpdateSnapshotsLocal(testMethod) || isForceUpdateSnapshotsGlobal();
    }

    @Override
    public boolean isSoftAssertions() {
        return testClass()
                .getAnnotation(EnableSnapshotTests.class)
                .softAssertions();
    }
}
