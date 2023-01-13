package de.skuzzle.test.snapshots.junit4;

import java.lang.reflect.Method;
import java.nio.file.Path;

import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseName;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.impl.SnapshotConfiguration;
import de.skuzzle.test.snapshots.impl.SnapshotTestContext;
import de.skuzzle.test.snapshots.validation.State;

public final class SnapshotRule implements CombinedRule, Snapshot {

    private SnapshotTestContext context;
    private Snapshot currentSnapshot;

    public static SnapshotRule enableSnapshotTests() {
        return new SnapshotRule();
    }

    @Override
    public void beforeClass(Description description) throws Exception {
        final Class<?> testClass = description.getTestClass();
        final SnapshotConfiguration configuration = SnapshotConfiguration.defaultConfigurationFor(testClass);
        context = SnapshotTestContext.forConfiguration(configuration);
        new TestClass(testClass)
                .getAnnotatedMethods(Ignore.class).stream()
                .map(FrameworkMethod::getMethod)
                .forEach(context::recordFailedOrSkippedTest);
    }

    private SnapshotTestContext currentContext() {
        State.check(context != null,
                "No SnapshotTestContext found. Make sure that the Rule instance is static and annotated with both @Rule *and* @ClassRule");
        return context;
    }

    private Snapshot currentSnapshot() {
        State.check(currentSnapshot != null, "There is no current snapshot");
        return currentSnapshot;
    }

    private Method getTestMethod(Description description) {
        try {
            return description.getTestClass().getMethod(description.getMethodName());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Could not determine test method");
        }
    }

    @Override
    public void afterClass(Description description) {
        currentContext().detectOrCleanupOrphanedSnapshots();
    }

    @Override
    public void before(Description description) throws Exception {
        final Method testMethod = getTestMethod(description);
        currentSnapshot = currentContext().createSnapshotTestFor(testMethod);
    }

    @Override
    public void after(Description description) throws Exception {
        try {
            currentContext().finalizeSnapshotTest();
        } catch (final AssumptionViolatedException e) {
            onSkippedTest(description);
            throw e;
        } finally {
            currentSnapshot = null;
        }
    }

    @Override
    public ChooseDataFormat assertThat(Object actual) {
        return currentSnapshot().assertThat(actual);
    }

    @Override
    public ChooseActual namedAccordingTo(SnapshotNaming namingStrategy) {
        return currentSnapshot().namedAccordingTo(namingStrategy);
    }

    @Override
    public ChooseName in(Path directory) {
        return currentSnapshot().in(directory);
    }

    @Override
    public void onFailedTest(Description description) {
        final Method testMethod = getTestMethod(description);
        currentContext().recordFailedOrSkippedTest(testMethod);
    }

    @Override
    public void onSkippedTest(Description description) {
        final Method testMethod = getTestMethod(description);
        currentContext().recordFailedOrSkippedTest(testMethod);
    }
}
