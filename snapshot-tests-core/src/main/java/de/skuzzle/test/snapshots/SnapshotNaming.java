package de.skuzzle.test.snapshots;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseName;

/**
 * Strategy interface for dynamically determining snapshot names. By default, the snapshot
 * name is made up of the test's method name and a serial number representing the count of
 * snapshot assertions within this test method.
 * <p>
 * An instance of this strategy can be passed to the DSL to customize the snapshot naming
 * like this:
 *
 * <pre>
 * snapshot.namedAccordingTo(MyCustomNamingScheme.getInstance()).assertThat(...)...
 * </pre>
 * <p>
 * Customizing the naming strategy is especially needed for paramterized tests, as
 * automatic naming would choose the same snapshot name for each execution.
 *
 * @see ChooseName
 * @since 1.1.0
 * @author Simon Taddiken
 */
@API(status = Status.EXPERIMENTAL, since = "1.1.0")
public interface SnapshotNaming {

    /**
     * The default naming strategy which simply concatenates method name + counter.
     *
     * @return The default naming strategy.
     */
    static SnapshotNaming defaultNaming() {
        return (testMethod, counter) -> testMethod.getName() + "_" + counter;
    }

    /**
     * Creates a naming strategy which always returns the given constant string.
     *
     * @param snapshotName The name of the snapshot.
     * @return Naming strategy which always returns the same name.
     */
    static SnapshotNaming constant(String snapshotName) {
        Objects.requireNonNull(snapshotName, "snapshotName must not be null");
        return (testMethod, counter) -> snapshotName;
    }

    /**
     * Creates a naming strategy similar to the {@linkplain #defaultNaming() default
     * naming strategy}. Besides the counter, this strategy also concatenates the toString
     * representation of various parameter objects to the snapshot name.
     * <p>
     * Using this strategy is useful when using snapshot tests in combination with
     * parameterized tests. This allows to match snapshots to an explicit set of actual
     * parameters with which a test has been executed.
     *
     * @param parameter1 The first parameter.
     * @param furtherParameters Optional further parameters.
     * @return The naming strategy.
     */
    static SnapshotNaming withParameters(Object parameter1, Object... furtherParameters) {
        Objects.requireNonNull(parameter1, "parameter must not be null");
        return (testMethod, counter) -> {
            final String parameterPart = Stream.concat(Stream.of(parameter1), Arrays.stream(furtherParameters))
                    .map(Object::toString)
                    .collect(Collectors.joining("_"));
            return testMethod.getName() + "_" + counter + "_" + parameterPart;
        };
    }

    /**
     * Determines the name of the snapshot being taken. Note that the name will be used as
     * file name for the snapshot file, so all restrictions apply that apply to your OS's
     * file naming.
     * <p>
     * Note that the result must be deterministic, otherwise the framework will not be
     * able to match a previously persisted snapshot with a currently executed snapshot
     * assertion.
     *
     * @param testMethod The test method.
     * @param counter Serial number of snapshot assertions within that test method (0
     *            based).
     * @return The name of the snapshot.
     */
    String determineSnapshotName(Method testMethod, int counter);
}
