package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.extension.ExtensionContext;

import de.skuzzle.test.snapshots.SnapshotAssertions;

final class DirectoryResolver {

    // FIXME: https://github.com/skuzzle/snapshot-tests/issues/3
    // This will not necessarily resolve to the correct directory. Especially multi
    // module setups and cases where the build is not invoked directly from its folder,
    // this might give some problems
    private final static Path BASE = Path.of("src/test/resources");

    public static Path resolveSnapshotDirectory(ExtensionContext extensionContext) throws IOException {
        final SnapshotAssertions snapshotAssertions = extensionContext.getRequiredTestInstance().getClass()
                .getAnnotation(SnapshotAssertions.class);
        final Class<?> testClass = extensionContext.getRequiredTestClass();

        final String testDirName = snapshotAssertions.snapshotDirectory().isEmpty()
                ? testClass.getName().replace('.', '/') + "_snapshots"
                : snapshotAssertions.snapshotDirectory();

        final Path testDirectory = BASE.resolve(testDirName);
        Files.createDirectories(testDirectory);
        return testDirectory;
    }

    private DirectoryResolver() {
        // hidden
    }
}
