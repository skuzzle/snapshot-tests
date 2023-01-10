package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.data.text.TextDiff;
import de.skuzzle.test.snapshots.data.text.TextDiff.Settings;
import de.skuzzle.test.snapshots.data.text.TextDiffAssertionError;
import de.skuzzle.test.snapshots.impl.SnapshotAssertionInput.ContextFilePaths;

final class SnapshotAssertionExecutor {

    /**
     * Executes a snapshot assertion and creates a {@link SnapshotTestResult} containing
     * detailed result information.
     * <p>
     * This method may have side effects. It will create the snapshot file if it did not
     * already exist and it will update it if either the header information changed since
     * the last execution or the test was run in 'force-update' mode.
     *
     * @param structuralAssertions {@link StructuralAssertions} instance that is used to
     *            do the assertion based on the sertialized snapshot.
     * @param assertionInput
     * @return The test result.
     * @throws IOException If an I/O error occurred while writing/updating a snapshot.
     */
    public SnapshotTestResult execute(StructuralAssertions structuralAssertions, SnapshotAssertionInput assertionInput)
            throws IOException {

        final SnapshotFile actualSnapshotFile = assertionInput.actualSnapshotFile();
        final ContextFilePaths contextFiles = assertionInput.contextFiles();
        final String serializedActual = actualSnapshotFile.snapshot();
        final SnapshotStatus status = determineStatus(assertionInput);

        switch (status) {
        case ASSERTED:
            final SnapshotFile existingSnapshotFile = SnapshotFile.fromSnapshotFile(contextFiles.snapshotFile);
            final String serializedExpected = existingSnapshotFile.snapshot();

            return compareTestResults(structuralAssertions, serializedExpected, serializedActual,
                    contextFiles.snapshotFile, assertionInput.lineNumberOffset(), assertionInput.contextLines())
                            .map(assertionError -> SnapshotTestResult.forFailedTest(
                                    contextFiles.snapshotFile,
                                    contextFiles.latestActualSnapshotFile,
                                    contextFiles.rawSnapshotFile,
                                    existingSnapshotFile, serializedActual, assertionError))
                            .orElseGet(() -> SnapshotTestResult.of(
                                    contextFiles.snapshotFile,
                                    contextFiles.latestActualSnapshotFile,
                                    contextFiles.rawSnapshotFile,
                                    status, existingSnapshotFile, serializedActual));

        case CREATED_INITIALLY:
        case UPDATED_FORCEFULLY:
        case DISABLED:
            return SnapshotTestResult.of(contextFiles.snapshotFile,
                    contextFiles.latestActualSnapshotFile,
                    contextFiles.rawSnapshotFile,
                    status, actualSnapshotFile, serializedActual);
        default:
            throw new IllegalStateException();
        }
    }

    private SnapshotStatus determineStatus(SnapshotAssertionInput assertionInput) {
        if (assertionInput.isDisableAssertion()) {
            return SnapshotStatus.DISABLED;
        } else if (!assertionInput.isSnapshotFileAreadyExists()) {
            return SnapshotStatus.CREATED_INITIALLY;
        } else if (assertionInput.isForceUpdateSnapshots()) {
            return SnapshotStatus.UPDATED_FORCEFULLY;
        } else {
            return SnapshotStatus.ASSERTED;
        }
    }

    private Optional<Throwable> compareTestResults(StructuralAssertions structuralAssertions, String storedSnapshot,
            String serializedActual, Path snapshotFile, int lineNumberOffset, int contextLines) {
        try {
            structuralAssertions.assertEquals(storedSnapshot, serializedActual);
            return Optional.empty();
        } catch (final AssertionError e) {
            final AssertionError diffableAssertionError = toDiffableAssertionError(e, serializedActual, storedSnapshot,
                    snapshotFile, lineNumberOffset, contextLines);
            return Optional.of(diffableAssertionError);
        } catch (final SnapshotException e) {
            return Optional.of(e);
        }
    }

    private AssertionError toDiffableAssertionError(AssertionError original, String serializedActual,
            String storedSnapshot, Path snapshotFile, int lineNumberOffset, int contextLines) {
        final StringBuilder assertionMessage = new StringBuilder();
        if (original.getMessage() != null) {
            assertionMessage.append(original.getMessage());
        }
        assertionMessage.append(System.lineSeparator())
                .append(System.lineSeparator())
                .append("Snapshot location:")
                .append(System.lineSeparator()).append("\t")
                .append(snapshotFile.toString())
                .append(System.lineSeparator());

        final TextDiff testDiff = determineDiff(original, storedSnapshot, serializedActual);
        if (testDiff.differencesDetected()) {
            assertionMessage
                    .append(System.lineSeparator())
                    .append("Full unified diff of actual result and stored snapshot:")
                    .append(System.lineSeparator())
                    .append(testDiff.renderDiffWithOffsetAndContextLines(lineNumberOffset, contextLines));
        }
        final AssertionFailedError error = new AssertionFailedError(assertionMessage.toString(),
                storedSnapshot, serializedActual, original.getCause());
        final String internalPackage = SnapshotDslResult.class.getPackageName();
        Throwables.filterStackTrace(error, element -> element.getClassName().startsWith(internalPackage));
        return error;
    }

    private TextDiff determineDiff(AssertionError original, String storedSnapshot, String serializedActual) {
        if (original instanceof TextDiffAssertionError) {
            // this is to reuse the diff that has already been created during text
            // comparison in TextDiffStructuralAssertions
            return ((TextDiffAssertionError) original).textDiff();
        } else {
            return TextDiff.compare(
                    Settings.defaultSettings()
                            .withInlineOpeningChangeMarker("~~~~")
                            .withInlineClosingChangeMarker("~~~~"),
                    storedSnapshot, serializedActual);
        }
    }
}
