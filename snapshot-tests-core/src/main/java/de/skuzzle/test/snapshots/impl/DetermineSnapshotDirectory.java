package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotDirectoryStrategy;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.io.DirectoryResolver;
import de.skuzzle.test.snapshots.validation.State;

final class DetermineSnapshotDirectory {

    static Path forTestclass(Class<?> testClass) {
        final Path testDirectoryLegacy = snapshotDirectoryLegacy(testClass);
        final SnapshotDirectory annotation = testClass.getAnnotation(SnapshotDirectory.class);
        if (testDirectoryLegacy != null) {
            State.check(annotation == null,
                    "Please use either legacy mode of specifying snapshot directory or the new @SnapshotDirectory annotation but not both");
            return testDirectoryLegacy;
        }

        if (annotation == null) {
            // default behavior if no annotation is present
            final String dirName = testClass.getName().replace('.', '/') + "_snapshots";
            return DirectoryResolver.resolve(dirName);
        }

        State.check(!isDefaultValue(annotation.determinedBy()) || !annotation.value().isEmpty(), "TBD");

        if (isDefaultValue(annotation.determinedBy())) {
            return DirectoryResolver.resolve(annotation.value());
        } else {
            return pathFromStrategy(testClass, annotation);
        }
    }

    private static boolean isDefaultValue(Class<?> type) {
        return "de.skuzzle.test.snapshots.DefaultSnapshotDirectoryStrategy".equals(type.getName());
    }

    private static Path pathFromStrategy(Class<?> testClass, SnapshotDirectory directory) {
        try {
            final Path snapshotDirectory = newInstanceOf(directory.determinedBy())
                    .determineSnapshotDirectory(testClass, directory);
            State.check(snapshotDirectory != null, "Custom SnapshotDirectoryStrategy %s returned null for %s",
                    directory.determinedBy().getName(), directory);
            return snapshotDirectory;
        } catch (final SnapshotException e) {
            throw new IllegalStateException(
                    "Error determining snapshot directory from strategy " + directory.determinedBy(), e);
        }
    }

    private static SnapshotDirectoryStrategy newInstanceOf(Class<? extends SnapshotDirectoryStrategy> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (final Exception e) {
            throw new IllegalStateException("Error creating an instance of " + type.getName(), e);
        }
    }

    private static Path snapshotDirectoryLegacy(Class<?> testClass) {
        final EnableSnapshotTests snapshotAssertions = testClass
                .getAnnotation(EnableSnapshotTests.class);
        if (snapshotAssertions.snapshotDirectory().isEmpty()) {
            return null;
        }
        final String testDirName = snapshotAssertions.snapshotDirectory();
        return DirectoryResolver.resolve(testDirName);
    }

    private DetermineSnapshotDirectory() {
        // hidden
    }
}
