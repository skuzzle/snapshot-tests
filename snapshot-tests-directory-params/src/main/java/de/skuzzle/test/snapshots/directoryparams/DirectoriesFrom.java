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
     * Name a class that implements {@link PathFilter} for more control over which
     * directories are to be included. The class is expected to have an accessible,
     * 0-arguments constructor.
     * <p>
     * Per default, all direct sub directories of {@link #directory()} are included.
     *
     * @return The path filter to use.
     */
    Class<? extends PathFilter> filter() default PathFilterAll.class;

    /**
     * Whether to recursively list all directories of the given root directory. Whether a
     * directory is actually suitable to be used as test case directory is determined by
     * an instance of {@link IsTestCaseDirectoryStrategy} that can be configured via
     * {@link #isTestcaseDeterminedBy()}.
     * <p>
     * By default, when recursive listing is enabled, all <em>leave-directories</em> are
     * considered test directories. That is, directories without any direct sub
     * directories.
     * 
     * @return Whether to recursively list directories.
     * @see #isTestcaseDeterminedBy()
     * @since 1.9.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.9.0")
    boolean recursive() default false;

    /**
     * Strategy that defines which directories actually represent a test case when
     * {@link #recursive()} is set to true. By default, all directories that do not
     * container any other directories (=<em>leave-directories</em>) are considered test
     * case directories.
     * <p>
     * When iterating the directories, this strategy is applied <em>after</em> the
     * {@link #filter() path filter}. Thus, the path filter takes precedence over this
     * strategy and only directories that were accepted by the path filter will be
     * subjected to the {@link IsTestCaseDirectoryStrategy}.
     * 
     * @return The strategy.
     * @see #recursive()
     * @since 1.9.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.9.0")
    Class<? extends IsTestCaseDirectoryStrategy> isTestcaseDeterminedBy() default DefaultIsTestCaseDirectoryStrategy.class;
}
