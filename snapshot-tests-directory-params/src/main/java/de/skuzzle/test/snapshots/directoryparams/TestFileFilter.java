package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Filter strategy that determines whether an encountered file is suitable to be injected
 * as a test case parameter.
 *
 * @author Simon Taddiken
 * @since 1.9.0
 * @see FilesFrom#filter()
 */
@API(status = Status.EXPERIMENTAL, since = "1.9.0")
public interface TestFileFilter {
    /**
     * Determines whether the given file is eligible for being injected as a parameter for
     * a parameterized test method annotated with {@link FilesFrom}.
     *
     * @param testFile The encountered test file to check.
     * @param recursive Whether recursive scanning mode has been enabled.
     * @return Whether the file shall be used as test parameter.
     * @throws IOException If testing the file fails.
     */
    boolean include(TestFile testFile, boolean recursive) throws IOException;

}
