package de.skuzzle.test.snapshots.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Collects the snapshot assertion results within a single test method.
 *
 * @author Simon Taddiken
 */
final class LocalResultCollector {

    private final List<SnapshotTestResult> results = new ArrayList<>();

    public void recordSnapshotTestResult(SnapshotTestResult result) {
        this.results.add(Arguments.requireNonNull(result));
    }

    public int size() {
        return results.size();
    }

    public void throwIfNotSuccessfulOrCreatedInitiallyOrUpdatedForcefully() throws Exception {
        Throwable failures = Throwables.flattenThrowables(results.stream()
                .map(SnapshotTestResult::failure)
                .flatMap(Optional::stream));
        failures = Throwables.combine(failures, failIfCreatedInitially());
        failures = Throwables.combine(failures, failIfUpdatedForcefully());
        Throwables.throwIfNotNull(failures);
    }

    public void throwIfNotSuccessful() throws Exception {
        final Throwable failures = Throwables.flattenThrowables(results.stream()
                .map(SnapshotTestResult::failure)
                .flatMap(Optional::stream));
        Throwables.throwIfNotNull(failures);
    }

    private Throwable failIfCreatedInitially() {
        if (wasAnyCreatedInitially()) {
            return new AssertionError(String.format(
                    "Snapshots have been created the first time.%nRun the test again and you should see it succeed."));
        }
        return null;
    }

    private Throwable failIfUpdatedForcefully() {
        if (wasAnyUpdatedForcefully()) {
            return new AssertionError(String.format(
                    "Snapshots have been updated forcefully.%n"
                            + "Remove '@ForceUpdateSnapshots' annotation from your test class and calls to 'justUpdateSnapshot()' then run the tests again."));
        }
        return null;
    }

    private boolean wasAnyCreatedInitially() {
        return results.stream().map(SnapshotTestResult::status).anyMatch(SnapshotStatus.CREATED_INITIALLY::equals);
    }

    private boolean wasAnyUpdatedForcefully() {
        return results.stream().map(SnapshotTestResult::status).anyMatch(SnapshotStatus.UPDATED_FORCEFULLY::equals);
    }

}
