package de.skuzzle.test.snapshots.directories;

import java.io.IOException;
import java.nio.file.Path;

public class DirectoryResolver {
    // FIXME: https://github.com/skuzzle/snapshot-tests/issues/3
    // This will not necessarily resolve to the correct directory. Especially multi
    // module setups and cases where the build is not invoked directly from its folder,
    // this might give some problems
    private final static Path BASE = Path.of("src", "test", "resources");

    public static Path resolve(String directory) throws IOException {
        return BASE.resolve(directory);
    }

    private DirectoryResolver() {
        // hidden
    }
}
