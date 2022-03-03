package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

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
 */
@API(status = Status.EXPERIMENTAL, since = "1.2.0")
public interface PathFilter {

    public static PathFilter fromPredicate(Predicate<Path> predicate) {
        return path -> predicate.test(path);
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
