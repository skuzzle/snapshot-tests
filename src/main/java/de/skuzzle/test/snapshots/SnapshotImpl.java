package de.skuzzle.test.snapshots;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.extension.ExtensionContext;

import de.skuzzle.test.snapshots.SnapshotDsl.ChoseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

class SnapshotImpl implements Snapshot {

    private final ExtensionContext extensionContext;
    private int counter;
    private boolean snapshotsUpdated;

    public SnapshotImpl(ExtensionContext extensionContext) {
        this.extensionContext = extensionContext;
    }

    @Override
    public ChoseDataFormat assertThat(Object actual) {
        return new SnapshotDslImpl(this, actual);
    }

    boolean snapshotsUpdated() {
        return this.snapshotsUpdated;
    }

    void setSnapshotsUpdated() {
        this.snapshotsUpdated = true;
    }

    boolean updateSnapshots() {
        return extensionContext.getRequiredTestClass()
                .getAnnotation(SnapshotAssertions.class)
                .updateSnapshots();
    }

    String getTestClassName() {
        return extensionContext.getRequiredTestClass().getName();
    }

    String determineNextSnapshotName() {
        return extensionContext.getRequiredTestMethod().getName() + "_" + counter++;
    }

    private Path determineSnapshotDirecotry() throws IOException {
        return DirectoryResolver.resolveSnapshotDirectory(extensionContext);
    }

    Path determineSnapshotFile(String snapshotName) throws IOException {
        return determineSnapshotDirecotry().resolve(snapshotName + ".snapshot");
    }
}
