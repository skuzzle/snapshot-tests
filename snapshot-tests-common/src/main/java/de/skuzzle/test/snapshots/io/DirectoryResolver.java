package de.skuzzle.test.snapshots.io;

import java.nio.file.Path;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Resolves directories relative to src/test/resources.
 *
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL)
public class DirectoryResolver {
    // FIXME: https://github.com/skuzzle/snapshot-tests/issues/3
    // This will not necessarily resolve to the correct directory. Especially multi
    // module setups and cases where the build is not invoked directly from its folder,
    // this might give some problems
    @API(status = Status.INTERNAL, since = "1.1.0")
    public final static Path BASE = Path.of("src", "test", "resources");

    public static Path resolve(String directory) {
        return BASE.resolve(Arguments.requireNonNull(directory, "directory must not be null"));
    }

    @API(status = Status.INTERNAL, since = "1.8.0")
    public static Path relativize(Path path) {
        try {
            return resolve(BASE.toAbsolutePath().relativize(path.toAbsolutePath()).toString());
        } catch (final Exception e) {
            // i dont trust this thing and its only used for pretty printing orphan
            // warnings. So as fallback we can just use the original path here
            return path;
        }
    }

    private DirectoryResolver() {
        // hidden
    }
}
