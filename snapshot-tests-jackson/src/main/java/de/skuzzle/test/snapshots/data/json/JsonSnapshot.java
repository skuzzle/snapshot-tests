package de.skuzzle.test.snapshots.data.json;

import java.util.function.Consumer;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;
import de.skuzzle.test.snapshots.validation.Arguments;

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
@API(status = Status.STABLE)
public final class JsonSnapshot implements StructuredDataProvider {

    /**
     * Takes Snapshots using jackson {@link ObjectMapper} and compare the results using
     * {@link JSONAssert}.
     */
    public static final StructuredDataProvider json = withDefaultObjectMapper().build();

    private final ObjectMapper objectMapper;
    private JSONComparator jsonComparator;

    private JsonSnapshot(ObjectMapper objectMapper) {
        this.objectMapper = Arguments.requireNonNull(objectMapper, "objectMapper must not be null");
        this.jsonComparator = new DefaultComparator(JSONCompareMode.STRICT);
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
        Arguments.requireNonNull(c, "consumer must not be null");
        c.accept(objectMapper);
        return this;
    }

    /**
     * Sets the {@link JSONComparator} for comparing actual and expected. If not set, the
     * {@link DefaultComparator} along with {@link JSONCompareMode#STRICT} will be used.
     *
     * @param jsonComparator The comparator to use.
     * @return This instance.
     * @apiNote This method is likely going to be replaced with a wrapper API around
     *          JSONAssert types. The JSONAssert API is a bit awkward to use and I'd like
     *          to remove it as a visible dependency altogether
     */
    @API(status = Status.EXPERIMENTAL)
    public JsonSnapshot withComparator(JSONComparator jsonComparator) {
        this.jsonComparator = Arguments.requireNonNull(jsonComparator, "comparator must not be null");
        return this;
    }

    @Override
    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = new JacksonJsonSnapshotSerializer(objectMapper);
        final StructuralAssertions structuralAssertions = new JsonAssertStructuralAssertions(jsonComparator);
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }
}
