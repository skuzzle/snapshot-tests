package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pointer to a file that has been listed in a directory by {@link FilesFrom}. Provides a
 * few methods for conveniently working with that file.
 *
 * @author Simon Taddiken
 */
public final class TestFile {

    private final Path file;

    TestFile(Path file) {
        this.file = Objects.requireNonNull(file, "file must not be null");
    }

    public Path file() {
        return this.file;
    }

    public Path directory() {
        return this.file.getParent();
    }

    public String name() {
        final String fileName = file.getFileName().toString();
        final int dot = fileName.lastIndexOf('.');
        return dot < 0
                ? ""
                : fileName.substring(0, dot);
    }

    public String nameWithExtension() {
        return file.getFileName().toString();
    }

    public String extension() {
        return FileExtension.withoutLeadingDot(file);
    }

    public InputStream openStream() throws IOException {
        return Files.newInputStream(file);
    }

    public byte[] asBinary() throws IOException {
        try (var inputStream = openStream()) {
            return inputStream.readAllBytes();
        }
    }

    /**
     * Reads the file's contents into a String using the given charset.
     *
     * @param charset The charset to use.
     * @return The file contents as String
     * @throws IOException If an I/O error occurs.
     * @see {@link #asText(Charset, Map)}
     */
    public String asText(Charset charset) throws IOException {
        return new String(asBinary(), Objects.requireNonNull(charset, "charset must not be null"));
    }

    private static final Pattern REPLACIBLE_VAR = Pattern.compile("\\$\\{(.*)\\}");

    // dont expose as api yet
    String asText(Charset charset, Map<String, ? extends Object> context) throws IOException {
        final String raw = new String(asBinary(), charset);
        final Set<String> replacedVariables = new HashSet<>(context.size());
        final Matcher matcher = REPLACIBLE_VAR.matcher(raw);
        final StringBuilder b = new StringBuilder(raw.length());
        while (matcher.find()) {
            final String varName = matcher.group(1);
            final Object replacement = context.get(varName);
            if (replacement == null) {
                throw new IllegalArgumentException(String.format(
                        "Could not resolve variable '%s' in TestFile '%s'. Given context object contains no replacement: %s",
                        varName, nameWithExtension(), context));
            }
            replacedVariables.add(varName);
            matcher.appendReplacement(b, replacement.toString());
        }

        matcher.appendTail(b);
        return b.toString();
    }
}
