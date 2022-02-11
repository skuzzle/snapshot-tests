package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.junit.jupiter.api.extension.ExtensionContext;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.directories.DirectoryResolver;

/**
 * Relevant configuration for executing snapshot tests in a test class that is annotated
 * with {@link EnableSnapshotTests}.
 *
 * @author Simon Taddiken
 */
final class SnapshotConfiguration {

    private static final String FORCE_UPDATE_SYSTEM_PROPERTY = "forceUpdateSnapshots";

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

    public boolean isForceUpdateSnapshotsGlobal() {
        // Annotation on test class
        final boolean valueFromLegacyAnnotation = testClass()
                .getAnnotation(EnableSnapshotTests.class)
                .forceUpdateSnapshots();
        if (valueFromLegacyAnnotation
                || extensionContext.getRequiredTestClass().isAnnotationPresent(ForceUpdateSnapshots.class)) {
            return true;
        }

        // System property
        return System.getProperties().keySet().stream()
                .map(Object::toString)
                .anyMatch(FORCE_UPDATE_SYSTEM_PROPERTY::equalsIgnoreCase);
    }

    public boolean isForceUpdateSnapshotsLocal(Method testMethod) {
        // Annotation on test method
        if (testMethod.isAnnotationPresent(ForceUpdateSnapshots.class)) {
            return true;
        }

        return isForceUpdateSnapshotsGlobal();
    }

    public boolean isSoftAssertions() {
        return extensionContext.getRequiredTestClass()
                .getAnnotation(EnableSnapshotTests.class)
                .softAssertions();
    }
}
