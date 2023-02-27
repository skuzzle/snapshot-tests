package de.skuzzle.test.snapshots.data.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class SnapshotsTest {
    @Test
    @Deprecated
    void testLegacyWarning() {
        XmlSnapshot.xml();
        assertThat(XmlSnapshot.LEGACY_WARNING_PRINTED).isTrue();
    }
}
