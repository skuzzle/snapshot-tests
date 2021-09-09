package de.skuzzle.test.snapshots.data.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.skuzzle.test.snapshots.data.SnapshotSerializer;

class JacksonJsonSnapshotSerializer implements SnapshotSerializer {

    private final ObjectMapper objectMapper;

    public JacksonJsonSnapshotSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String serialize(Object testResult) throws Exception {
        return objectMapper.writeValueAsString(testResult);
    }

}
