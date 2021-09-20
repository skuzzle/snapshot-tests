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

        final SnapshotConfiguration configuration = SnapshotConfiguration.fromExtensionContext(extensionContext);
        return extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(KEY_SNAPSHOT_INSTANCE, k -> new SnapshotImpl(configuration, extensionContext));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        final SnapshotImpl snapshotImpl = context.getStore(NAMESPACE).get(KEY_SNAPSHOT_INSTANCE, SnapshotImpl.class);

        snapshotImpl.finalizeAssertions();
    }

}
