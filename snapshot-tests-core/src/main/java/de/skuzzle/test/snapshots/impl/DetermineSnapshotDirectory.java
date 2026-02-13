package de.skuzzle.test.snapshots.impl;

import java.nio.file.Path;

import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotDirectoryStrategy;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.io.DirectoryResolver;
import de.skuzzle.test.snapshots.validation.State;

final class DetermineSnapshotDirectory {

    static Path forTestclass(TestClass testClass) {
        final SnapshotDirectory annotation = testClass.getAnnotation(SnapshotDirectory.class);
        if (annotation == null) {
            // default behavior if no annotation is present
            final String dirName = testClass.getName().replace('.', '/') + "_snapshots";
            return DirectoryResolver.resolve(dirName);
        }

        State.check(!isDefaultValue(annotation.determinedBy()) || !annotation.value().isEmpty(),
                "Either specify the value() attribute or the determinedBy() attribute within the @SnapshotDirectory annotation on %s",
                testClass.getName());

        if (isDefaultValue(annotation.determinedBy())) {
            return DirectoryResolver.resolve(annotation.value());
        } else {
            return pathFromStrategy(testClass, annotation);
        }
    }

    private static boolean isDefaultValue(Class<?> type) {
        return "de.skuzzle.test.snapshots.DefaultSnapshotDirectoryStrategy".equals(type.getName());
    }

    private static Path pathFromStrategy(TestClass testClass, SnapshotDirectory directory) {
        try {
            final Path snapshotDirectory = newInstanceOf(directory.determinedBy())
                    .determineSnapshotDirectory(testClass.testClass(), directory);
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

    private DetermineSnapshotDirectory() {
        // hidden
    }
}
