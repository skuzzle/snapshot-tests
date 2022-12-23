package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.StructuralAssertions;

interface ExecutionLifecycle {

    void beforeExecution(SnapshotAssertionInput assertionInput) throws Exception;

    SnapshotTestResult execute(SnapshotAssertionInput assertionInput, StructuralAssertions structuralAssertions)
            throws Exception;

    void afterExecution(SnapshotAssertionInput assertionInput, SnapshotTestResult result) throws Exception;
}
