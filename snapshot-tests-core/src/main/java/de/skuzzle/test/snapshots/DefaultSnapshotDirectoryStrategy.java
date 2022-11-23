package de.skuzzle.test.snapshots;

import java.nio.file.Path;

/**
 * Just a tagging class file as default value for
 * {@link SnapshotDirectory#determinedBy()}.
 *
 * @author Simon Taddiken
 * @since 1.7.0
 */
final class DefaultSnapshotDirectoryStrategy implements SnapshotDirectoryStrategy {

    // Implementation NOTE: The class's full qualified name is used in the internal impl
    // package

    @Override
    public Path determineSnapshotDirectory(Class<?> testClass, SnapshotDirectory directory) throws SnapshotException {
        throw new UnsupportedOperationException();
    }

}
