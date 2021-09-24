package de.skuzzle.test.snapshots.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import de.skuzzle.test.snapshots.SnapshotResult;
import de.skuzzle.test.snapshots.SnapshotStatus;

class LocalResultCollector {

    private final List<SnapshotResult> results = new ArrayList<>();

    public SnapshotResult add(SnapshotResult result) {
        this.results.add(Objects.requireNonNull(result));
        return result;
    }

    public Collection<SnapshotResult> results() {
        return Collections.unmodifiableList(results);
    }

    public int size() {
        return results.size();
    }

    public void assertSuccess() throws Exception {
        Throwable failures = Throwables.flattenThrowables(results.stream()
                .map(SnapshotResult::failure)
                .flatMap(Optional::stream));
        failures = Throwables.combine(failures, assertNotCreatedInitially());
        failures = Throwables.combine(failures, assertNotUpdatedForcefully());
        Throwables.throwIfNotNull(failures);
    }

    // TODO: better name
    public void assertSuccessOther() throws Exception {
        final Throwable failures = Throwables.flattenThrowables(results.stream()
                .map(SnapshotResult::failure)
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
        return results.stream().map(SnapshotResult::status).anyMatch(SnapshotStatus.CREATED_INITIALLY::equals);
    }

    private boolean wasUpdatedForcefully() {
        return results.stream().map(SnapshotResult::status).anyMatch(SnapshotStatus.UPDATED_FORCEFULLY::equals);
    }

}
