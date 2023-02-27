package de.skuzzle.test.snapshots.data.xml;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class XmlSnapshotTest {
    @Test
    @Deprecated
    void testLegacyWarning() {
        XmlSnapshot.xml();
        assertThat(XmlSnapshot.LEGACY_WARNING_PRINTED).isTrue();
    }
}