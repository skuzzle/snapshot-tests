package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotDirectoryStrategy;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.io.DirectoryResolver;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

class DetermineSnapshotDirectoryTest {

    @Test
    void empty_snapshot_directory_annotation_should_raise_exception() throws Exception {
        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> DetermineSnapshotDirectory.forTestclass(EmptyAnnotation.class));
    }

    @EnableSnapshotTests
    @SnapshotDirectory
    private static class EmptyAnnotation {

    }

    @Test
    void testResolveDefaultDirectory() throws Exception {
        final Path snapshotDirectory = DetermineSnapshotDirectory.forTestclass(DefaultSnapshotDirectory.class);
        assertThat(snapshotDirectory).isEqualTo(
                DirectoryResolver.resolve(DefaultSnapshotDirectory.class.getName().replace('.', '/') + "_snapshots"));
    }

    @EnableSnapshotTests
    private static class DefaultSnapshotDirectory {

    }

    @Test
    void testResolveDirectoryFromAnnotationString() throws Exception {
        final Path snapshotDirectory = DetermineSnapshotDirectory.forTestclass(FromAnnotationString.class);
        assertThat(snapshotDirectory).isEqualTo(DirectoryResolver.resolve("test"));
    }

    @EnableSnapshotTests
    @SnapshotDirectory("test")
    private static class FromAnnotationString {

    }

    @Test
    void testResolveDirectoryFromStrategy() throws Exception {
        final Path snapshotDirectory = DetermineSnapshotDirectory.forTestclass(FromStrategy.class);
        assertThat(snapshotDirectory).isEqualTo(Path.of("directory"));
    }

    @EnableSnapshotTests
    @SnapshotDirectory(value = "directory", determinedBy = SimpleTestSnapshotDirectoryStrategy.class)
    private static class FromStrategy {

    }

    public static class SimpleTestSnapshotDirectoryStrategy implements SnapshotDirectoryStrategy {

        @Override
        public Path determineSnapshotDirectory(Class<?> testClass, SnapshotDirectory directory)
                throws SnapshotException {
            return Path.of(directory.value());
        }

    }

}
