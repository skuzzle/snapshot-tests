package de.skuzzle.test.snapshots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * A snapshot file is a plain text file containing a header and the actual serialized
 * snapshot. The header is a simple key-value format which is separated from the actual
 * snapshot contents by two line breaks (\n).
 *
 * @author Simon Taddiken
 * @since 0.0.5
 */
@API(status = Status.EXPERIMENTAL)
public final class SnapshotFile {

    private final SnapshotHeader header;
    private final String snapshot;

    private SnapshotFile(SnapshotHeader header, String snapshot) {
        this.header = Arguments.requireNonNull(header, "snapshot header must not be null");
        this.snapshot = Arguments.requireNonNull(snapshot, "snapshot must not be null");
    }

    public static SnapshotFile of(SnapshotHeader header, String snapshot) {
        return new SnapshotFile(header, snapshot);
    }

    public static SnapshotFile fromSnapshotFile(Path file) throws IOException {
        try (final var reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            return readFrom(reader);
        }
    }

    public static SnapshotFile readFrom(BufferedReader reader) throws IOException {
        final SnapshotHeader header = SnapshotHeader.readFrom(reader);

        final StringWriter snapshot = new StringWriter();
        reader.transferTo(snapshot);
        return new SnapshotFile(header, snapshot.toString());
    }

    public SnapshotFile changeHeader(SnapshotHeader newHeader) {
        return new SnapshotFile(newHeader, snapshot);
    }

    /**
     * Header information to this snapshot that are written by the framework.
     *
     * @return The header.
     */
    public SnapshotHeader header() {
        return header;
    }

    /**
     * The serialized snapshot. This is the string that has been produced by the
     * {@link SnapshotSerializer} in place.
     *
     * @return The serialized snapshot string.
     */
    public String snapshot() {
        return snapshot;
    }

    public SnapshotFile writeTo(Path snapshotFile) throws IOException {
        try (var writer = Files.newBufferedWriter(snapshotFile, StandardCharsets.UTF_8)) {
            writeTo(writer);
        }
        return this;
    }

    void writeTo(Writer writer) throws IOException {
        header.writeTo(writer);
        writer.write(snapshot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, snapshot);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof SnapshotFile
                && Objects.equals(header, ((SnapshotFile) obj).header)
                && Objects.equals(snapshot, ((SnapshotFile) obj).snapshot);
    }

    @API(status = Status.EXPERIMENTAL)
    public static final class SnapshotHeader {

        public static final String TEST_CLASS = "test-class";
        public static final String TEST_METHOD = "test-method";
        public static final String SNAPSHOT_NUMBER = "snapshot-number";
        public static final String SNAPSHOT_NAME = "snapshot-name";

        private final SortedMap<String, String> values;

        private SnapshotHeader(SortedMap<String, String> values) {
            this.values = Collections.unmodifiableSortedMap(values);
        }

        public static SnapshotHeader fromMap(Map<String, String> values) {
            return new SnapshotHeader(new TreeMap<>(values));
        }

        public static SnapshotHeader readFrom(BufferedReader reader) throws IOException {
            final SortedMap<String, String> values = new TreeMap<>();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                final String[] parts = line.split(":", 2);
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Header contains invalid line: " + line);
                }
                final String key = parts[0].trim();
                final String value = parts[1].trim();
                final String prev = values.put(key, value);

                if (prev != null) {
                    throw new IllegalArgumentException(String
                            .format("Header contains duplicate key: '%s' with values '%s' and '%s'", key, value, prev));
                }
                line = reader.readLine();
            }
            return new SnapshotHeader(values);
        }

        public String get(String key) {
            final String value = values.get(Arguments.requireNonNull(key, "key must not be null"));
            return Arguments.requireNonNull(value, "No SnapshotHeader value for key '%s' among %s", key, values);
        }

        public int getInt(String key) {
            return Integer.parseInt(get(key));
        }

        private void writeTo(Writer writer) throws IOException {
            for (final var entry : values.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.write("\n");
            }
            writer.write("\n");
        }

        @Override
        public int hashCode() {
            return Objects.hash(values);
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof SnapshotHeader
                    && Objects.equals(values, ((SnapshotHeader) obj).values);
        }

        @Override
        public String toString() {
            return "SnapshotHeader" + values;
        }

    }
}
