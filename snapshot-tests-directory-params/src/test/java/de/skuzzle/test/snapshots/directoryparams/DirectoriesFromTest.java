package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.params.ParameterizedTest;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class DirectoriesFromTest {

    @ParameterizedTest
    @DirectoriesFrom(testResourcesDirectory = "test-directories")
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

    @ParameterizedTest
    @DirectoriesFrom(testResourcesDirectory = "test-directories-recursive", recursive = true,
            isTestcaseDeterminedBy = TestIsTestCaseDirectoryStrategy.class)
    void testDirectoriesRecursively(TestDirectory directory, Snapshot snapshot) throws IOException {
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

    static class TestIsTestCaseDirectoryStrategy implements IsTestCaseDirectoryStrategy {

        @Override
        public boolean determineIsTestCaseDirectory(TestDirectory directory) {
            return directory.hasFile("input1.txt");
        }

    }

    private String transform(String input1, String input2) {
        return input1 + input2;
    }
}
