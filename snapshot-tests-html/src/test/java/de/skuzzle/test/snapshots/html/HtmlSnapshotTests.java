package de.skuzzle.test.snapshots.html;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.data.html.HtmlStructuralAssertions;

@EnableSnapshotTests
public class HtmlSnapshotTests {

    @Test
    void testCompareHtml(Snapshot snapshot) throws Exception {
        snapshot.assertThat("<html><body><h1>Test</h2></body></html>").as(Object::toString)
                .matchesAccordingTo(new HtmlStructuralAssertions());

    }
}
