package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.StructuralAssertions;

/**
 * 
 * @author Simon Taddiken
 * @since 1.8.0
 */
final class DefaultExecutionLifecycle implements ExecutionLifecycle {

    private final SnapshotAssertionExecutor assertionExecutor;
    private final ResultRecorder resultRecorder;

    DefaultExecutionLifecycle(SnapshotAssertionExecutor assertionExecutor, ResultRecorder resultRecorder) {
        this.resultRecorder = resultRecorder;
        this.assertionExecutor = assertionExecutor;
    }

    @Override
    public SnapshotTestResult executeAssertion(final SnapshotAssertionInput assertionInput,
            StructuralAssertions structuralAssertions) throws Exception {
        return assertionExecutor.execute(structuralAssertions, assertionInput);
    }

    @Override
    public void beforeExecution(SnapshotAssertionInput assertionInput) throws Exception {
        if (!decideAcceptNullAsActual(assertionInput) && assertionInput.actualWasNull()) {
            throw new AssertionError("Expected actual not to be null in order to take snapshot");
        }

        if (decideWriteContextFiles(assertionInput)) {
            writeAdditionalContextFiles(assertionInput);
        }
    }

    @Override
    public void afterExecution(SnapshotAssertionInput assertionInput, SnapshotTestResult result) throws Exception {
        this.resultRecorder.recordSnapshotTestResult(result);

        if (decideUpdateHeader(assertionInput, result)) {
            // persistently update the snapshot's header if for example the class or test
            // method has been renamed
            // This happens only if the snapshot was taken with a custom name or custom
            // directory or a new library version introduces changes to the header
            updatePersistedSnapshotHeader(assertionInput, result);
        }

        if (decideUpdatePersistedSnapshot(result)) {
            updatePersistedSnapshot(assertionInput);
        }

        if (!assertionInput.isSoftAssertions()) {
            this.resultRecorder.throwIfNotSuccessful();
        }
    }

    private boolean decideAcceptNullAsActual(SnapshotAssertionInput assertionInput) {
        return assertionInput.isDisableAssertion();
    }

    private boolean decideWriteContextFiles(SnapshotAssertionInput assertionInput) {
        return !assertionInput.isDisableAssertion();
    }

    private boolean decideUpdateHeader(SnapshotAssertionInput assertionInput, SnapshotTestResult result) {
        return assertionInput.isSnapshotFileAreadyExists()
                && !assertionInput.actualSnapshotFile().header().equals(result.snapshotFile().header());
    }

    private void updatePersistedSnapshotHeader(SnapshotAssertionInput assertionInput, SnapshotTestResult result)
            throws IOException {
        final Path snapshotFilePath = assertionInput.contextFiles().snapshotFile();
        result.snapshotFile().changeHeader(assertionInput.actualSnapshotFile().header()).writeTo(snapshotFilePath);
    }

    private void writeAdditionalContextFiles(SnapshotAssertionInput assertionInput)
            throws IOException {
        final SnapshotFile snapshotFile = assertionInput.actualSnapshotFile();
        final Path latestActualSnapshotFile = assertionInput.contextFiles().actualResultFile();

        if (assertionInput.alwaysPersistActualResult()) {
            snapshotFile.writeTo(latestActualSnapshotFile);
        } else {
            Files.deleteIfExists(latestActualSnapshotFile);
        }

        final Path rawSnapshotFile = assertionInput.contextFiles().rawActualResultFile();
        if (assertionInput.isAlwaysPersistRawResult()) {
            Files.writeString(rawSnapshotFile, snapshotFile.snapshot(), StandardCharsets.UTF_8);
        } else {
            Files.deleteIfExists(rawSnapshotFile);
        }
    }

    private boolean decideUpdatePersistedSnapshot(SnapshotTestResult result) {
        switch (result.status()) {
        case CREATED_INITIALLY:
        case UPDATED_FORCEFULLY:
            return true;
        default:
            return false;
        }
    }

    private void updatePersistedSnapshot(SnapshotAssertionInput assertionInput) throws IOException {
        final Path snapshotFilePath = assertionInput.contextFiles().snapshotFile();
        assertionInput.actualSnapshotFile().writeTo(snapshotFilePath);
    }
}
