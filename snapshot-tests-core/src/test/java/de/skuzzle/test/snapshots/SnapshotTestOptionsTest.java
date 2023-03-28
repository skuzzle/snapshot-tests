package de.skuzzle.test.snapshots;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SnapshotTestOptions
public class SnapshotTestOptionsTest {

    @Test
    void testCorrectDefaultValueForNormalizeLineEndings() {
        final SnapshotTestOptions annotation = SnapshotTestOptionsTest.class.getAnnotation(SnapshotTestOptions.class);
        assertThat(annotation.normalizeLineEndings()).isEqualTo(SnapshotTestOptions.DEFAULT_NORMALIZE_LINE_ENDINGS);
    }

    @Test
    void testCorrectDefaultValueForDiffFormat() {
        final SnapshotTestOptions annotation = SnapshotTestOptionsTest.class.getAnnotation(SnapshotTestOptions.class);
        assertThat(annotation.diffFormat()).isEqualTo(SnapshotTestOptions.DEFAULT_DIFF_FORMAT);
    }
}
