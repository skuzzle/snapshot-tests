package de.skuzzle.test.snapshots;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import de.skuzzle.test.snapshots.SnapshotFile.SnapshotHeader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nl.jqno.equalsverifier.EqualsVerifier;

public class SnapshotFileTest {

    private BufferedReader string(String s) {
        return new BufferedReader(new StringReader(s));
    }

    @ParameterizedTest
    @ValueSource(strings = { "\r", "\n", "\r\n" })
    void testCreateHeaderWithLineBreakInKey(String lineBreak) throws Exception {
        final String illegalKey = String.format("key%swith linebreak", lineBreak);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> SnapshotHeader.fromMap(Map.of(illegalKey, "value")));
    }

    @ParameterizedTest
    @ValueSource(strings = { "\r", "\n", "\r\n" })
    void testCreateHeaderWithLinebreakInValue(String lineBreak) throws Exception {
        final String illegalValue = String.format("value%swith linebreak", lineBreak);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> SnapshotHeader.fromMap(Map.of("key", illegalValue)));
    }

    @Test
    void testWriteRead() throws Exception {
        final SnapshotFile snapshotFile = SnapshotFile.of(SnapshotHeader.fromMap(Map.of("key", "value")),
                "actualSnapshot");
        assertSameAfterWriteRead(snapshotFile);
    }

    @Test
    void testWriteReadEmptyHeader() throws Exception {
        final SnapshotFile snapshotFile = SnapshotFile.of(SnapshotHeader.fromMap(Map.of()),
                "actualSnapshot");
        assertSameAfterWriteRead(snapshotFile);

    }

    @Test
    void testSnapshotStartsWithNewLines() throws Exception {
        final SnapshotFile snapshotFile = SnapshotFile.of(SnapshotHeader.fromMap(Map.of("key", "value")),
                "\nactualSnapshot");

        assertSameAfterWriteRead(snapshotFile);
    }

    private void assertSameAfterWriteRead(SnapshotFile snapshotFile) throws IOException {
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
