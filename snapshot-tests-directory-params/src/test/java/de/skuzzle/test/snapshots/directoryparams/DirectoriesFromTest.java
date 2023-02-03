package de.skuzzle.test.snapshots.directoryparams;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class DirectoriesFromTest {

    private int expectedResultCount;
    private int actualResultCount;

    @ParameterizedTest
    @Order(1)
    @DirectoriesFrom(testResourcesDirectory = "test-directories")
    void testDirectories(TestDirectory directory, Snapshot snapshot) throws IOException {
        this.expectedResultCount = 2;
        this.actualResultCount++;
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

    @Test
    @Order(2)
    void testDirectoriesResultCount() {
        final int expected = expectedResultCount;
        final int actual = actualResultCount;
        this.expectedResultCount = 0;
        this.actualResultCount = 0;
        assertThat(actual).isEqualByComparingTo(expected);
    }

    @ParameterizedTest
    @Order(3)
    @DirectoriesFrom(testResourcesDirectory = "test-directories-recursive", recursive = true)
    void testDirectoriesRecursivelyDefaultFilter(TestDirectory directory, Snapshot snapshot) throws IOException {
        this.expectedResultCount = 2;
        this.actualResultCount++;
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

    @Test
    @Order(4)
    void testDirectoriesRecursivelyDefaultFilterResultCount() {
        final int expected = expectedResultCount;
        final int actual = actualResultCount;
        this.expectedResultCount = 0;
        this.actualResultCount = 0;
        assertThat(actual).isEqualByComparingTo(expected);
    }

    @ParameterizedTest
    @Order(5)
    @DirectoriesFrom(testResourcesDirectory = "test-directories-recursive", recursive = true,
            filter = SampleTestDirectoryFilter.class)
    void testDirectoriesRecursivelyCustomFilter(TestDirectory directory, Snapshot snapshot) throws IOException {
        this.expectedResultCount = 1;
        this.actualResultCount++;
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

    @Test
    @Order(6)
    void testDirectoriesRecursivelyCustomFilterResultCount() {
        final int expected = expectedResultCount;
        final int actual = actualResultCount;
        this.expectedResultCount = 0;
        this.actualResultCount = 0;
        assertThat(actual).isEqualByComparingTo(expected);
    }

    static class SampleTestDirectoryFilter implements TestDirectoryFilter {

        @Override
        public boolean include(TestDirectory testDirectory, boolean recursive) throws IOException {
            return testDirectory.name().equals("v1-test1");

        }

    }

    private String transform(String input1, String input2) {
        return input1 + input2;
    }
}
