package de.skuzzle.test.snapshots.junit5;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * This class is only public so it can be referenced by the entry point annotation.
 *
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL)
public final class LegacyJUnit5SnapshotExtension extends JUnit5SnapshotExtension {
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        Junit5SnapshotTestContextProvider.createLegacy(extensionContext);
    }
}
