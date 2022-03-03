package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Default {@link PathFilter} implementation used by {@link FilesFrom} which accepts all
 * files.
 *
 * @author Simon Taddiken
 * @since 1.2.0
 */
final class PathFilterAll implements PathFilter {

    @Override
    public boolean include(Path path) throws IOException {
        return true;
    }

}
