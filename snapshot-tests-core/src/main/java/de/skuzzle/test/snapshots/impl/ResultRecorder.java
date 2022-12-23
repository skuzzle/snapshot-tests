package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.validation.Arguments;

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
