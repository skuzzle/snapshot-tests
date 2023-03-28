package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.function.Predicate;

import de.skuzzle.test.snapshots.validation.Arguments;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * {@link Path} based predicate that can be implemented for more fine grained control over
 * which files are to be included by {@link FilesFrom}.
 * <p>
 * Implementations are required to have an accessible, 0-argument constructor in order to
 * be used with {@link FilesFrom#filter()}.
 *
 * @author Simon Taddiken
 * @since 1.2.0
 * @see FilesFrom#filter()
 * @see DirectoriesFrom#filter()
 * @deprecated Since 1.9.0 - Use either {@link TestFileFilter} or
 *             {@link TestDirectoryFilter} instead.
 */
@API(status = Status.DEPRECATED, since = "1.9.0")
@Deprecated(since = "1.9.0", forRemoval = true)
public interface PathFilter extends TestFileFilter, TestDirectoryFilter {

    public static PathFilter fromPredicate(Predicate<Path> predicate) {
        return predicate::test;
    }

    /**
     * Determines whether the given path is eligible for being injected as a parameter for
     * a parameterized test method annotated with {@link FilesFrom}.
     *
     * @param path Path to a file to check.
     * @return Whether the file shall be used as test parameter.
     * @throws IOException If testing the file fails.
     */
    boolean include(Path path) throws IOException;

    @Override
    default boolean include(TestDirectory testDirectory, boolean recursive) throws IOException {
        return include(testDirectory.path());
    }

    @Override
    default boolean include(TestFile testFile, boolean recursive) throws IOException {
        return include(testFile.file());
    }

    /**
     * Creates a default predicate, converting potential {@link IOException}s to
     * {@link UncheckedIOException}s.
     *
     * @return This filter as a predicate.
     */
    default Predicate<Path> toPredicate() {
        return path -> {
            try {
                return include(path);
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    /**
     * Creates a new filter by composing this and the given other one using 'and'
     * semantics.
     *
     * @param other The filter to chain.
     * @return A new chained filter.
     */
    default PathFilter and(PathFilter other) {
        Arguments.requireNonNull(other, "other PathFilter must not be null");
        return path -> include(path) && other.include(path);
    }
}
