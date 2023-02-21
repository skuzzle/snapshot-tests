package de.skuzzle.test.snapshots.junit5;

import de.skuzzle.test.snapshots.*;
import de.skuzzle.test.snapshots.impl.SnapshotTestContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@EnableSnapshotTests
@SnapshotTestOptions(alwaysPersistActualResult = true)
public class NestedSnapshotTests {

    @Test
    void testTopLevel(Snapshot snapshot, SnapshotTestContext context) {
        final Class<?> testClass = context.snapshotConfiguration().testClass();
        assertThat(testClass).isEqualTo(NestedSnapshotTests.class);
    }

    @Nested
    @SnapshotTestOptions(alwaysPersistActualResult = false)
    class NestedClassWithSnapshotTests {

        @Test
        void testWithSnapshot(Snapshot snapshot, SnapshotTestContext context) {
            final SnapshotTestResult result = snapshot.assertThat("123").asText().matchesSnapshotText();

            assertThat(result.contextFiles().actualResultFile()).doesNotExist();
            final Class<?> testClass = context.snapshotConfiguration().testClass();
            assertThat(testClass).isEqualTo(NestedClassWithSnapshotTests.class);
        }

        @Nested
        @SnapshotDirectory("nested-snapshot-tests")
        class CommonAncestor {

            @Nested
            @SnapshotTestOptions(alwaysPersistActualResult = true)
            class SecondLevelNest1 {
                @Test
                void testWithSnapshot1(Snapshot snapshot, SnapshotTestContext context) {
                    final SnapshotTestResult result = snapshot.assertThat("123").asText().matchesSnapshotText();
                    assertThat(result.contextFiles().actualResultFile()).exists();
                    final Class<?> testClass = context.snapshotConfiguration().testClass();
                    assertThat(testClass).isEqualTo(SecondLevelNest1.class);
                }
            }

            @Nested
            class SecondLevelNest2 {
                @Test
                void testWithSnapshot2(Snapshot snapshot, SnapshotTestContext context) {
                    final SnapshotTestResult result = snapshot.assertThat("123").asText().matchesSnapshotText();
                    assertThat(result.contextFiles().actualResultFile()).doesNotExist();
                    final Class<?> testClass = context.snapshotConfiguration().testClass();
                    assertThat(testClass).isEqualTo(SecondLevelNest2.class);
                }
            }
        }
    }

    @Nested
    class SiblingNestedClassWithSnapshotTests {

        @Test
        void testWithSnapshot(Snapshot snapshot, SnapshotTestContext context) {
            final SnapshotTestResult result = snapshot.assertThat("123").asText().matchesSnapshotText();

            assertThat(result.contextFiles().actualResultFile()).exists();
            final Class<?> testClass = context.snapshotConfiguration().testClass();
            assertThat(testClass).isEqualTo(SiblingNestedClassWithSnapshotTests.class);
        }
    }
}
