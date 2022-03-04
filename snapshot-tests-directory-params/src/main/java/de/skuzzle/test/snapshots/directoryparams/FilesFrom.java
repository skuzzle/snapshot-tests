package de.skuzzle.test.snapshots.directoryparams;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * ArgumentsProvider that lists files from a directory and injects them as
 * {@link TestFile} instance into a test method. Usage:
 *
 * <pre>
 * &#64;ParameterizedTest
 * &#64;FilesFrom(directory = "test-input", extensions = "txt", filter = MyCustomPathFilter.class)
 * void test(TestFile testFile) throws IOException {
 *     final String fileContents = testFile.asText(StandardCharsets.UTF_8);
 *     // ...
 * }
 * </pre>
 * <p>
 * Filtering for extensions is optional. If no extensions are specified, then all files
 * will be listed. Likewise, specifying a filter is optional but can be used to gain more
 * fine grained control over which files are being treated as test parameter.
 *
 * @author Simon Taddiken
 * @see TestFile
 * @see DirectoriesFrom
 */
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, CONSTRUCTOR })
@Documented
@ArgumentsSource(FilesFromArgumentsProvider.class)
@API(status = Status.EXPERIMENTAL)
public @interface FilesFrom {

    /**
     * The directory, relative to src/test/resources, from which to list the files.
     */
    String directory();

    /**
     * Whether to also include files from all sub directories of {@link #directory()}.
     */
    boolean recursive() default false;

    /**
     * File extensions to include when listing the directory. When left empty (which is
     * the default), all files are included. Extensions can be specified with or without
     * leading '.' and filtering for extensions is <em>not</em> case sensitive.
     * <p>
     * If you need more fine grained control over which files are to be listed, you can
     * implement {@link PathFilter} and use the {@link #filter()} option.
     *
     * @return The extensions to include.
     * @see #filter()
     */
    String[] extensions() default {};

    /**
     * Name a class that implements {@link PathFilter} for more control over which files
     * are to be included. The class is expected to have an accessible, 0-arguments
     * constructor.
     * <p>
     * Note that this filter will be used in addition to the extension filter if
     * {@link #extensions()} is configured with a non empty array.
     *
     * @return The path filter to use.
     * @since 1.2.0
     * @see #extensions()
     */
    @API(status = Status.EXPERIMENTAL, since = "1.2.0")
    Class<? extends PathFilter> filter() default PathFilterAll.class;

}
