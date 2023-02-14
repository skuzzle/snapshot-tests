package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotDirectory;
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

    @ParameterizedTest
    @FilesFrom(testResourcesDirectory = "test-input", extensions = "txt", filter = LegacyPathFilter.class)
    void testWithLegacyPathFilter(TestFile testFile, Snapshot snapshot) throws IOException {
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

    static class LegacyPathFilter implements PathFilter {
        @Override
        public boolean include(Path path) throws IOException {
            return path.getFileName().toString().equals("input1.txt");
        }
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
