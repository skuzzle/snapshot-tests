package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Simon Taddiken
 * @since 1.7.0
 * @deprecated Since 1.7.0 - Only introduced for compatibility reasons. Will be removed
 * with 2.0.0
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

    private boolean isForceUpdateSnapshotsGlobal() {
        // Annotation on test class
        final boolean valueFromLegacyAnnotation = testClass()
                .getAnnotation(EnableSnapshotTests.class)
                .forceUpdateSnapshots();

        return valueFromLegacyAnnotation;
    }

    @Override
    public boolean isForceUpdateSnapshots(Method testMethod) {
        return delegate.isForceUpdateSnapshots(testMethod) || isForceUpdateSnapshotsGlobal();
    }

    @Override
    public boolean isSoftAssertions() {
        return testClass()
                .getAnnotation(EnableSnapshotTests.class)
                .softAssertions();
    }

    @Override
    public boolean alwaysPersistActualResult(Method testMethod) {
        return delegate.alwaysPersistActualResult(testMethod);
    }

    @Override
    public boolean alwaysPersistRawResult(Method testMethod) {
        return delegate.alwaysPersistRawResult(testMethod);
    }

    @Override
    public int textDiffContextLines(Method testMethod) {
        return delegate.textDiffContextLines(testMethod);
    }

    @Override
    public boolean addOffsetToReportedLinenumbers(Method testMethod) {
        return delegate.addOffsetToReportedLinenumbers(testMethod);
    }
}
