package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class UncheckedIO {

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

    private UncheckedIO() {
        // hidden
    }
}
