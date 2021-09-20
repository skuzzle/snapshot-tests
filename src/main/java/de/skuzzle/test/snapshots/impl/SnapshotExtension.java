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

        return extensionContext.getStore(NAMESPACE).getOrComputeIfAbsent(KEY_SNAPSHOT_INSTANCE,
                k -> new SnapshotImpl(extensionContext));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        final SnapshotImpl snapshotImpl = context.getStore(NAMESPACE).get(KEY_SNAPSHOT_INSTANCE, SnapshotImpl.class);
        if (snapshotImpl.wasUpdatedForcefully()) {
            throw new AssertionError(String.format(
                    "Snapshots have been updated forcefully.%n"
                            + "Remove 'updateSnapshots = true' attribute from your test class %s and calls to 'justUpdateSnapshot()' and run the tests again.",
                    snapshotImpl.getTestClassName()));
        }
        if (snapshotImpl.wasCreatedInitially()) {
            throw new AssertionError(String.format(
                    "Snapshots have been created the first time.%nRun the test again and you should see it succeed."));
        }
    }

}
