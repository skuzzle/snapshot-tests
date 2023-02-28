package de.skuzzle.test.snapshots;

import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseStructure;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Allows to do snapshot assertions. An instance of this class can be injected into your
 * test case by just specifying a parameter of this type:
 *
 * <pre>
 * &#64;Test
 * void test(Snapshot snapshot) throws Exception {
 *     ...
 *     snapshot.assertThat(...)...
 * }
 * </pre>
 *
 * Note that the respective test class must be annotated with {@link EnableSnapshotTests},
 * otherwise the test framework will not be able to resolve the <code>Snapshot</code>
 * parameter of the test method.
 * <p>
 * Note that the Snapshot instance that is being injected into your test is stateful and
 * not thread safe. You should also refrain from using incomplete DSL usages. You must
 * always end the DSL call chain with any of the terminal operations
 * {@link ChooseAssertions#disabled()}, {@link ChooseAssertions#disabledBecause(String)},
 * {@link ChooseAssertions#matchesAccordingTo(StructuralAssertions)},
 * {@link ChooseAssertions#matchesSnapshotText()} or
 * {@link ChooseStructure#matchesSnapshotStructure()}.
 * <p>
 * Each method in the DSL advances the Snapshot instance's internal state until a terminal
 * operation has been called. So it is illegal to reuse DSL stages that have already been
 * completed like in this example:
 *
 * <pre>
 * snapshot.assertThat(actual);
 * snapshot.assertThat(actual);
 * </pre>
 *
 * This snippet does not only use two incomplete usages, but also tries to re-use the
 * 'choose-actual' stage without having called a terminal operation.
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE, since = "1.8.0")
public interface Snapshot extends SnapshotDsl.Snapshot {

}
