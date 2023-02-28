package de.skuzzle.test.snapshots.snippets;

import java.nio.file.Path;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotDirectoryStrategy;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@EnableSnapshotTests
class ChangeDirectory {

    // tag::changeDirViaDSL[]
    @Test
    void testChangeDirectoryViaDSL(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot
                .in(Path.of("src", "test", "resources", "snapshots")) // <1>
                .assertThat(person)
                .asText()
                .matchesSnapshotText();
    }
    // end::changeDirViaDSL[]

    @Nested
    // tag::changeDirViaAnnotation[]
    @EnableSnapshotTests
    @SnapshotDirectory("snapshots") // <1>
    class ChangeDirectoryViaAnnotationTest {
        @Test
        void testChangeDirectoryViaAnnotation(Snapshot snapshot) {
            final Person person = Person.determinePerson();
            snapshot
                    .assertThat(person)
                    .asText()
                    .matchesSnapshotText();
        }
    }

    // end::changeDirViaAnnotation[]

    @Nested
    // tag::changeDirViaStrategy[]
    @EnableSnapshotTests
    @SnapshotDirectory(determinedBy = ResolveSnapshotDirectory.class) // <1>
    class ChangeDirectoryViaStrategyTest {
        @Test
        void testChangeDirectoryViaStrategy(Snapshot snapshot) {
            final Person person = Person.determinePerson();
            snapshot
                    .assertThat(person)
                    .asText()
                    .matchesSnapshotText();
        }
    }

    public static class ResolveSnapshotDirectory implements SnapshotDirectoryStrategy { // <2>

        @Override
        public Path determineSnapshotDirectory(Class<?> testClass, SnapshotDirectory directory)
                throws SnapshotException {
            return Path.of("src", "test", "resources", "snapshots");
        }

    }
    // end::changeDirViaStrategy[]
}
