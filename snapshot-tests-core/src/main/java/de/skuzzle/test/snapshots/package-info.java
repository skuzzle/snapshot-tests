/**
 * <h1>The main public snapshot-testing API.</h1>
 * <p>
 * Snapshot tests are enabled for a test class via
 * {@link de.skuzzle.test.snapshots.EnableSnapshotTests}. This will allow you to inject an
 * instance of {@link de.skuzzle.test.snapshots.SnapshotDsl.Snapshot} into any test method
 * within said test class.
 *
 * <pre>
 * &#64;EnableSnapshotTests
 * class SomethingSomethingTest {
 *
 *     &#64;Test
 *     void test_to_string_representation(Snapshot snapshot) throws Exception {
 *         Object actualTestResult = subject.codeUnterTest(...);
 *         snapshot.assertThat(actualTestResult)
 *             .asText()
 *             .matchesSnapshotText();
 *     }
 *
 *     &#64;Test
 *     void test_json_serialization(Snapshot snapshot) throws Exception {
 *         Object actualTestResult = subject.codeUnterTest(...);
 *         snapshot.assertThat(actualTestResult)
 *             .as(XmlSnapshot.xml)
 *             .matchesSnapshotStructure();
 *     }
 * }
 * </pre>
 *
 * @see de.skuzzle.test.snapshots.EnableSnapshotTests
 * @see de.skuzzle.test.snapshots.SnapshotDsl.Snapshot
 */
@API(status = Status.STABLE)
package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
