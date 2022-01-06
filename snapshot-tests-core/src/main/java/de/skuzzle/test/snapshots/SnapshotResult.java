package de.skuzzle.test.snapshots;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * Result details of a single snapshot assertion.
 *
 * @author Simon Taddiken
 * @since 0.0.2
 */
public final class SnapshotResult {

    private final SnapshotFile snapshot;
    private final Path targetFile;
    private final SnapshotStatus status;
    private final Throwable failure;

    private SnapshotResult(Path targetFile, SnapshotStatus status, SnapshotFile snapshot, Throwable failure) {
        this.targetFile = Objects.requireNonNull(targetFile);
        this.status = Objects.requireNonNull(status);
        this.snapshot = Objects.requireNonNull(snapshot);
        this.failure = failure;
    }

    public static SnapshotResult forFailedTest(Path targetFile, SnapshotFile snapshot, Throwable failure) {
        return new SnapshotResult(targetFile, SnapshotStatus.ASSERTED, snapshot,
                Objects.requireNonNull(failure));
    }

    public static SnapshotResult of(Path targetFile, SnapshotStatus status, SnapshotFile snapshot) {
        return new SnapshotResult(targetFile, status, snapshot, null);
    }

    /**
     * The snapshot file.
     *
     * @return The snapshot file.
     */
    public Path targetFile() {
        return this.targetFile;
    }

    /**
     * Whether snapshot has been created/updated or asserted.
     *
     * @return Whether snapshot has been created/updated or asserted.
     */
    public SnapshotStatus status() {
        return this.status;
    }

    /**
     * The snapshot.
     *
     * @return The serialized snapshot.
     */
    public SnapshotFile serializedSnapshot() {
        return this.snapshot;
    }

    /**
     * The exception with which the snapshot assertion failed if any.
     *
     * @return The failure.
     */
    public Optional<Throwable> failure() {
        return Optional.ofNullable(this.failure);
    }

    /**
     * Deletes the snapshot file.
     *
     * @throws IOException if an I/O error occurs
     */
    public void deleteSnapshot() throws IOException {
        Files.delete(targetFile);
    }

}
