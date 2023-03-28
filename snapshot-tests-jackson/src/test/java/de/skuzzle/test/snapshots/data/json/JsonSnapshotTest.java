package de.skuzzle.test.snapshots.data.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@Deprecated
class JsonSnapshotTest {

    @Test
    @Deprecated
    void testLegacyWarning() {
        JsonSnapshot.json();
        assertThat(JsonSnapshot.LEGACY_WARNING_PRINTED).isTrue();
    }
}
