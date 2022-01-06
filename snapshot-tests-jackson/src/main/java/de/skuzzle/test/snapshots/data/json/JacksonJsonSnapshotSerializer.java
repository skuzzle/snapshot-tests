package de.skuzzle.test.snapshots.data.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotSerializer;

final class JacksonJsonSnapshotSerializer implements SnapshotSerializer {

    private final ObjectMapper objectMapper;

    public JacksonJsonSnapshotSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(Object testResult) throws SnapshotException {
        try {
            return objectMapper.writeValueAsString(testResult);
        } catch (final JsonProcessingException e) {
            throw new SnapshotException("Error serializing object to json: " + testResult, e);
        }
    }

}
