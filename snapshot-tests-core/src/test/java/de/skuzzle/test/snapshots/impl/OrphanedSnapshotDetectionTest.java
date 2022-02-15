package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.EnableSnapshotTests;

@EnableSnapshotTests
public class OrphanedSnapshotDetectionTest {

    void failingTestMethod() {
        throw new RuntimeException();
    }

    @Test
    void test() throws Throwable {
        final SnapshotTestContext context = SnapshotTestContext.forTestClass(OrphanedSnapshotDetectionTest.class);
        context.recordFailedTest(OrphanedSnapshotDetectionTest.class.getDeclaredMethod("failingTestMethod"));
        final Collection<Path> orphanedSnapshots = context.detectOrCleanupOrphanedSnapshots();
        assertThat(orphanedSnapshots).hasSize(1);
    }

}
