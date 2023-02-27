package de.skuzzle.test.snapshots.data.json;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Deprecated
class JsonSnapshotTest {

    @Test
    @Deprecated
    void testLegacyWarning() {
        JsonSnapshot.json();
        assertThat(JsonSnapshot.LEGACY_WARNING_PRINTED).isTrue();
    }
}