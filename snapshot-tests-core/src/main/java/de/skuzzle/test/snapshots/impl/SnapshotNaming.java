package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;

final class SnapshotNaming {

    public static boolean isSnapshotFile(Path path) {
        return path.getFileName().toString().endsWith(".snapshot");
    }

    /**
     * @deprecated Implementation should be adjusted so that it uses the information from
     *             the snapshot header instead of relying on the file name (See
     *             https://github.com/skuzzle/snapshot-tests/issues/8)
     */
    @Deprecated
    public static boolean isSnapshotFileForMethod(Path path, Method testMethod) {
        try {
            return SnapshotFile.fromSnapshotFile(path)
                    .header()
                    .get(SnapshotHeader.TEST_METHOD)
                    .equals(testMethod.getName());
        } catch (final IOException e) {
            throw new RuntimeException();
        }
    }

    public static String getSnapshotName(Method testMethod, int counter) {
        return testMethod.getName() + "_" + counter;
    }

    public static String getSnapshotFileName(String snapshotName) {
        return snapshotName + ".snapshot";
    }

}
