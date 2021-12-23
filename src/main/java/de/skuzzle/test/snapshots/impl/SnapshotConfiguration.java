package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.util.Objects;

import org.junit.jupiter.api.extension.ExtensionContext;

import de.skuzzle.test.snapshots.SnapshotAssertions;

/**
 * Relevant configuration for executing snapshot tests in a test class that is annotated
 * with {@link SnapshotAssertions}.
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

    public String snapshotDirecotry() throws IOException {
        final SnapshotAssertions snapshotAssertions = extensionContext.getRequiredTestClass()
                .getAnnotation(SnapshotAssertions.class);

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
                .getAnnotation(SnapshotAssertions.class)
                .forceUpdateSnapshots();
    }

    public boolean isSoftAssertions() {
        return extensionContext.getRequiredTestClass()
                .getAnnotation(SnapshotAssertions.class)
                .softAssertions();
    }
}
