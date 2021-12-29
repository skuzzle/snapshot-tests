package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Collection;

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

        final Method testMethod = extensionContext.getRequiredTestMethod();
        return extensionContext.getStore(NAMESPACE)
                .getOrComputeIfAbsent(KEY_SNAPSHOT_INSTANCE,
                        k -> new SnapshotTest(snapshotConfiguration, testMethod));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        final GlobalResultCollector globalResultCollector = extensionContext.getStore(NAMESPACE)
                .get(KEY_RESULT_COLLECTOR_INSTANCE, GlobalResultCollector.class);

        extensionContext.getExecutionException().ifPresent(
                egal -> globalResultCollector.addFailedTestMethod(extensionContext.getRequiredTestMethod()));

        final SnapshotTest snapshotImpl = extensionContext.getStore(NAMESPACE)
                .get(KEY_SNAPSHOT_INSTANCE, SnapshotTest.class);

        if (snapshotImpl != null) {
            snapshotImpl.finalizeTest(globalResultCollector);
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        final GlobalResultCollector globalResultCollector = extensionContext.getStore(NAMESPACE)
                .get(KEY_RESULT_COLLECTOR_INSTANCE, GlobalResultCollector.class);

        final SnapshotConfiguration snapshotConfiguration = extensionContext.getStore(NAMESPACE)
                .get(KEY_SNAPSHOT_CONFIGURATION_INSTANCE, SnapshotConfiguration.class);

        final Path snapshotDirectory = SnapshotDirectoryResolver.resolveSnapshotDirectory(snapshotConfiguration);
        final Collection<Path> orphanedSnapshots = globalResultCollector.findOrphanedSnapshotsIn(snapshotDirectory);
        orphanedSnapshots
                .forEach(orphaned -> {
                    if (snapshotConfiguration.isForceUpdateSnapshots()) {
                        UncheckedIO.delete(orphaned);
                        System.out.println("Deleted orphaned snapshot file " + orphaned);
                    } else {
                        System.out.println(
                                "Found orphaned snapshot file. Run with 'forceUpdateSnapshots' option to remove: "
                                        + orphaned);
                    }
                });
    }

}
