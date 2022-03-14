package de.skuzzle.test.snapshots.impl;

import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.SnapshotTestResult;

@API(status = Status.INTERNAL, since = "1.1.0")
public interface InternalSnapshotTest {

    /**
     * Returns the results of all snapshot assertions within the currently executed test
     * method.
     *
     * @return The test results.
     */
    List<SnapshotTestResult> testResults();

    /**
     * When soft assertions are enabled, performs the assertions based on the results
     * returned by {@link #testResults()}.
     *
     * @throws Exception
     */
    void executeAssertions() throws Exception;
}
