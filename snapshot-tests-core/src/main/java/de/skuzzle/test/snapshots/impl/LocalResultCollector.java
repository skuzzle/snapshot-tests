package de.skuzzle.test.snapshots.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;

/**
 * Collects the snapshot assertion results within a single test method.
 *
 * @author Simon Taddiken
 */
final class LocalResultCollector {

    private final List<SnapshotTestResult> results = new ArrayList<>();

    public SnapshotTestResult add(SnapshotTestResult result) {
        this.results.add(Objects.requireNonNull(result));
        return result;
    }

    public Collection<SnapshotTestResult> results() {
        return Collections.unmodifiableList(results);
    }

    public int size() {
        return results.size();
    }

    public void assertSuccess() throws Exception {
        Throwable failures = Throwables.flattenThrowables(results.stream()
                .map(SnapshotTestResult::failure)
                .flatMap(Optional::stream));
        failures = Throwables.combine(failures, assertNotCreatedInitially());
        failures = Throwables.combine(failures, assertNotUpdatedForcefully());
        Throwables.throwIfNotNull(failures);
    }

    public void assertSuccessEagerly() throws Exception {
        final Throwable failures = Throwables.flattenThrowables(results.stream()
                .map(SnapshotTestResult::failure)
                .flatMap(Optional::stream));
        Throwables.throwIfNotNull(failures);
    }

    private Throwable assertNotCreatedInitially() {
        if (wasCreatedInitially()) {
            return new AssertionError(String.format(
                    "Snapshots have been created the first time.%nRun the test again and you should see it succeed."));
        }
        return null;
    }

    private Throwable assertNotUpdatedForcefully() {
        if (wasUpdatedForcefully()) {
            return new AssertionError(String.format(
                    "Snapshots have been updated forcefully.%n"
                            + "Remove 'updateSnapshots = true' attribute from your test class and calls to 'justUpdateSnapshot()' and run the tests again."));
        }
        return null;
    }

    private boolean wasCreatedInitially() {
        return results.stream().map(SnapshotTestResult::status).anyMatch(SnapshotStatus.CREATED_INITIALLY::equals);
    }

    private boolean wasUpdatedForcefully() {
        return results.stream().map(SnapshotTestResult::status).anyMatch(SnapshotStatus.UPDATED_FORCEFULLY::equals);
    }

}
