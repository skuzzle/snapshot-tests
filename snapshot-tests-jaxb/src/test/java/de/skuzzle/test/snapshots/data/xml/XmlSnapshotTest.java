package de.skuzzle.test.snapshots.data.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class XmlSnapshotTest {
    @Test
    @Deprecated
    void testLegacyWarning() {
        XmlSnapshot.xml();
        assertThat(XmlSnapshot.LEGACY_WARNING_PRINTED).isTrue();
    }
}