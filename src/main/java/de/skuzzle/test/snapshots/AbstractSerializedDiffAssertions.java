package de.skuzzle.test.snapshots;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.api.Assertions;

import name.fraser.neil.plaintext.TextDiff;

abstract class AbstractSerializedDiffAssertions implements SerializedDiffAssertions {

    protected final SnapshotImpl snapshot;
    protected final Object actual;

    public AbstractSerializedDiffAssertions(SnapshotImpl snapshot, Object actual) {
        this.snapshot = snapshot;
        this.actual = actual;
    }

    private void assertEquality(String storedSnapshot, String serializedActual) throws Exception {
        try {
            compareToSnapshot(storedSnapshot, serializedActual);
        } catch (final AssertionError e) {
            Assertions.assertThat(serializedActual).as(e.getMessage()).isEqualTo(storedSnapshot);
        }
    }

    @Override
    public void matchesSnapshotStructure() throws Exception {
        final String snapshotName = snapshot.determineNextSnapshotName();
        final Path snapshotFile = snapshot.determineSnapshotFile(snapshotName);
        final String serialized = serializeToString(actual);

        if (Files.exists(snapshotFile)) {
            final String storedSnapshot = Files.readString(snapshotFile, StandardCharsets.UTF_8);
            assertEquality(storedSnapshot, serialized);
        } else {
            Files.writeString(snapshotFile, serialized, StandardCharsets.UTF_8);
        }
    }

    @Override
    public void matchesSnapshotText() throws Exception {
        final String snapshotName = snapshot.determineNextSnapshotName();
        final Path snapshotFile = snapshot.determineSnapshotFile(snapshotName);
        final String serializedActual = serializeToString(actual);

        if (Files.exists(snapshotFile)) {
            final String storedSnapshot = Files.readString(snapshotFile, StandardCharsets.UTF_8);
            final TextDiff textDiff = TextDiff.diffOf(storedSnapshot, serializedActual);
            if (textDiff.hasDifference()) {
                Assertions.assertThat(serializedActual)
                        .as("Stored snapshot doesn't match actual result.%nDiff:%n%s", textDiff)
                        .isEqualTo(storedSnapshot);
            }
        } else {
            Files.writeString(snapshotFile, serializedActual, StandardCharsets.UTF_8);
        }
    }

    protected abstract void compareToSnapshot(String storedSnapshot, String serializedActual) throws Exception;

    protected abstract String serializeToString(Object object) throws Exception;
}
