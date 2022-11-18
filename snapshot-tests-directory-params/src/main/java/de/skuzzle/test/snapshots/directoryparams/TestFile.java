package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Pointer to a file that has been listed in a directory by {@link FilesFrom}. Provides a
 * few methods for conveniently working with that file.
 *
 * @see FilesFrom
 * @author Simon Taddiken
 */
@API(status = Status.EXPERIMENTAL)
public final class TestFile {

    private final Path file;

    TestFile(Path file) {
        Arguments.requireNonNull(file, "file must not be null");
        Arguments.check(Files.exists(file), "file doesn't exist: %s", file);
        Arguments.check(Files.isRegularFile(file), "file is not a regular file: %s", file);
        this.file = file;
    }

    /**
     * Returns a pointer to a sibling file with given name.
     *
     * @param fileName The file name (including extension) of a sibling file to resolve.
     * @return Pointer to the sibling file.
     * @since 1.2.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.2.0")
    public TestFile sibling(String fileName) {
        Arguments.requireNonNull(fileName, "sibling fileName must not be null");
        return new TestFile(directory().resolve(fileName));
    }

    /**
     * Resolves a sibling file with same name as this one but with the given extension.
     *
     * @param extension The extension of a sibling file with identical name to resolve.
     * @return Pointer to the sibling file.
     * @since 1.2.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.2.0")
    public TestFile siblingWithExtension(String extension) {
        Arguments.requireNonNull(extension, "sibling extension must not be null");
        return testDirectory().resolve(name() + "." + extension);
    }

    /**
     * This file as Path.
     *
     * @return The file.
     */
    public Path file() {
        return this.file;
    }

    /**
     * The directory that contains this file.
     *
     * @return This file's directory.
     * @deprecated Since 1.2.0 - use {@link #testDirectory()} instead.
     */
    @Deprecated(forRemoval = true, since = "1.2.0")
    @API(status = Status.DEPRECATED, since = "1.2.0")
    public Path directory() {
        return this.file.getParent();
    }

    /**
     * The directory that contains this file.
     *
     * @return This file's directory.
     * @since 1.2.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.2.0")
    public TestDirectory testDirectory() {
        return new TestDirectory(this.file.getParent());
    }

    /**
     * The name of this file without extension.
     *
     * @return Name without extension.
     */
    public String name() {
        final String fileName = file.getFileName().toString();
        final int dot = fileName.lastIndexOf('.');
        return dot < 0
                ? fileName
                : fileName.substring(0, dot);
    }

    /**
     * The full name of this file including extension.
     *
     * @return The full name.
     */
    public String nameWithExtension() {
        return file.getFileName().toString();
    }

    /**
     * Returns this file's extension without leading dot. If this file has no extension
     * (that is, {@link #nameWithExtension()} does not contain a '.') an empty String is
     * returned.
     *
     * @return This file's extension without leading dot.
     */
    public String extension() {
        return FileExtension.withoutLeadingDot(file);
    }

    /**
     * Opens a new stream to read the file's content.
     *
     * @return The stream.
     * @throws IOException If an IO error occurs.
     */
    public InputStream openStream() throws IOException {
        return Files.newInputStream(file);
    }

    /**
     * Fully reads this file into a newly allocated byte array.
     *
     * @return The contents of the file as byte array.
     * @throws IOException If an IO error occurs.
     */
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
     */
    public String asText(Charset charset) throws IOException {
        return new String(asBinary(), Arguments.requireNonNull(charset, "charset must not be null"));
    }

    /**
     * Reads the file's contents into a String using the UTF-8 charset.
     *
     * @return The file contents as String
     * @throws IOException If an I/O error occurs.
     * @see #asText(Charset)
     * @since 1.3.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.3.0")
    public String asText() throws IOException {
        return asText(StandardCharsets.UTF_8);
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

    @Override
    public String toString() {
        try {
            return this.file.toRealPath().toString();
        } catch (final IOException e) {
            return this.file.toAbsolutePath().toString();
        }
    }
}
