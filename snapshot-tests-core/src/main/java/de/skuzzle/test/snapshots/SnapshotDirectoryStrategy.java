package de.skuzzle.test.snapshots;

import java.nio.file.Path;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Strategy interface which can be used to determine a custom snapshot directory. Use with
 * {@link SnapshotDirectory#determinedBy()}.
 *
 * @author Simon Taddiken
 * @since 1.7.0
 */
@API(status = Status.EXPERIMENTAL, since = "1.7.0")
public interface SnapshotDirectoryStrategy {

    /**
     * Determine the directory into which snapshots will be persisted.
     *
     * @param testClass The test class for which snapshot tests are enabled.
     * @param directory The {@link SnapshotDirectory} annotation instance.
     * @return The path at which snapshot files will be persisted.
     * @throws SnapshotException Can be thrown by implementors in case determining the
     *             directory is not possible.
     */
    Path determineSnapshotDirectory(Class<?> testClass, SnapshotDirectory directory) throws SnapshotException;

}
