package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.skuzzle.test.snapshots.ContextFiles;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.reflection.StackTraces;
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

    public void throwIfNotSuccessfulOrCreatedInitiallyOrUpdatedForcefully(boolean softAssertions) throws Exception {
        Throwable failures = null;
        if (softAssertions) {
            failures = Throwables.flattenThrowables(results.stream()
                    .map(SnapshotTestResult::failure)
                    .flatMap(Optional::stream));
        }

        failures = Throwables.combine(failures, failIfCreatedInitially());
        failures = Throwables.combine(failures, failIfUpdatedForcefully());
        failures = Throwables.combine(failures, abortIfNoneFailedAndAtLeastOneWasDisabled());

        final String internalPackage = SnapshotDslResult.class.getPackageName();
        StackTraces.filterStackTrace(failures, element -> element.getClassName().startsWith(internalPackage));
        Throwables.throwIfNotNull(failures);
    }

    public void throwIfNotSuccessful() throws Exception {
        final Throwable failures = Throwables.flattenThrowables(results.stream()
                .map(SnapshotTestResult::failure)
                .flatMap(Optional::stream));

        final String internalPackage = SnapshotDslResult.class.getPackageName();
        StackTraces.filterStackTrace(failures, element -> element.getClassName().startsWith(internalPackage));
        Throwables.throwIfNotNull(failures);
    }

    private Throwable failIfCreatedInitially() {
        if (wasAnyCreatedInitially()) {
            final String createdSnapshotFilePaths = results.stream()
                    .filter(result -> result.status() == SnapshotStatus.CREATED_INITIALLY)
                    .map(SnapshotTestResult::contextFiles)
                    .map(ContextFiles::snapshotFile)
                    .map(Path::toString)
                    .collect(Collectors.joining(System.lineSeparator()));
            return new AssertionError(String.format(
                    "Snapshots have been created the first time.%nRun the test again and you should see it succeed.%n%nCreated snapshot files:%n%s",
                    createdSnapshotFilePaths));
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

    private Throwable abortIfNoneFailedAndAtLeastOneWasDisabled() {
        if (wasAtLeastOneDisabledAndAllOthersSuccessful()) {
            return AssumptionExceptionDetector.assumptionFailed("Assertion was disabled")
                    .orElse(null);
        }
        return null;
    }

    private boolean wasAtLeastOneDisabledAndAllOthersSuccessful() {
        final boolean atLeastOneDisabled = results.stream().map(SnapshotTestResult::status)
                .anyMatch(SnapshotStatus.DISABLED::equals);
        final boolean noFailed = results.stream().map(SnapshotTestResult::failure).noneMatch(Optional::isPresent);
        return atLeastOneDisabled && noFailed && !wasAnyCreatedInitially() && !wasAnyUpdatedForcefully();
    }

    private boolean wasAnyCreatedInitially() {
        return results.stream().map(SnapshotTestResult::status).anyMatch(SnapshotStatus.CREATED_INITIALLY::equals);
    }

    private boolean wasAnyUpdatedForcefully() {
        return results.stream().map(SnapshotTestResult::status).anyMatch(SnapshotStatus.UPDATED_FORCEFULLY::equals);
    }

}
