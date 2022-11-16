package de.skuzzle.test.snapshots;

import static java.util.Comparator.comparing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * A snapshot file is a plain text file containing a header and the actual serialized
 * snapshot. The header is a simple key-value format which is separated from the actual
 * snapshot contents by two line breaks (\n).
 * <p>
 * This class is immutable.
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

    /**
     * Creates a new {@link SnapshotFile} instance with the given header and the same
     * snapshot contents.
     *
     * @param newHeader The new header.
     * @return A new {@link SnapshotFile} instance.
     * @since 1.2.1
     */
    @API(status = Status.EXPERIMENTAL, since = "1.2.1")
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

    /**
     * Writes this object to the specified file, potentially overriding it if it already
     * exists.
     *
     * @param snapshotFile The target file to write to.
     * @return The instance.
     * @throws IOException If an IO error occurs during writing.
     */
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
        /**
         * Stores per snapshot whether it had been taken with a dynamic directory. This
         * information helps to improve static orphan detection.
         *
         * @since 1.2.2
         */
        @API(status = Status.EXPERIMENTAL, since = "1.2.2")
        public static final String DYNAMIC_DIRECTORY = "dynamic-directory";

        private final Map<String, String> values;

        private SnapshotHeader(Map<String, String> values) {
            values.forEach((key, value) -> {
                Arguments.check(key.indexOf('\n') < 0 && key.indexOf('\r') < 0,
                        "Snapshot header key must not contain linebreaks but was '%s'='%s'", key, value);
                Arguments.check(value.indexOf('\n') < 0 && value.indexOf('\r') < 0,
                        "Snapshot header values must not contain linebreaks but was '%s'='%s'", key, value);
            });
            this.values = Collections.unmodifiableMap(values);
        }

        public static SnapshotHeader fromMap(Map<String, String> values) {
            return new SnapshotHeader(Map.copyOf(values));
        }

        public static SnapshotHeader readFrom(BufferedReader reader) throws IOException {
            final Map<String, String> values = new HashMap<>();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                final String[] parts = line.split(":", 2);
                Arguments.check(parts.length == 2, "Header contains invalid line: " + line);
                final String key = parts[0].trim();
                final String value = parts[1].trim();
                final String prev = values.put(key, value);

                Arguments.check(prev == null,
                        "Header contains duplicate key: '%s' with values '%s' and '%s'", key, value, prev);
                line = reader.readLine();
            }
            return new SnapshotHeader(values);
        }

        public String getOrDefault(String key, String defaultValue) {
            final String value = values.get(Arguments.requireNonNull(key, "key must not be null"));
            return value == null ? defaultValue : value;
        }

        public String get(String key) {
            final String value = values.get(Arguments.requireNonNull(key, "key must not be null"));
            return Arguments.requireNonNull(value, "No SnapshotHeader value for key '%s' among %s", key, values);
        }

        public int getInt(String key) {
            return Integer.parseInt(get(key));
        }

        /**
         * Retrieves a boolean header value.
         *
         * @param key The name of the header to retrieve.
         * @param defaultValue Default value if no header for that key exists.
         * @return The boolean value.
         * @since 1.2.2
         */
        @API(status = Status.EXPERIMENTAL, since = "1.2.2")
        public boolean getBoolean(String key, boolean defaultValue) {
            return Boolean.parseBoolean(getOrDefault(key, "" + defaultValue));
        }

        private void writeTo(Writer writer) throws IOException {
            // sort entries by key to ensure deterministic results
            final Iterable<Entry<String, String>> sortedEntries = values.entrySet().stream()
                    .sorted(comparing(Entry::getKey))::iterator;

            for (final var entry : sortedEntries) {
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
