package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.directories.DirectoryResolver;

final class SnapshotDirectoryResolver {

    public static Path resolveSnapshotDirectory(SnapshotConfiguration configuration) throws IOException {
        final String testDirName = configuration.snapshotDirecotry();

        final Path testDirectory = DirectoryResolver.resolve(testDirName);
        Files.createDirectories(testDirectory);
        return testDirectory;
    }

    private SnapshotDirectoryResolver() {
        // hidden
    }
}
