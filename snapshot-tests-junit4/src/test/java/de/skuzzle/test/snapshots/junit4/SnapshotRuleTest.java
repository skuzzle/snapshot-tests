package de.skuzzle.test.snapshots.junit4;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class SnapshotRuleTest {
    @Rule
    @ClassRule
    public static final SnapshotRule snapshot = SnapshotRule.enableSnapshotTests();

    @Test
    public void test() {
        snapshot
                .assertThat("xyz")
                .asText().matchesSnapshotText();
    }

}
