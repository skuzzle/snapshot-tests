package de.skuzzle.test.snapshots;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.extension.ExtensionContext;

class SnapshotImpl implements Snapshot {

    private final ExtensionContext extensionContext;
    private int counter;

    public SnapshotImpl(ExtensionContext extensionContext) {
        this.extensionContext = extensionContext;
    }

    @Override
    public DiffAssertions assertThat(Object actual) {
        return new DiffAssertionsImpl(this, actual);
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
