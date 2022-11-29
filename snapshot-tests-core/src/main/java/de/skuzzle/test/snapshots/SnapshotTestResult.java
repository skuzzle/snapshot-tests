package de.skuzzle.test.snapshots;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Result details of a single snapshot assertion. An instance of this class is returned by
 * every DSL terminal operation.
 *
 * @author Simon Taddiken
 * @since 0.0.2
 */
@API(status = Status.EXPERIMENTAL)
public final class SnapshotTestResult {

    private final SnapshotFile snapshot;
    private final Path targetFile;
    private final SnapshotStatus status;
    private final String serializedActual;
    private final Throwable failure;

    private SnapshotTestResult(Path targetFile, SnapshotStatus status, SnapshotFile snapshotFile,
            String serializedActual,
            Throwable failure) {
        this.targetFile = Arguments.requireNonNull(targetFile);
        this.status = Arguments.requireNonNull(status);
        this.snapshot = Arguments.requireNonNull(snapshotFile);
        this.serializedActual = serializedActual;
        this.failure = failure;
    }

    @API(status = Status.INTERNAL)
    public static SnapshotTestResult forFailedTest(Path targetFile, SnapshotFile snapshotFile, String serializedActual,
            Throwable failure) {
        return new SnapshotTestResult(targetFile, SnapshotStatus.ASSERTED, snapshotFile, serializedActual,
                Arguments.requireNonNull(failure));
    }

    @API(status = Status.INTERNAL)
    public static SnapshotTestResult of(Path targetFile, SnapshotStatus status, SnapshotFile snapshotFile,
            String serializedActual) {
        return new SnapshotTestResult(targetFile, status, snapshotFile, serializedActual, null);
    }

    /**
     * The snapshot file. Note that it is possible that the file does not exist in case
     * that {@link #status()} is {@link SnapshotStatus#DISABLED}.
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
     * The contents of the persisted snapshot file.
     *
     * @return The serialized snapshot.
     * @deprecated Since 1.7.0 - Use {@link #snapshotFile()} instead.
     */
    @Deprecated(since = "1.7.0")
    @API(status = Status.DEPRECATED, since = "1.7.0")
    public SnapshotFile serializedSnapshot() {
        return this.snapshot;
    }

    /**
     * The contents of the persisted snapshot file. Note that the file's content and the
     * value of {@link #serializedActual()} can be different, even though the snapshot
     * test did not fail. For example they could differ in whitespaces if whitespaces were
     * ignored during comparison. Or they can differ in certain attributes if you used
     * structure compare with custom comparison rules.
     *
     * @return The snapshot file.
     */
    public SnapshotFile snapshotFile() {
        return this.snapshot;
    }

    /**
     * Returns the serialized string value of the actual test input. Note that this value
     * can be different from the contents of {@link #snapshotFile()} (see the method's
     * documentation for details).
     * <p>
     * Note that this value can be null in case you passed a null value into
     * {@link Snapshot#assertThat(Object)}. Unless you called
     * {@link ChooseAssertions#disabled()}, passing a null value into a snapshot test will
     * fail the test anyway.
     *
     * @return The serialized actual value.
     * @since 1.7.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.7.0")
    public String serializedActual() {
        return this.serializedActual;
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

    @Override
    public String toString() {
        return new StringBuilder()
                .append("status=").append(status)
                .append(", targetFile=").append(targetFile)
                .toString();
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
        ASSERTED,
        /**
         * No assertion has been performed and the snapshot file has not been created or
         * updated.
         *
         * @since 1.5.0
         */
        DISABLED;
    }
}
