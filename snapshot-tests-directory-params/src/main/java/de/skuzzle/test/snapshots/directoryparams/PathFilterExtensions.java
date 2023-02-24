package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.util.Arrays;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Special PathFilter implementation that handles the {@link FilesFrom#extensions()}
 * filter.
 *
 * @author Simon Taddiken
 * @since 1.2.0
 */
final class PathFilterExtensions implements TestFileFilter {

    private final String[] extensions;

    static TestFileFilter extensions(String[] extensionsArrays) {
        return new PathFilterExtensions(extensionsArrays);
    }

    private PathFilterExtensions(String[] extensions) {
        this.extensions = Arguments.requireNonNull(extensions, "extensions array must not be null");
    }

    @Override
    public boolean include(TestFile testFile, boolean recursive) throws IOException {
        return extensions.length == 0 ||
                Arrays.stream(extensions)
                        .anyMatch(configuredExtension -> matchesExtension(configuredExtension, testFile));
    }

    private boolean matchesExtension(String configuredExtension, TestFile testFile) {
        final String normalizedExtensionWithoutDot = configuredExtension.startsWith(".")
                ? configuredExtension.substring(1)
                : configuredExtension;

        final String filesExtensionWithoutDot = testFile.extension();
        return filesExtensionWithoutDot.equalsIgnoreCase(normalizedExtensionWithoutDot);
    }

}
