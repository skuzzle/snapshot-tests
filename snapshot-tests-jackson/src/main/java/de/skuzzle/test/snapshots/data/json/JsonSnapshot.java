package de.skuzzle.test.snapshots.data.json;

import java.util.Objects;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;

public final class JsonSnapshot {

    private final ObjectMapper objectMapper;

    private JsonSnapshot(ObjectMapper objectMapper) {
        Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        this.objectMapper = objectMapper;
    }

    public static JsonSnapshot withDefaultObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModules(new Jdk8Module(), new JavaTimeModule());
        return new JsonSnapshot(objectMapper);
    }

    public static JsonSnapshot withObjectMapper(ObjectMapper objectMapper) {
        return new JsonSnapshot(objectMapper);
    }

    public JsonSnapshot configure(Consumer<ObjectMapper> c) {
        c.accept(objectMapper);
        return this;
    }

    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = new JacksonJsonSnapshotSerializer(objectMapper);
        final StructuralAssertions structuralAssertions = new JsonAssertStructuralAssertions();
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }
}
