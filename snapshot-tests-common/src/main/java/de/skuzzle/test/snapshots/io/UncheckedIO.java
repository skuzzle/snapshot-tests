package de.skuzzle.test.snapshots.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Internal IO utility which throws {@link UncheckedIOException} instead of
 * {@link IOException} and is thus eligible to be used in standard functional interfaces
 * coming with JDK.
 *
 * @author Simon Taddiken
 * @since 1.1.0
 */
@API(status = Status.INTERNAL, since = "1.1.0")
public final class UncheckedIO {

    public static boolean isSameFile(Path f1, Path f2) {
        try {
            return Files.isSameFile(f1, f2);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void delete(Path path) {
        try {
            Files.delete(path);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void deleteIfExists(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Stream<Path> list(Path directory) {
        try {
            return Files.list(directory);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Stream<Path> walk(Path directory) {
        try {
            return Files.walk(directory);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void createDirectories(Path testDirectory) {
        try {
            Files.createDirectories(testDirectory);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private UncheckedIO() {
        // hidden
    }
}
