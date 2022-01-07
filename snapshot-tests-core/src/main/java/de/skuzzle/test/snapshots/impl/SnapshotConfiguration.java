package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.junit.jupiter.api.extension.ExtensionContext;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.directories.DirectoryResolver;

/**
 * Relevant configuration for executing snapshot tests in a test class that is annotated
 * with {@link EnableSnapshotTests}.
 *
 * @author Simon Taddiken
 */
final class SnapshotConfiguration {

    private final ExtensionContext extensionContext;

    private SnapshotConfiguration(ExtensionContext extensionContext) {
        this.extensionContext = Objects.requireNonNull(extensionContext);
    }

    public static SnapshotConfiguration fromExtensionContext(ExtensionContext extensionContext) {
        return new SnapshotConfiguration(extensionContext);
    }

    public Path determineSnapshotDirectory() throws IOException {
        final String testDirName = snapshotDirecotryName();

        final Path testDirectory = DirectoryResolver.resolve(testDirName);
        Files.createDirectories(testDirectory);
        return testDirectory;
    }

    private String snapshotDirecotryName() throws IOException {
        final EnableSnapshotTests snapshotAssertions = extensionContext.getRequiredTestClass()
                .getAnnotation(EnableSnapshotTests.class);

        final String testDirName = snapshotAssertions.snapshotDirectory().isEmpty()
                ? extensionContext.getRequiredTestClass().getName().replace('.', '/') + "_snapshots"
                : snapshotAssertions.snapshotDirectory();
        return testDirName;
    }

    public Class<?> testClass() {
        return extensionContext.getRequiredTestClass();
    }

    public boolean isForceUpdateSnapshots() {
        return extensionContext.getRequiredTestClass()
                .getAnnotation(EnableSnapshotTests.class)
                .forceUpdateSnapshots();
    }

    public boolean isSoftAssertions() {
        return extensionContext.getRequiredTestClass()
                .getAnnotation(EnableSnapshotTests.class)
                .softAssertions();
    }
}
