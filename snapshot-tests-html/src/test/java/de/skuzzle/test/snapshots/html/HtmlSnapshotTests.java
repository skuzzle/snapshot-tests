package de.skuzzle.test.snapshots.html;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.data.html.HtmlSnapshot;

@EnableSnapshotTests
public class HtmlSnapshotTests {

    @Test
    void testCompareHtml(Snapshot snapshot) throws Exception {
        snapshot.assertThat("<html><body><h1>Test</h1></body></html>").as(HtmlSnapshot.html())
                .matchesSnapshotStructure();

    }
}
