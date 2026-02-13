/**
 * <h2>The main public snapshot-testing API.</h2>
 * <p>
 * Snapshot tests are enabled for a test class either via
 * <code>@EnableSnapshotTests</code> annotation when using JUnit5 or via
 * <code>SnapshotRule</code> when using JUnit4.
 *
 * <p>
 * The following is an example code for usage with JUnit5. Check out the documentation of
 * the JUnit4 module for information about usage with JUnit4.
 * </p>
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
 * @see de.skuzzle.test.snapshots.Snapshot
 */
@API(status = Status.STABLE)
package de.skuzzle.test.snapshots;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
