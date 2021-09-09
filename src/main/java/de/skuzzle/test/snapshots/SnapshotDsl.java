package de.skuzzle.test.snapshots;

import de.skuzzle.test.snapshots.data.SnapshotSerializer;
import de.skuzzle.test.snapshots.data.StructuralAssertions;

public interface SnapshotDsl {

    public interface Snapshot {

        ChoseDataFormat assertThat(Object actual);
    }

    public interface ChoseDataFormat {

        ChoseStructure asXml();

        ChoseStructure asJson();

        ChoseAssertions as(SnapshotSerializer serializer);

    }

    public interface ChoseAssertions {

        void matchesSnapshotText() throws Exception;

        void matchesAccordingTo(StructuralAssertions structuralAssertions) throws Exception;
    }

    public interface ChoseStructure extends ChoseAssertions {

        void matchesSnapshotStructure() throws Exception;

    }
}
