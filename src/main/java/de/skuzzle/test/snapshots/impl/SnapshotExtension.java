package de.skuzzle.test.snapshots.impl;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

public final class SnapshotExtension implements ParameterResolver, AfterEachCallback,
        BeforeAllCallback, AfterAllCallback {

    private static final Namespace NAMESPACE = Namespace.create(SnapshotExtension.class);
    private static final String KEY_SNAPSHOT_INSTANCE = "SNAPSHOT_INSTANCE";
    private static final String KEY_RESULT_COLLECTOR_INSTANCE = "RESULT_COLLECTOR_INSTANCE";
    private static final String KEY_SNAPSHOT_CONFIGURATION_INSTANCE = "SNAPSHOT_CONFIGURATION_INSTANCE";

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        final SnapshotConfiguration configuration = SnapshotConfiguration.fromExtensionContext(extensionContext);
        final GlobalResultCollector globalResultCollector = new GlobalResultCollector();
        extensionContext.getStore(NAMESPACE).put(KEY_RESULT_COLLECTOR_INSTANCE, globalResultCollector);
        extensionContext.getStore(NAMESPACE).put(KEY_SNAPSHOT_CONFIGURATION_INSTANCE, configuration);

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        return Snapshot.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        final SnapshotConfiguration snapshotConfiguration = extensionContext.getStore(NAMESPACE)
                .get(KEY_SNAPSHOT_CONFIGURATION_INSTANCE, SnapshotConfiguration.class);
        return extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(KEY_SNAPSHOT_INSTANCE,
                        k -> new SnapshotImpl(snapshotConfiguration, extensionContext));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        final SnapshotImpl snapshotImpl = context.getStore(NAMESPACE)
                .get(KEY_SNAPSHOT_INSTANCE, SnapshotImpl.class);
        final GlobalResultCollector globalResultCollector = context.getStore(NAMESPACE)
                .get(KEY_RESULT_COLLECTOR_INSTANCE, GlobalResultCollector.class);

        snapshotImpl.finalizeAssertions(globalResultCollector);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        final GlobalResultCollector globalResultCollector = context.getStore(NAMESPACE)
                .get(KEY_RESULT_COLLECTOR_INSTANCE, GlobalResultCollector.class);
        System.out.println(globalResultCollector);
    }

}
