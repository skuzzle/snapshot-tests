package de.skuzzle.test.snapshots;

public interface Snapshot {

    DiffAssertions assertThat(Object actual);
}
