package de.skuzzle.test.snapshots.io;

import java.nio.file.Files;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.validation.Arguments;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Utility for working with filenames.
 *
 * @author Simon Taddiken
 * @since 1.9.0
 */
@API(status = Status.INTERNAL, since = "1.9.0")
public final class FileName {

    /**
     * Returns the file name without any extension. That is, everything starting from (and
     * including) the last '.' in the full file name will be stripped.
     *
     * @param path The path to obtain the filename from.
     * @return The file name without extension.
     */
    public static String fromPath(Path path) {
        Arguments.check(path != null, "path must not be null");
        Arguments.check(!Files.isDirectory(path), "Not a file: %s", path);
        final String filenameWithExtension = path.getFileName().toString();
        final int dotIdx = filenameWithExtension.lastIndexOf('.');
        if (dotIdx >= 0) {
            return filenameWithExtension.substring(0, dotIdx);
        }
        return filenameWithExtension;
    }
}
