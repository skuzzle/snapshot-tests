package de.skuzzle.test.snapshots.snippets;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotDirectory;
import de.skuzzle.test.snapshots.SnapshotDirectoryStrategy;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

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

    // tag::changeDirViaAnnotation[]
    @Test
    @SnapshotDirectory("snapshots") // <1>
    void testChangeDirectoryViaAnnotation(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot
                .assertThat(person)
                .asText()
                .matchesSnapshotText();
    }
    // end::changeDirViaAnnotation[]

    // tag::changeDirViaStrategy[]
    @Test
    @SnapshotDirectory(determinedBy = ResolveSnapshotDirectory.class) // <1>
    void testChangeDirectoryViaStrategy(Snapshot snapshot) {
        final Person person = Person.determinePerson();
        snapshot
                .assertThat(person)
                .asText()
                .matchesSnapshotText();
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
