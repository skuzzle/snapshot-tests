package de.skuzzle.test.snapshots;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;
import nl.jqno.equalsverifier.EqualsVerifier;

public class SnapshotFileTest {

    private BufferedReader string(String s) {
        return new BufferedReader(new StringReader(s));
    }

    @Test
    void testWriteRead() throws Exception {
        final SnapshotFile snapshotFile = SnapshotFile.of(SnapshotHeader.fromMap(Map.of("key", "value")),
                "actualSnapshot");
        final StringWriter writer = new StringWriter();
        snapshotFile.writeTo(writer);

        final SnapshotFile snapshotFile2 = SnapshotFile.readFrom(string(writer.toString()));
        assertThat(snapshotFile).isEqualTo(snapshotFile2);
    }

    @Test
    void testWriteReadEmptyHeader() throws Exception {
        final SnapshotFile snapshotFile = SnapshotFile.of(SnapshotHeader.fromMap(Map.of()),
                "actualSnapshot");
        final StringWriter writer = new StringWriter();
        snapshotFile.writeTo(writer);

        final SnapshotFile snapshotFile2 = SnapshotFile.readFrom(string(writer.toString()));
        assertThat(snapshotFile).isEqualTo(snapshotFile2);
    }

    @Test
    void testSnapshotFileEquals() throws Exception {
        EqualsVerifier.forClass(SnapshotFile.class).verify();
    }

    @Test
    void testHeaderEquals() throws Exception {
        EqualsVerifier.forClass(SnapshotHeader.class).verify();
    }

    @Test
    void testReadSnapshotFile() throws Exception {
        final SnapshotFile snapshotFile = SnapshotFile.readFrom(string("key1:value1\n\nactualSnapshot"));
        assertThat(snapshotFile.header()).isEqualTo(SnapshotHeader.fromMap(Map.of("key1", "value1")));
        assertThat(snapshotFile.snapshot()).isEqualTo("actualSnapshot");
    }

    @Test
    void testReadHeaderNoNewLineAtTheEnd() throws Exception {
        final SnapshotHeader fromReader = SnapshotHeader.readFrom(string("key1: value1\nkey2:value2:with:colons"));
        assertThat(fromReader)
                .isEqualTo(SnapshotHeader.fromMap(Map.of("key1", "value1", "key2", "value2:with:colons")));
    }

    @Test
    void testReadHeaderUntilBlankLine() throws Exception {
        final SnapshotHeader fromReader = SnapshotHeader
                .readFrom(string("key1: value1\nkey2:value2:with:colons\n\nkey3:should be ignored"));
        assertThat(fromReader)
                .isEqualTo(SnapshotHeader.fromMap(Map.of("key1", "value1", "key2", "value2:with:colons")));
    }
}
