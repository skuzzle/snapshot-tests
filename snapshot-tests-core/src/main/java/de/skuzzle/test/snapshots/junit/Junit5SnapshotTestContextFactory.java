package de.skuzzle.test.snapshots.junit;

import java.util.Optional;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;

import de.skuzzle.test.snapshots.impl.DefaultSnapshotConfiguration;
import de.skuzzle.test.snapshots.impl.SnapshotTestContext;

/**
 * Manages the lifecycle of a {@link SnapshotTestContext} instance by attaching it to the
 * JUnit5's {@link ExtensionContext}.
 *
 * @author Simon Taddiken
 */
final class Junit5SnapshotTestContextFactory {

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
     * @param extensionContext
     * @return
     */
    public static SnapshotTestContext create(ExtensionContext extensionContext) {
        searchParents(extensionContext)
                .ifPresent(existingContext -> {
                    throw new IllegalStateException(
                            "There is already a SnapshotTestContext attached to the given ExtensionContext or any of its parents");
                });

        final Class<?> testClass = extensionContext.getRequiredTestClass();
        final var configuration = DefaultSnapshotConfiguration.forTestClass(testClass);
        final var snapshotTestContext = SnapshotTestContext.forConfiguration(configuration);
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
