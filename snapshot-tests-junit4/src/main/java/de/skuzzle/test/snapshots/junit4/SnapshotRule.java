package de.skuzzle.test.snapshots.junit4;

import java.lang.reflect.Method;
import java.nio.file.Path;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import de.skuzzle.test.snapshots.DeleteOrphanedSnapshots;
import de.skuzzle.test.snapshots.ForceUpdateSnapshots;
import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseName;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.StructuredDataProvider;
import de.skuzzle.test.snapshots.impl.SnapshotConfiguration;
import de.skuzzle.test.snapshots.impl.SnapshotTestContext;
import de.skuzzle.test.snapshots.validation.State;

/**
 * <h2>Enabling snapshot tests</h2>
 * <p>
 * To get started with snapshot assertions you need to add this rule implementation as
 * both a &#64;Rule and &#64;ClassRule like this:
 * 
 * <pre>
 * &#64;Rule
 * &#64;ClassRule
 * public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();
 * </pre>
 * 
 * This allows you to write snapshot assertions in your tests like this
 *
 * <pre>
 * public class MyTestClass {
 *     &#64;Rule
 *     &#64;ClassRule
 *     public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();
 *     
 *     &#64;Test
 *     public void testSomething() throws Exception {
 *         Object actual = ...
 *         snapshot.assertThat(actual).asText().matchesSnapshotText();
 *     }
 * }
 * </pre>
 * <p>
 * <code>asText()</code> will 'serialize' actual test results using
 * {@link Object#toString()}. There are additional {@link StructuredDataProvider}
 * implementations that allow to serialize snapshots as json, xml or html. To use them,
 * you need to declare their respective maven modules as dependency.
 *
 * <pre>
 *     &#64;Test
 *     public void testSomething() throws Exception {
 *         Object actual = ...
 *         snapshot.assertThat(actual).as(TextSnapshot.text).matchesSnapshotText();
 *         snapshot.assertThat(actual).as(JsonSnapshot.json).matchesSnapshotText();
 *         snapshot.assertThat(actual).as(XmlSnapshot.xml).matchesSnapshotText();
 *     }
 * </pre>
 * <p>
 * When providing a structured data format like json/xml (or in general: an implementation
 * of {@link StructuredDataProvider}) you can make use of <em>structural assertions</em>
 * to compare snapshots. Depending on the implementation, those might provide better error
 * messages than plain text comparison.
 *
 * <pre>
 *     &#64;Test
 *     public void testSomething() throws Exception {
 *         Object actual = ...
 *         snapshot.assertThat(actual).as(JsonSnapshot.json).matchesSnapshotStructure();
 *         snapshot.assertThat(actual).as(XmlSnapshot.xml).matchesSnapshotStructure();
 *     }
 * </pre>
 *
 * <h2>Updating snapshots</h2>
 * <p>
 * Snapshots can become outdated when your code under test changes on purpose. In that
 * case you can advice the framework to override existing snapshots with your code under
 * test's actual result by placing the annotation {@link ForceUpdateSnapshots} on either
 * the whole snapshot test class or on a single test method. You can also use the
 * {@link ChooseAssertions#justUpdateSnapshot()} terminal operation of the DSL.
 *
 * <h2>Orphaned snapshots</h2>
 * <p>
 * Snapshot files can become orphans if, for example you rename a test class/method or you
 * change the snapshot assertions within a test. This framework comes with a sophisticated
 * approach for detecting those orphaned files. By default, we will log a warning with the
 * found orphan. You can temporarily place the {@link DeleteOrphanedSnapshots} annotation
 * on a snapshot test class to have those files deleted automatically.
 *
 * @author Simon Taddiken
 * @see Snapshot
 * @see SnapshotNaming
 * @see SnapshotDirectory
 * @see DeleteOrphanedSnapshots
 * @see ForceUpdateSnapshots
 * @since 1.8.0
 */
@API(status = Status.EXPERIMENTAL, since = "1.8.0")
public final class SnapshotRule implements CombinedRule, Snapshot {

    private SnapshotTestContext context;
    private Snapshot currentSnapshot;

    /**
     * Creates a new rule instance. Note that you need to add both &#64;Rule and
     * &#64;ClassRule annotation like this:
     * 
     * <pre>
     * &#64;Rule
     * &#64;ClassRule
     * public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();
     * </pre>
     * 
     * @return The new rule instance.
     */
    public static SnapshotRule enableSnapshotTests() {
        return new SnapshotRule();
    }

    private SnapshotRule() {
        // hidden
    }

    private SnapshotTestContext currentContext() {
        State.check(context != null,
                "No SnapshotTestContext found. Make sure that the Rule instance is static and annotated with both @Rule *and* @ClassRule");
        return context;
    }

    private Snapshot currentSnapshot() {
        State.check(currentSnapshot != null, "There is no current snapshot");
        return currentSnapshot;
    }

    private Method getTestMethod(Description description) {
        try {
            return description.getTestClass().getMethod(description.getMethodName());
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException("Could not determine test method");
        }
    }

    /**
     * @deprecated Method is just for internal use.
     */
    @Override
    @Deprecated
    @API(status = Status.INTERNAL)
    public void beforeClass(Description description) throws Exception {
        final Class<?> testClass = description.getTestClass();
        final SnapshotConfiguration configuration = SnapshotConfiguration.defaultConfigurationFor(testClass);
        context = SnapshotTestContext.forConfiguration(configuration);
        new TestClass(testClass)
                .getAnnotatedMethods(Ignore.class).stream()
                .map(FrameworkMethod::getMethod)
                .forEach(context::recordFailedOrSkippedTest);
    }

    /**
     * @deprecated Method is just for internal use.
     */
    @Override
    @Deprecated
    @API(status = Status.INTERNAL)
    public void afterClass(Description description) {
        currentContext().detectOrCleanupOrphanedSnapshots();
    }

    /**
     * @deprecated Method is just for internal use.
     */
    @Override
    @Deprecated
    @API(status = Status.INTERNAL)
    public void before(Description description) throws Exception {
        final Method testMethod = getTestMethod(description);
        currentSnapshot = currentContext().createSnapshotTestFor(testMethod);
    }

    /**
     * @deprecated Method is just for internal use.
     */
    @Override
    @Deprecated
    @API(status = Status.INTERNAL)
    public void after(Description description) throws Exception {
        try {
            currentContext().finalizeSnapshotTest();
        } catch (final AssumptionViolatedException e) {
            onSkippedTest(description);
            throw e;
        } finally {
            currentSnapshot = null;
        }
    }

    /**
     * @deprecated Method is just for internal use.
     */
    @Override
    @Deprecated
    @API(status = Status.INTERNAL)
    public void onFailedTest(Description description) {
        final Method testMethod = getTestMethod(description);
        currentContext().recordFailedOrSkippedTest(testMethod);
    }

    /**
     * @deprecated Method is just for internal use.
     */
    @Override
    @Deprecated
    @API(status = Status.INTERNAL)
    public void onSkippedTest(Description description) {
        final Method testMethod = getTestMethod(description);
        currentContext().recordFailedOrSkippedTest(testMethod);
    }

    @Override
    public ChooseDataFormat assertThat(Object actual) {
        return currentSnapshot().assertThat(actual);
    }

    @Override
    public ChooseActual namedAccordingTo(SnapshotNaming namingStrategy) {
        return currentSnapshot().namedAccordingTo(namingStrategy);
    }

    @Override
    public ChooseName in(Path directory) {
        return currentSnapshot().in(directory);
    }

}
