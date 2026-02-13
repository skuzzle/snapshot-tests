package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.validation.Arguments;

final class SnapshotDirectoryWriter {

    static final SnapshotDirectoryWriter INSTANCE = new SnapshotDirectoryWriter();

    private SnapshotDirectoryWriter() {
    }

    public void writeSnapshot(SnapshotFile snapshotFile, Path snapshotFilePath) throws IOException {
        Arguments.requireNonNull(snapshotFile, "snapshotFile must not be null");
        Arguments.requireNonNull(snapshotFilePath, "snapshotFilePath must not be null");
        ensureSnapshotDirectoryExists(snapshotFilePath);
        snapshotFile.writeTo(snapshotFilePath);
    }

    public void writeString(String s, Path filePath) throws IOException {
        Arguments.requireNonNull(s, "string must not be null");
        Arguments.requireNonNull(filePath, "filePath must not be null");

        Files.writeString(filePath, s, StandardCharsets.UTF_8);
    }

    private void ensureSnapshotDirectoryExists(Path snapshotFilePath) throws IOException {
        final Path snapshotDirectory = snapshotFilePath.getParent();
        Files.createDirectories(snapshotDirectory);
    }
}
