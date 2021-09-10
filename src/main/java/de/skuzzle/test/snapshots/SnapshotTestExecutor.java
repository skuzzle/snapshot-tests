package de.skuzzle.test.snapshots;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.api.Assertions;

import de.skuzzle.test.snapshots.data.SnapshotSerializer;
import de.skuzzle.test.snapshots.data.StructuralAssertions;

class SnapshotTestExecutor {

    private final SnapshotImpl snapshot;
    private final Object actual;
    private final SnapshotSerializer snapshotSerializer;
    private final StructuralAssertions structuralAssertions;

    public SnapshotTestExecutor(SnapshotImpl snapshot, SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions,
            Object actual) {
        this.snapshot = snapshot;
        this.snapshotSerializer = snapshotSerializer;
        this.structuralAssertions = structuralAssertions;
        this.actual = actual;
    }

    private void assertEquality(String storedSnapshot, String serializedActual) throws Exception {
        try {
            structuralAssertions.assertEquals(storedSnapshot, serializedActual);
        } catch (final AssertionError e) {
            // Compare again using 'assertEquals' but retain the original error message.
            // This enables diff support in major IDEs.
            Assertions.assertThat(serializedActual)
                    .as(e.getMessage())
                    .isEqualTo(storedSnapshot);
        }
    }

    public void matchesSnapshotStructure() throws Exception {
        final String snapshotName = snapshot.determineNextSnapshotName();
        final Path snapshotFile = snapshot.determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        if (snapshot.updateSnapshots() || !Files.exists(snapshotFile)) {
            Files.writeString(snapshotFile, serializedActual, StandardCharsets.UTF_8);
            snapshot.setSnapshotsUpdated();
        } else {
            final String storedSnapshot = Files.readString(snapshotFile, StandardCharsets.UTF_8);
            assertEquality(storedSnapshot, serializedActual);
        }
    }

    public void justUpdateSnapshot() throws Exception {
        final String snapshotName = snapshot.determineNextSnapshotName();
        final Path snapshotFile = snapshot.determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        Files.writeString(snapshotFile, serializedActual, StandardCharsets.UTF_8);
        snapshot.setSnapshotsUpdated();
    }

}
