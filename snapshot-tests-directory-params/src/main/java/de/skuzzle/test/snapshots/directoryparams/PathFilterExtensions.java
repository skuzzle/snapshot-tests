package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * PathFilter implementation that handles the {@link FilesFrom#extensions()} filter.
 *
 * @author Simon Taddiken
 * @since 1.2.0
 */
final class PathFilterExtensions implements PathFilter {

    private final String[] extensions;

    static PathFilter extensions(String[] extensionsArrays) {
        return new PathFilterExtensions(extensionsArrays);
    }

    private PathFilterExtensions(String[] extensions) {
        this.extensions = Arguments.requireNonNull(extensions, "extensions array must not be null");
    }

    @Override
    public boolean include(Path path) throws IOException {
        return extensions.length == 0 ||
                Arrays.stream(extensions)
                        .anyMatch(configuredExtension -> matchesExtension(configuredExtension, path));
    }

    private boolean matchesExtension(String configuredExtension, Path file) {
        final String normalizedExtension = configuredExtension.startsWith(".")
                ? configuredExtension
                : "." + configuredExtension;
        final String filesExtension = FileExtension.includingLeadingDot(file);
        return filesExtension.equalsIgnoreCase(normalizedExtension);
    }

}
