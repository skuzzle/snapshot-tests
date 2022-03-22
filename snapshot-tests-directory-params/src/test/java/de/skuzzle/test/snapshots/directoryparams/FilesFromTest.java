package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

@EnableSnapshotTests(snapshotDirectory = "test-input")
public class FilesFromTest {

    @ParameterizedTest
    @FilesFrom(directory = "test-input", extensions = "txt")
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
