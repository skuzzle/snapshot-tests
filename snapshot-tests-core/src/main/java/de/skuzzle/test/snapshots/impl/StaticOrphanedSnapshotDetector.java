package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import de.skuzzle.test.snapshots.io.UncheckedIO;

/**
 * Orphan detector that uses header information from ALL snapshot files to
 * <em>statically</em> (=without executing the tests) determine whether
 * <ol>
 * <li>the test class still exists and</li>
 * <li>the test method in that class still exists and</li>
 * <li>the test method is a snapshot test (has a snapshot parameter) and</li>
 * <li>the snapshot file is located in the correct directory</li>
 * </ol>
 *
 * If one of these conditions do not hold true for a snapshot file it is deemed an orphan.
 *
 * @author Simon Taddiken
 * @see DynamicOrphanedSnapshotsDetector
 */
final class StaticOrphanedSnapshotDetector {

    /**
     * Statically (=without executing the tests) detects all orphaned files within the
     * given root and its child directories.
     *
     * @param root The root directory to traverse.
     * @return Stream of orphaned snapshot files.
     */
    public Stream<Path> detectOrphans(Path root) {
        try (var files = UncheckedIO.walk(root)) {
            return files
                    .filter(InternalSnapshotNaming::isSnapshotFile)
                    .map(SnapshotFileAndPath::readFrom)
                    .filter(SnapshotFileAndPath::isOrphaned)
                    .map(SnapshotFileAndPath::path)
                    .collect(Collectors.toList())
                    .stream();
        }
    }

    public static final class SnapshotFileAndPath {
        private final Path path;
        private final SnapshotFile snapshotFile;

        public SnapshotFileAndPath(Path path, SnapshotFile snapshotFile) {
            this.path = path;
            this.snapshotFile = snapshotFile;
        }

        static SnapshotFileAndPath readFrom(Path path) {
            try {
                final SnapshotFile snapshotFile = SnapshotFile.fromSnapshotFile(path);
                return new SnapshotFileAndPath(path, snapshotFile);
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public Path path() {
            return this.path;
        }

        private boolean isOrphaned() {
            // test whether test class still exists
            final Optional<Class<?>> testClass = testClass();
            if (testClass.isEmpty()) {
                return true;
            }
            // test whether test method still exists in test class
            final Optional<Method> testMethod = testMethodIn(testClass.orElseThrow());
            if (testMethod.isEmpty()) {
                return true;
            }
            // test whether snapshot is located in correct folder
            final SnapshotConfiguration configuration = DefaultSnapshotConfiguration
                    .forTestClass(testClass.orElseThrow());
            final Path snapshotDirectory = configuration.determineSnapshotDirectory();
            final Path snapshotFileName = path.getFileName();
            return !Files.exists(snapshotDirectory.resolve(snapshotFileName));
        }

        private Optional<Class<?>> testClass() {
            try {
                final var className = snapshotFile.header().get(SnapshotHeader.TEST_CLASS);
                final Class<?> testClass = Class.forName(className, true, getClass().getClassLoader());
                return Optional.of(testClass);
            } catch (final ClassNotFoundException e) {
                return Optional.empty();
            }
        }

        private Optional<Method> testMethodIn(Class<?> testClass) {
            final var methodName = snapshotFile.header().get(SnapshotHeader.TEST_METHOD);
            return Arrays.stream(testClass.getDeclaredMethods())
                    .filter(method -> method.getName().equals(methodName))
                    .filter(this::isSnapshotTest)
                    .findAny();
        }

        private boolean isSnapshotTest(Method method) {
            return !Modifier.isStatic(method.getModifiers())
                    && !Modifier.isPrivate(method.getModifiers())
                    && Arrays.stream(method.getParameterTypes())
                            .anyMatch(parameterType -> Snapshot.class.isAssignableFrom(parameterType));
        }

    }
}