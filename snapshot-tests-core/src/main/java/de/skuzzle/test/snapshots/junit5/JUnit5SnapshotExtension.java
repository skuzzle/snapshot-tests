package de.skuzzle.test.snapshots.junit5;

import java.lang.reflect.Method;
import java.util.Optional;

import de.skuzzle.test.snapshots.impl.SnapshotTestContext;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestWatcher;

@API(status = Status.INTERNAL)
class JUnit5SnapshotExtension implements
        ParameterResolver,
        BeforeAllCallback,
        AfterEachCallback,
        AfterAllCallback,
        TestWatcher {

    // just load the class to execute its static block for printing a
    // deprecation/migration warning
    static final DetectJunit5Module DETECT_JUNIT5 = new DetectJunit5Module();

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        Junit5SnapshotTestContextProvider.create(extensionContext);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        final var snapshotTestContext = Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext);
        final var parameterType = parameterContext.getParameter().getType();
        return snapshotTestContext.isSnapshotParameter(parameterType)
                || SnapshotTestContext.class.isAssignableFrom(parameterType);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {

        final var snapshotTestContext = Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext);
        if (parameterContext.getParameter().getType().isAssignableFrom(SnapshotTestContext.class)) {
            return snapshotTestContext;
        }

        final Method testMethod = extensionContext.getRequiredTestMethod();
        return snapshotTestContext.createSnapshotTestFor(testMethod);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext)
                .finalizeSnapshotTest();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext)
                .detectOrCleanupOrphanedSnapshots();
    }

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable cause) {
        Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext)
                .recordFailedOrSkippedTest(extensionContext.getRequiredTestMethod());
    }

    @Override
    public void testAborted(ExtensionContext extensionContext, Throwable cause) {
        Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext)
                .recordFailedOrSkippedTest(extensionContext.getRequiredTestMethod());
    }

    @Override
    public void testDisabled(ExtensionContext extensionContext, Optional<String> reason) {
        Junit5SnapshotTestContextProvider.fromExtensionContext(extensionContext)
                .recordFailedOrSkippedTest(extensionContext.getRequiredTestMethod());
    }
}
