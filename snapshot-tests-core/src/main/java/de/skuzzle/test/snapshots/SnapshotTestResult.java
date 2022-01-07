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
public final class SnapshotTestResult {

    private final SnapshotFile snapshot;
    private final Path targetFile;
    private final SnapshotStatus status;
    private final Throwable failure;

    private SnapshotTestResult(Path targetFile, SnapshotStatus status, SnapshotFile snapshot, Throwable failure) {
        this.targetFile = Objects.requireNonNull(targetFile);
        this.status = Objects.requireNonNull(status);
        this.snapshot = Objects.requireNonNull(snapshot);
        this.failure = failure;
    }

    public static SnapshotTestResult forFailedTest(Path targetFile, SnapshotFile snapshot, Throwable failure) {
        return new SnapshotTestResult(targetFile, SnapshotStatus.ASSERTED, snapshot,
                Objects.requireNonNull(failure));
    }

    public static SnapshotTestResult of(Path targetFile, SnapshotStatus status, SnapshotFile snapshot) {
        return new SnapshotTestResult(targetFile, status, snapshot, null);
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
    /**
     * Information about the creation of a single snapshot file.
     *
     * @author Simon Taddiken
     * @since 0.0.2
     */
    public enum SnapshotStatus {
        /**
         * Persistent snapshot file did not exist prior to executing this test. It has now
         * been created.
         */
        CREATED_INITIALLY,
        /**
         * Persistent snapshot has been forcefully updated with the actual test result.
         */
        UPDATED_FORCEFULLY,
        /**
         * Persistent snapshot has been compared against the actual test result.
         */
        ASSERTED
    }
}