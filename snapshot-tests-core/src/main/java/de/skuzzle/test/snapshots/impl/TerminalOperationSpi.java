package de.skuzzle.test.snapshots.impl;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.StructuralAssertions;

public interface TerminalOperationSpi {

    SnapshotTestResult disabled(SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions,
            Object actual);
}
