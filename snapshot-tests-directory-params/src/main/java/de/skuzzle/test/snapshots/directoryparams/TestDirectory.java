package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.io.UncheckedIO;
import de.skuzzle.test.snapshots.validation.Arguments;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * A pointer to a directory with some convenient helper methods. Can be injected in
 * parameterized tests that are annotated with {@link DirectoriesFrom}.
 *
 * @see DirectoriesFrom
 * @author Simon Taddiken
 * @since 1.2.0
 */
@API(status = Status.EXPERIMENTAL, since = "1.2.0")
public final class TestDirectory {

    private final Path directory;

    TestDirectory(Path directory) {
        Arguments.requireNonNull(directory, "directory must not be null");
        Arguments.check(Files.exists(directory), "directory doesn't exist: %s", directory);
        Arguments.check(Files.isDirectory(directory), "directory is not a regular directory: %s", directory);
        this.directory = directory;
    }

    /**
     * Returns the underlying Path object.
     *
     * @return The path
     */
    public Path path() {
        return directory;
    }

    /**
     * Returns the name of this directory.
     *
     * @return The name.
     */
    public String name() {
        return directory.getFileName().toString();
    }

    /**
     * Determines whether this is a leave directory or not.
     *
     * @return Whether this directory has any sub directories.
     * @since 1.9.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.9.0")
    public boolean hasSubDirectories() {
        return UncheckedIO.list(directory).anyMatch(Files::isDirectory);
    }

    /**
     * Whether this directory contains an element with the given file name. Checks for
     * both directories and files.
     *
     * @param fileName Name of a file to check for.
     * @return Whether this directory contains a file or directory with given name.
     */
    @API(status = Status.EXPERIMENTAL, since = "1.9.0")
    public boolean containsFile(String fileName) {
        Arguments.requireNonNull(fileName, "fileName must not be null");
        return Files.exists(directory.resolve(fileName));
    }

    /**
     * Resolves a file within this directory. If the given fileName points to a directory
     * and not a file, this method will throw an exception.
     *
     * @param fileName
     * @return The resolved file.
     */
    public TestFile resolve(String fileName) {
        Arguments.requireNonNull(fileName, "fileName must not be null");
        return new TestFile(directory.resolve(fileName));
    }

    /**
     * Lists all the files that are directly contained in this directory.
     *
     * @return The files
     */
    public Stream<TestFile> files() {
        return UncheckedIO.list(directory)
                .filter(Files::isRegularFile)
                .map(TestFile::new);
    }

    @Override
    public String toString() {
        try {
            return this.directory.toAbsolutePath().toRealPath().toString();
        } catch (final IOException e) {
            return this.directory.toAbsolutePath().toString();
        }
    }
}
