package de.skuzzle.test.snapshots.junit5;

import java.util.Optional;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import de.skuzzle.test.snapshots.impl.SnapshotConfiguration;
import de.skuzzle.test.snapshots.impl.SnapshotTestContext;

/**
 * Manages the lifecycle of a {@link SnapshotTestContext} instance by attaching it to the
 * JUnit5's {@link ExtensionContext}.
 *
 * @author Simon Taddiken
 * @since 1.1.0
 */
@API(status = Status.INTERNAL, since = "1.1.0")
final class Junit5SnapshotTestContextProvider {

    private static final Namespace NAMESPACE = Namespace.create(JUnit5SnapshotExtension.class);
    private static final String KEY_SELF = "SNAPSHOT_CONTEXT";

    /**
     * Retrieves the test context from the given extension context. Traverses the
     * extension context's parents if the test context could not be found directly.
     * <p>
     * Throws an exception if the {@link SnapshotTestContext} could not be found.
     *
     * @param extensionContext The extension context.
     * @return The {@link SnapshotTestContext} that has prior been attached by
     *         {@link #create(ExtensionContext)}.
     */
    public static SnapshotTestContext fromExtensionContext(ExtensionContext extensionContext) {
        return searchParents(extensionContext)
                .orElseThrow(() -> new IllegalStateException(
                        "SnapshotTestContext not found on given ExtensionContext or any of its parents"));
    }

    /**
     * Creates a {@link SnapshotTestContext} and attaches it to the given JUnit5
     * {@link ExtensionContext}. The extension context is assumed to be pertaining to the
     * test class (as opposed to pertaining to a single test method)
     *
     * @param extensionContext The extension context to attach to.
     * @return The attached {@link SnapshotTestContext}.
     */
    public static SnapshotTestContext create(ExtensionContext extensionContext) {
        final var testClass = extensionContext.getRequiredTestClass();
        final SnapshotConfiguration snapshotConfiguration = SnapshotConfiguration.defaultConfigurationFor(testClass);
        final var snapshotTestContext = SnapshotTestContext.forConfiguration(snapshotConfiguration);
        extensionContext.getStore(NAMESPACE).put(KEY_SELF, snapshotTestContext);
        return snapshotTestContext;
    }

    /**
     * Creates a {@link SnapshotTestContext} and attaches it to the given JUnit5
     * {@link ExtensionContext}. The extension context is assumed to be pertaining to the
     * test class (as opposed to pertaining to a single test method)
     *
     * @param extensionContext The extension context to attach to.
     * @return The attached {@link SnapshotTestContext}.
     * @since 1.7.0
     * @deprecated Since 1.7.0 - Only introduced to handle backward compatibility.
     */
    @Deprecated(since = "1.7.0")
    public static SnapshotTestContext createLegacy(ExtensionContext extensionContext) {
        final var testClass = extensionContext.getRequiredTestClass();
        final SnapshotConfiguration snapshotConfiguration = SnapshotConfiguration.legacyConfigurationFor(testClass);
        final var snapshotTestContext = SnapshotTestContext.forConfiguration(snapshotConfiguration);
        extensionContext.getStore(NAMESPACE).put(KEY_SELF, snapshotTestContext);
        return snapshotTestContext;
    }

    private static Optional<SnapshotTestContext> searchParents(ExtensionContext extensionContext) {
        ExtensionContext current = extensionContext;
        SnapshotTestContext snapshotTestContext;
        while (current != null) {
            snapshotTestContext = current.getStore(NAMESPACE).get(KEY_SELF, SnapshotTestContext.class);
            if (snapshotTestContext != null) {
                return Optional.of(snapshotTestContext);
            }
            current = current.getParent().orElse(null);
        }
        return Optional.empty();
    }
}
