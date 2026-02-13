package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.util.function.Function;

import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * delegates the outcome of snapshot assertions to both the current
 * {@link SnapshotTestContext} and the {@link LocalResultCollector} so that
 * {@link SnapshotDslImpl} only needs a single dependency.
 *
 * @author Simon Taddiken
 * @since 1.8.0
 */
final class ResultRecorder {

    private final LocalResultCollector localResultCollector;
    private final SnapshotTestContext context;

    private ResultRecorder(LocalResultCollector localResultCollector, SnapshotTestContext context) {
        this.localResultCollector = Arguments.requireNonNull(localResultCollector);
        this.context = Arguments.requireNonNull(context);
    }

    static ResultRecorder forFreshTestMethod(SnapshotTestContext context, Method testMethod) {
        final Function<String, Throwable> assumptionFailedConstructor = context
                .testFrameworkSupport()::assumptionFailed;
        final boolean allowMultipleSnapshotsWithSameName = context.snapshotConfiguration()
                .allowMultipleSnapshotsWithSameName(testMethod);

        final LocalResultCollector localResultCollector = new LocalResultCollector(
                assumptionFailedConstructor,
                allowMultipleSnapshotsWithSameName);
        return new ResultRecorder(localResultCollector, context);
    }

    public void recordSnapshotTestResult(SnapshotTestResult result) {
        localResultCollector.recordSnapshotTestResult(result);
        context.recordSnapshotTestResult(result);
    }

    public void throwIfNotSuccessfulOrCreatedInitiallyOrUpdatedForcefully() throws Exception {
        localResultCollector.throwIfNotSuccessfulOrCreatedInitiallyOrUpdatedForcefully();
    }

    public void throwIfNotSuccessful() throws Exception {
        localResultCollector.throwIfNotSuccessful();
    }

    public int size() {
        return localResultCollector.size();
    }

}
