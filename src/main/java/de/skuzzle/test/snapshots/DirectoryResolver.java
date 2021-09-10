package de.skuzzle.test.snapshots;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.extension.ExtensionContext;

class DirectoryResolver {

    private final static Path BASE = Path.of("src/test/resources");

    public static Path resolveSnapshotDirectory(ExtensionContext extensionContext) throws IOException {
        final SnapshotAssertions snapshotAssertions = extensionContext.getRequiredTestInstance().getClass()
                .getAnnotation(SnapshotAssertions.class);
        final Class<?> testClass = extensionContext.getRequiredTestClass();

        final String testDirName = snapshotAssertions.snapshotDirectory().isEmpty()
                ? testClass.getPackageName().replace('.', '/') + "_snapshots"
                : snapshotAssertions.snapshotDirectory();

        final Path testDirectory = BASE.resolve(testDirName);
        Files.createDirectories(testDirectory);
        return testDirectory;
    }
}
