package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.ContextFiles;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import de.skuzzle.test.snapshots.io.FileName;

final class InternalSnapshotNaming {

    public static String getSnapshotFileName(String snapshotName) {
        return snapshotName + ".snapshot";
    }

    public static String getSnapshotFileNameActual(String snapshotName) {
        return snapshotName + ".snapshot_actual";
    }

    public static String getSnapshotFileNameRaw(String snapshotName) {
        return snapshotName + ".snapshot_raw";
    }

    public static boolean isSnapshotFile(Path path) {
        return path.getFileName().toString().endsWith(".snapshot");
    }

    public static ContextFiles contextFilesForSnapshotFile(Path snapshotFile) {
        final String snapshotFileName = FileName.fromPath(snapshotFile);
        final String actualFileName = getSnapshotFileNameActual(snapshotFileName);
        final String rawFileName = getSnapshotFileNameRaw(snapshotFileName);

        final Path snapshotDirectory = snapshotFile.getParent();
        return ContextFiles.of(
                snapshotDirectory.resolve(snapshotFileName),
                snapshotDirectory.resolve(actualFileName),
                snapshotDirectory.resolve(rawFileName));

    }

    public static boolean isSnapshotFileForMethod(Path path, Method testMethod) {
        try {
            return SnapshotFile.fromSnapshotFile(path)
                    .header()
                    .get(SnapshotHeader.TEST_METHOD)
                    .equals(testMethod.getName());
        } catch (final IOException e) {
            throw new UncheckedIOException(
                    "Error determining snapshot header for method " + testMethod + " from snapshot file at " + path, e);
        }
    }

}
