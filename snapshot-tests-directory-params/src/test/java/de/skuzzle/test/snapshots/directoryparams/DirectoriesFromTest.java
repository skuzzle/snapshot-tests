package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.params.ParameterizedTest;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;

@EnableSnapshotTests
public class DirectoriesFromTest {

    @ParameterizedTest
    @DirectoriesFrom(directory = "test-directories")
    void testDirectories(TestDirectory directory, Snapshot snapshot) throws IOException {
        // Given
        final String input1 = directory.resolve("input1.txt").asText(StandardCharsets.UTF_8);
        final String input2 = directory.resolve("input2.txt").asText(StandardCharsets.UTF_8);

        // When
        final String actualTestResult = transform(input1, input2);

        // Then
        snapshot.in(directory.path())
                .named("expected-output")
                .assertThat(actualTestResult)
                .asText()
                .matchesSnapshotText();
    }

    private String transform(String input1, String input2) {
        return input1 + input2;
    }
}
