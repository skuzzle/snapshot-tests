package de.skuzzle.test.snapshots.impl;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL, since = "1.1.0")
public interface InternalSnapshotTest {

    /**
     * When soft assertions are enabled, performs the assertions based on the results
     * returned by {@link #testResults()}.
     *
     * @throws Exception
     */
    void executeSoftAssertions() throws Exception;
}
