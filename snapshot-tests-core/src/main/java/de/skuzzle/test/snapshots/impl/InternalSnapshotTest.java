package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.SnapshotTestResult;

@API(status = Status.INTERNAL, since = "1.1.0")
public interface InternalSnapshotTest {

    public static InternalSnapshotTest of(SnapshotConfiguration configuration, Method testMethod) {
        return new SnapshotTestImpl(configuration, testMethod);
    }

    List<SnapshotTestResult> testResults();

    void executeAssertions() throws Exception;
}
