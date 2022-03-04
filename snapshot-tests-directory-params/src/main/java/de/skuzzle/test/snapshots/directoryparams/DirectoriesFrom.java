package de.skuzzle.test.snapshots.directoryparams;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.params.provider.ArgumentsSource;

/**
 * ArgumentsProvider that lists directories and injects them as {@link TestDirectory}
 * instance into a test method. Usage:
 *
 * <pre>
 * &#64;ParameterizedTest
 * &#64;DirectoriesFrom(directory = "test-input", filter = MyCustomPathFilter.class)
 * void test(TestDirectory testDirectory) throws IOException {
 *     final TestFile testFile = testDirectory.resolve("input.txt");
 *     final String fileContents = testFile.asText(StandardCharsets.UTF_8);
 *     // ...
 * }
 * </pre>
 *
 * @see TestDirectory
 * @see TestFile
 * @see FilesFrom
 * @author Simon Taddiken
 */
@Retention(RUNTIME)
@Target({ METHOD })
@Documented
@ArgumentsSource(DirectoriesFromArgumentsProvider.class)
@API(status = Status.EXPERIMENTAL, since = "1.2.0")
public @interface DirectoriesFrom {

    /**
     * The directory, relative to src/test/resources, from which to list the directories.
     */
    String directory();

    /**
     * Name a class that implements {@link PathFilter} for more control over which
     * directories are to be included. The class is expected to have an accessible,
     * 0-arguments constructor.
     * <p>
     * Per default, all sub directories of {@link #directory()} are included.
     *
     * @return The path filter to use.
     */
    Class<? extends PathFilter> filter() default PathFilterAll.class;
}
