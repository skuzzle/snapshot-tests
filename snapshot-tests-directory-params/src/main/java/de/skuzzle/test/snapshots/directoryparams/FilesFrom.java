package de.skuzzle.test.snapshots.directoryparams;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * ArgumentsProvider that lists files from a directory and injects them as
 * {@link TestFile} instance into a test method. Usage:
 *
 * <pre>
 * &#64;ParameterizedTest
 * &#64;FilesFrom(directory = "test-input", extensions = "txt")
 * void test(TestFile testFile) throws IOException {
 *     final String fileContents = testFile.asText(StandardCharsets.UTF_8);
 *     // ...
 * }
 * </pre>
 *
 * @author Simon Taddiken
 */
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
@Documented
@ArgumentsSource(DirectoryContentsArgumentsProvider.class)
public @interface FilesFrom {

    /**
     * The directory, relative to src/main/resources, from which to list the files.
     */
    String directory();

    /**
     * Whether to also include files from all sub directories of {@link #directory()}.
     */
    boolean recursive() default false;

    /**
     * File extensions to include when listing the directory. When left empty, all files
     * are included. Extensions can be specified with or without leading '.' and filtering
     * for extensions is <em>not</em> case sensitive.
     *
     * @return The extensions to include.
     */
    String[] extensions() default {};

}
