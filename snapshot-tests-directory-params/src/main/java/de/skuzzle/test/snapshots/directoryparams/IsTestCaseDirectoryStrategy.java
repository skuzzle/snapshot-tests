package de.skuzzle.test.snapshots.directoryparams;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * When using {@link DirectoriesFrom} with {@link DirectoriesFrom#recursive()
 * recursive=true} an instance of this strategy is used to determine whether an
 * encountered directory will be passed as argument to the test case and thus lead to a
 * parameterized test execution.
 * <p>
 * This interface is closely related to {@link PathFilter}. This strategy is only used
 * when the recursive option is enabled and will be queried <em>after</em> the PathFilter
 * in place.
 * 
 * @author Simon Taddiken
 * @since 1.9.0
 * @see DirectoriesFrom
 * @see PathFilter
 */
@API(status = Status.EXPERIMENTAL, since = "1.9.0")
public interface IsTestCaseDirectoryStrategy {

    /**
     * Determines whether the given encountered directory represents a test case and will
     * thus lead to a parameterized test execution of the test method annotated with
     * {@link DirectoriesFrom}.
     * 
     * @param directory The directory to test.
     * @return true if the given direcotry represents a test case directory.
     */
    boolean determineIsTestCaseDirectory(TestDirectory directory);
}
