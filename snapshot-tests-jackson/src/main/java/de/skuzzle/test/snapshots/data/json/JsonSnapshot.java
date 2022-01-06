package de.skuzzle.test.snapshots.data.json;

import java.util.Objects;
import java.util.function.Consumer;

import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataBuilder;

/**
 * Serializes snapshots as json using jackson. Use either the static factory methods or
 * the ready-to-use static instance {@link #json}. You can either provide an explicit
 * {@link ObjectMapper} or use one with sensible defaults.
 * <p>
 * Either way, the ObjectMapper can be further configured by calling
 * {@link #configure(Consumer)}.
 *
 * @author Simon Taddiken
 */
public final class JsonSnapshot implements StructuredDataBuilder {

    /**
     * Takes Snapshots using jackson {@link ObjectMapper} and compare the results using
     * {@link JSONAssert}.
     */
    public static final StructuredData json = withDefaultObjectMapper().build();

    private final ObjectMapper objectMapper;

    private JsonSnapshot(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
    }

    /**
     * Creates an instance using a default {@link ObjectMapper} with sensible defaults.
     * The object mapper can be configured further using {@link #configure(Consumer)}.
     *
     * @return A builder for building {@link StructuredData}.
     */
    public static JsonSnapshot withDefaultObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModules(new Jdk8Module(), new JavaTimeModule());
        return new JsonSnapshot(objectMapper);
    }

    /**
     * Creates an instance using the explicitly provided ObjectMapper.
     *
     * @param objectMapper The ObjectMapper to use for taking snapshots.
     * @return A builder for building {@link StructuredData}.
     */
    public static JsonSnapshot withObjectMapper(ObjectMapper objectMapper) {
        return new JsonSnapshot(objectMapper);
    }

    /**
     * Configure the underlying ObjectMapper by passing in a {@link Consumer}.
     *
     * @param c The consumer to which the ObjectMapper will be passed.
     * @return This instance.
     */
    public JsonSnapshot configure(Consumer<ObjectMapper> c) {
        c.accept(objectMapper);
        return this;
    }

    @Override
    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = new JacksonJsonSnapshotSerializer(objectMapper);
        final StructuralAssertions structuralAssertions = new JsonAssertStructuralAssertions();
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }
}
