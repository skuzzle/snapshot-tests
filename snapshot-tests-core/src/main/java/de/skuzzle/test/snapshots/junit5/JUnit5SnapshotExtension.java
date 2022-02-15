package de.skuzzle.test.snapshots.junit5;

import java.lang.reflect.Method;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import de.skuzzle.test.snapshots.impl.InternalSnapshotTest;

/**
 * This class is only public so it can be referenced by the entry point annotation.
 *
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL)
public final class JUnit5SnapshotExtension implements
        ParameterResolver,
        BeforeAllCallback,
        AfterEachCallback,
        AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        Junit5SnapshotTestContextProvider.create(extensionContext);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        final var snapshotTestContext = Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext);
        final var parameterType = parameterContext.getParameter().getType();
        return snapshotTestContext.isSnapshotParameter(parameterType);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        final var snapshotTestContext = Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext);

        final Method testMethod = extensionContext.getRequiredTestMethod();
        return snapshotTestContext.createSnapshotTestFor(testMethod);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        final var snapshotTestContext = Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext);

        extensionContext.getExecutionException()
                .ifPresent(__ -> snapshotTestContext.recordFailedTest(extensionContext.getRequiredTestMethod()));

        final InternalSnapshotTest snapshotTest = snapshotTestContext.clearCurrentSnapshotTest().orElse(null);
        if (snapshotTest != null) {
            snapshotTestContext.recordSnapshotTestResults(snapshotTest.testResults());
            snapshotTest.executeAssertions();
        }
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        final var snapshotTestContext = Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext);
        snapshotTestContext.detectOrCleanupOrphanedSnapshots();
    }

}