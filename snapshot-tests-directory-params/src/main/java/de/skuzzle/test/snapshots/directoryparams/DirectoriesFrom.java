package de.skuzzle.test.snapshots.directoryparams;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.params.provider.ArgumentsSource;

import de.skuzzle.test.snapshots.directoryparams.Filters.TestDirectoryFilterAll;

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
 * <p>
 * You can either use {@link #projectDirectory()} which will take a path that is relative
 * to the current project root (<code>Path.of(projectDirectory)</code>) or you can use
 * {@link #testResourcesDirectory()} which will take a path that is relative to the
 * current project's test resources directory.
 * <p>
 * Directories can be listed recursively when {@link #recursive()} ist set to true. In
 * that case, you should specify a {@link IsTestCaseDirectoryStrategy} that determines
 * which of the encountered directories will actually lead to a parameterized test
 * execution.
 * 
 * @see TestDirectory
 * @see TestFile
 * @see FilesFrom
 * @author Simon Taddiken
 * @since 1.2.0
 */
@Retention(RUNTIME)
@Target({ METHOD })
@Documented
@ArgumentsSource(DirectoriesFromArgumentsProvider.class)
@API(status = Status.EXPERIMENTAL, since = "1.2.0")
public @interface DirectoriesFrom {

    /**
     * The directory, relative to src/test/resources, from which to list the directories.
     *
     * @deprecated Since 1.6.0 - Use {@link #testResourcesDirectory()} instead for a 1:1
     *             replacement or {@link #projectDirectory()} to specify a directory
     *             relative to the current project.
     */
    @Deprecated(since = "1.6.0", forRemoval = true)
    String directory() default "";

    /**
     * The directory, relative to src/test/resources, from which to list the files. Mutual
     * exclusive to {@link #projectDirectory()} but having one of those properties set is
     * mandatory.
     *
     * @since 1.6.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.6.0")
    String testResourcesDirectory() default "";

    /**
     * The directory, from which to list the files. The actual Path will be resolved as
     * <code>Path.of(projectDirectory)</code>. Mutual exclusive to
     * {@link #projectDirectory()} but having one of those properties set is mandatory.
     *
     * @since 1.6.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.6.0")
    String projectDirectory() default "";

    /**
     * Name a class that implements {@link TestDirectoryFilter} for more control over
     * which directories are to be included. The class is expected to have an accessible,
     * 0-arguments constructor.
     * <p>
     * The default behaves differently, depending on whether {@link #recursive()} is
     * enabled or not. In non-recursive mode, all encountered directories will be
     * accepted. In recursive mode, only leave directories (=directories without child
     * directories) are accepted.
     *
     * @return The directory filter to use.
     * @see #recursive()
     */
    Class<? extends TestDirectoryFilter> filter() default TestDirectoryFilterAll.class;

    /**
     * Whether to recursively list all directories of the given root directory. By
     * default, when recursive listing is enabled, all <em>leave-directories</em> are
     * considered test directories. That is, directories without any direct sub
     * directories. This behavior can be adjusted by providing a custom
     * {@link TestDirectoryFilter} via {@link #filter()}.
     * <p>
     * Defaults to false.
     * 
     * @return Whether to recursively list directories.
     * @since 1.9.0
     * @see #filter()
     */
    @API(status = Status.EXPERIMENTAL, since = "1.9.0")
    boolean recursive() default false;
}
