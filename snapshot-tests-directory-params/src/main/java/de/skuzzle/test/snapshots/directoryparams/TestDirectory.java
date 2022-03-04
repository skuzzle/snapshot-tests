package de.skuzzle.test.snapshots.directoryparams;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.io.UncheckedIO;
import de.skuzzle.test.snapshots.validation.Arguments;

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

    public Path path() {
        return directory;
    }

    public String name() {
        return directory.getFileName().toString();
    }

    public TestFile resolve(String fileName) {
        Arguments.requireNonNull(fileName, "fileName must not be null");
        return new TestFile(directory.resolve(fileName));
    }

    public Stream<TestFile> files() {
        return UncheckedIO.list(directory)
                .filter(Files::isRegularFile)
                .map(TestFile::new);
    }

    @Override
    public String toString() {
        return name();
    }
}
