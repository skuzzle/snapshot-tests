package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotFile;
import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.SnapshotTestResult.SnapshotStatus;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class ChangeSnapshotHeaderTest {

    @Test
    void testUpdateSnapshotHeader(Snapshot snapshot) throws Exception {
        final String snapshotName = "change_header_test";
        final SnapshotConfiguration configuration = DefaultSnapshotConfiguration
                .forTestClass(ChangeSnapshotHeaderTest.class);
        final Path snapshotDirectory = configuration.determineSnapshotDirectory();
        final Path snapshotFilePath = snapshotDirectory
                .resolve(InternalSnapshotNaming.getSnapshotFileName(snapshotName));

        SnapshotHeader expectedHeader = null;
        if (Files.exists(snapshotFilePath)) {
            final SnapshotFile existingSnapshotFile = SnapshotFile.fromSnapshotFile(snapshotFilePath);
            expectedHeader = existingSnapshotFile.header();

            existingSnapshotFile.changeHeader(SnapshotHeader.fromMap(Map.of("modified", "whatever")))
                    .writeTo(snapshotFilePath);
        }

        final SnapshotTestResult result = snapshot
                .named(snapshotName)
                .assertThat("abc")
                .asText()
                .matchesSnapshotText();

        assertThat(result.status()).isEqualTo(SnapshotStatus.ASSERTED);
        final SnapshotFile snapshotFile = SnapshotFile.fromSnapshotFile(result.targetFile());
        assertThat(snapshotFile.header()).isEqualTo(expectedHeader);
    }
}
