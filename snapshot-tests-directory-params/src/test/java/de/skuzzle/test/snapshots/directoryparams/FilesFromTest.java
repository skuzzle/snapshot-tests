package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.params.ParameterizedTest;

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

    @ParameterizedTest
    @FilesFrom(testResourcesDirectory = "test-input", extensions = "txt", filter = SimpleTestFileFilter.class)
    void testWithTestFileFilter(TestFile testFile, Snapshot snapshot) throws IOException {
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

    static class SimpleTestFileFilter implements TestFileFilter {

        @Override
        public boolean include(TestFile testFile, boolean recursive) throws IOException {
            return testFile.name().equals("input1");
        }
    }

    private String transform(String input) {
        return input;
    }
}
