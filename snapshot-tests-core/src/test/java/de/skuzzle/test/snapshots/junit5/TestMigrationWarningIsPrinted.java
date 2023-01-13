package de.skuzzle.test.snapshots.junit5;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Simon Taddiken
 * @deprecated Since 1.8.0 - Test can be removed with release 2.0.0
 */
@Deprecated(since = "1.8.0", forRemoval = true)
@EnableSnapshotTests
class TestMigrationWarningIsPrinted {

    @Test
    void testWarningPrinted() throws Exception {
        Assertions.assertThat(DetectJunit5Module.WARNING_PRINTED).isTrue();
    }
}
