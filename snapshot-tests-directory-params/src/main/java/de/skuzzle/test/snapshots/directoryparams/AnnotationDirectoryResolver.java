package de.skuzzle.test.snapshots.directoryparams;

import java.nio.file.Path;

import de.skuzzle.test.snapshots.io.DirectoryResolver;

final class AnnotationDirectoryResolver {

    private AnnotationDirectoryResolver() {
        // hidden
    }

    public static Path resolveDirectory(String legacyDir, String projectDir, String testResourcesDir) {
        if (!legacyDir.isEmpty()) {
            de.skuzzle.test.snapshots.validation.Arguments.check(testResourcesDir.isEmpty() && projectDir.isEmpty(),
                    "You have to specify only one root directory");

            return DirectoryResolver.resolve(legacyDir);
        } else if (!testResourcesDir.isEmpty()) {
            de.skuzzle.test.snapshots.validation.Arguments.check(legacyDir.isEmpty() && projectDir.isEmpty(),
                    "You have to specify only one root directory");

            return DirectoryResolver.resolve(testResourcesDir);
        } else if (!projectDir.isEmpty()) {
            de.skuzzle.test.snapshots.validation.Arguments.check(legacyDir.isEmpty() && testResourcesDir.isEmpty(),
                    "You have to specify only one root directory");
            return Path.of(projectDir);
        }
        throw new IllegalArgumentException(
                "You have to specify either projectDirectory=... or testResourcesDirectory=...");
    }
}
