package de.skuzzle.test.snapshots.impl;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

public final class SnapshotExtension implements ParameterResolver, AfterEachCallback {

    private static final Namespace NAMESPACE = Namespace.create(SnapshotExtension.class);
    private static final String KEY_SNAPSHOT_INSTANCE = "SNAPSHOT_INSTANCE";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        return Snapshot.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        final Object snapshotInstance = new SnapshotImpl(extensionContext);
        extensionContext.getStore(NAMESPACE).put(KEY_SNAPSHOT_INSTANCE, snapshotInstance);
        return snapshotInstance;
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        final SnapshotImpl snapshotImpl = context.getStore(NAMESPACE).get(KEY_SNAPSHOT_INSTANCE, SnapshotImpl.class);
        if (snapshotImpl.snapshotsUpdated()) {
            throw new AssertionError(String.format(
                    "Snapshots have been updated or created the first time.%nRemove 'updateSnapshots = true'  attribute from your test class %s and calls to 'justUpdateSnapshot()' and run the tests again.",
                    snapshotImpl.getTestClassName()));
        }
    }

}
