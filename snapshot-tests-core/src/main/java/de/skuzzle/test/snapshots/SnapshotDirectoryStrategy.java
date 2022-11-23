package de.skuzzle.test.snapshots;

import java.nio.file.Path;

public interface SnapshotDirectoryStrategy {

    Path determineSnapshotDirectory(Class<?> testClass, SnapshotDirectory directory) throws SnapshotException;

}
