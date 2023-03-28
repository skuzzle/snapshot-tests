package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.DeleteOrphanedSnapshots;
import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.SnapshotTestOptions;
import de.skuzzle.test.snapshots.SnapshotTestOptions.DiffLineNumbers;
import de.skuzzle.test.snapshots.validation.Arguments;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Relevant configuration for executing snapshot tests in a test class that is annotated
 * with {@link EnableSnapshotTests}.
 *
 * @author Simon Taddiken
 * @since 1.1.0
 */
@API(status = Status.INTERNAL, since = "1.1.0")
final class DefaultSnapshotConfiguration implements SnapshotConfiguration {

    private static final SnapshotTestOptions DEFAULT_SNAPSHOT_TEST_OPTIONS = DefaultSnapshotTestOptions.class
            .getAnnotation(SnapshotTestOptions.class);

    private static final String FORCE_UPDATE_SYSTEM_PROPERTY = "forceUpdateSnapshots";
    private static final String DELETE_ORPHANS_SYSTEM_PROPERTY = "deleteOrphanedSnapshots";

    // default number of context lines that will be printed around changes in huge unified
    // diffs
    private static final int DEFAULT_CONTEXT_LINES = SnapshotTestOptions.DEFAULT_CONTEXT_LINES;

    private final TestClass testClass;

    private DefaultSnapshotConfiguration(Class<?> testClass) {
        Arguments.requireNonNull(testClass, "testClass must not be null");
        this.testClass = TestClass.wrap(testClass);
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
        return testClass.testClass();
    }

    @Override
    public boolean isDeleteOrphanedSnapshots() {
        return testClass().isAnnotationPresent(DeleteOrphanedSnapshots.class)
                || System.getProperties().keySet().stream()
                        .map(Object::toString)
                        .anyMatch(DELETE_ORPHANS_SYSTEM_PROPERTY::equalsIgnoreCase);
    }

    private boolean isForceUpdateSnapshotsGlobal() {
        if (testClass().isAnnotationPresent(ForceUpdateSnapshots.class)) {
            return true;
        }

        // System property
        return System.getProperties().keySet().stream()
                .map(Object::toString)
                .anyMatch(FORCE_UPDATE_SYSTEM_PROPERTY::equalsIgnoreCase);
    }

    @Override
    public boolean isForceUpdateSnapshots(Method testMethod) {
        // Annotation on test method
        if (testMethod.isAnnotationPresent(ForceUpdateSnapshots.class)) {
            return true;
        }

        return isForceUpdateSnapshotsGlobal();
    }

    private SnapshotTestOptions determineOptions(Method testMethod) {
        SnapshotTestOptions options = testMethod.getAnnotation(SnapshotTestOptions.class);
        if (options != null) {
            return options;
        }
        options = testClass.getAnnotation(SnapshotTestOptions.class);
        if (options != null) {
            return options;
        }
        return DEFAULT_SNAPSHOT_TEST_OPTIONS;
    }

    @Override
    public boolean alwaysPersistActualResult(Method testMethod) {
        final var snapshotTestOptions = determineOptions(testMethod);
        return snapshotTestOptions.alwaysPersistActualResult();
    }

    @Override
    public boolean alwaysPersistRawResult(Method testMethod) {
        final var snapshotTestOptions = determineOptions(testMethod);
        return snapshotTestOptions.alwaysPersistRawResult();
    }

    @Override
    public int textDiffContextLines(Method testMethod) {
        final var snapshotTestOptions = determineOptions(testMethod);
        return snapshotTestOptions.textDiffContextLines();
    }

    @Override
    public boolean addOffsetToReportedLinenumbers(Method testMethod) {
        final var snapshotTestOptions = determineOptions(testMethod);
        return snapshotTestOptions.renderLineNumbers() == DiffLineNumbers.ACCODRDING_TO_PERSISTED_SNAPSHOT_FILE;
    }

    @Override
    public SnapshotTestOptions.NormalizeLineEndings normalizeLineEndings(Method testMethod) {
        final var snapshotTestOptions = determineOptions(testMethod);
        return snapshotTestOptions.normalizeLineEndings();
    }

    @Override
    public boolean isSoftAssertions() {
        return false;
    }

    @Override
    public String toString() {
        return "DefaultSnapshotConfiguration[" + testClass.getName() + "]";
    }

    @SnapshotTestOptions
    private static class DefaultSnapshotTestOptions {

    }
}
