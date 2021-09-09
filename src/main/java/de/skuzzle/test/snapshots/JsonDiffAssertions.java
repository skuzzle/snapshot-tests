package de.skuzzle.test.snapshots;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDiffAssertions extends AbstractSerializedDiffAssertions {
    private final ObjectMapper objectMapper;

    public JsonDiffAssertions(SnapshotImpl snapshot, Object actual, ObjectMapper objectMapper) {
        super(snapshot, actual);
        this.objectMapper = objectMapper;
    }

    @Override
    protected void compareToSnapshot(String storedSnapshot, String serializedActual) throws Exception {
        JSONAssert.assertEquals(storedSnapshot, serializedActual, JSONCompareMode.STRICT);
    }

    @Override
    protected String serializeToString(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

}
