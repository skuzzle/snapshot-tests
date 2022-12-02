package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;

import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
@SnapshotDirectory("test-input")
public class FilesFromTest {

    @ParameterizedTest
    @FilesFrom(testResourcesDirectory = "test-input", extensions = "txt")
    void test(TestFile testFile, Snapshot snapshot) throws IOException {
        // Given
        final String testInput = testFile.asText(StandardCharsets.UTF_8, Map.of("variable", testFile.name()));

        // When
        final String actualTestResult = transform(testInput);

        // Then
        snapshot.named(testFile.name())
                .assertThat(actualTestResult)
                .asText()
                .matchesSnapshotText();
    }

    private String transform(String input) {
        return input;
    }
}
