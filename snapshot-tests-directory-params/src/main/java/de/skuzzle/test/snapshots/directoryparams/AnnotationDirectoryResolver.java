package de.skuzzle.test.snapshots.directoryparams;

import java.nio.file.Path;

import de.skuzzle.test.snapshots.io.DirectoryResolver;

final class AnnotationDirectoryResolver {

    private AnnotationDirectoryResolver() {
        // hidden
    }

    public static Path resolveDirectory(String projectDir, String testResourcesDir) {
        if (!testResourcesDir.isEmpty()) {
            de.skuzzle.test.snapshots.validation.Arguments.check(projectDir.isEmpty(),
                    "You have to specify only one root directory");

            return DirectoryResolver.resolve(testResourcesDir);
        } else if (!projectDir.isEmpty()) {
            return Path.of(projectDir);
        }
        throw new IllegalArgumentException(
                "You have to specify either projectDirectory=... or testResourcesDirectory=...");
    }
}
