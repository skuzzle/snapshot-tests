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

    private final Path snapshotFile;
    private final SnapshotStatus status;
    private final String serializedSnapshot;
    private final Throwable failure;

    private SnapshotResult(Path snapshotFile, SnapshotStatus status, String serializedSnapshot, Throwable failure) {
        this.snapshotFile = Objects.requireNonNull(snapshotFile);
        this.status = Objects.requireNonNull(status);
        this.serializedSnapshot = Objects.requireNonNull(serializedSnapshot);
        this.failure = failure;
    }

    public static SnapshotResult forFailedTest(Path snapshotFile, String serializedSnapshot, Throwable failure) {
        return new SnapshotResult(snapshotFile, SnapshotStatus.ASSERTED, serializedSnapshot,
                Objects.requireNonNull(failure));
    }

    public static SnapshotResult of(Path snapshotFile, SnapshotStatus status, String serializedSnapshot) {
        return new SnapshotResult(snapshotFile, status, serializedSnapshot, null);
    }

    /**
     * The snapshot file.
     *
     * @return The snapshot file.
     */
    public Path snapshotFile() {
        return this.snapshotFile;
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
     * The serialized snapshot.
     *
     * @return The serialized snapshot.
     */
    public String serializedSnapshot() {
        return this.serializedSnapshot;
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
        Files.delete(snapshotFile);
    }

}
