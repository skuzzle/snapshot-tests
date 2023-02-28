package de.skuzzle.test.snapshots.impl;

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

    static ResultRecorder forFreshTestMethod(SnapshotTestContext context) {
        return new ResultRecorder(new LocalResultCollector(), context);
    }

    public void recordSnapshotTestResult(SnapshotTestResult result) {
        localResultCollector.recordSnapshotTestResult(result);
        context.recordSnapshotTestResult(result);
    }

    public void throwIfNotSuccessfulOrCreatedInitiallyOrUpdatedForcefully(boolean softAssertions) throws Exception {
        localResultCollector.throwIfNotSuccessfulOrCreatedInitiallyOrUpdatedForcefully(softAssertions);
    }

    public void throwIfNotSuccessful() throws Exception {
        localResultCollector.throwIfNotSuccessful();
    }

    public int size() {
        return localResultCollector.size();
    }

}
