package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;

final class SnapshotNaming {

    public static boolean isSnapshotFile(Path path) {
        return path.getFileName().toString().endsWith(".snapshot");
    }

    public static boolean isSnapshotFileForMethod(Path path, Method testMethod) {
        return path.getFileName().toString().startsWith(testMethod.getName() + "_");
    }

    public static String getSnapshotName(Method testMethod, int counter) {
        return testMethod.getName() + "_" + counter;
    }

    public static String getSnapshotFileName(String snapshotName) {
        return snapshotName + ".snapshot";
    }

}
