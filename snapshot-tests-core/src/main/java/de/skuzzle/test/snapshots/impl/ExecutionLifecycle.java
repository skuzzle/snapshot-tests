package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.StructuralAssertions;

/**
 * Separates the execution of a snapshot assertion into multiple stages.
 *
 * @author Simon Taddiken
 * @since 1.8.0
 */
interface ExecutionLifecycle {

    /**
     * Instruments the lifecycle methods to perform a full assertion.
     *
     * @param structuralAssertions The {@link StructuralAssertions} instance to use for
     *            asserting equivalence.
     * @param assertionInput The assertion input.
     * @return The test result.
     * @throws Exception If any technical problem occurs.
     */
    default SnapshotTestResult executeLifecycleWith(StructuralAssertions structuralAssertions,
            SnapshotAssertionInput assertionInput) throws Exception {
        beforeExecution(assertionInput);

        final SnapshotTestResult testResult = executeAssertion(assertionInput, structuralAssertions);

        afterExecution(assertionInput, testResult);
        return testResult;
    }

    void beforeExecution(SnapshotAssertionInput assertionInput) throws Exception;

    SnapshotTestResult executeAssertion(SnapshotAssertionInput assertionInput,
            StructuralAssertions structuralAssertions)
            throws Exception;

    void afterExecution(SnapshotAssertionInput assertionInput, SnapshotTestResult result) throws Exception;
}
