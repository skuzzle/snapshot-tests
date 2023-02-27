package de.skuzzle.test.snapshots.data.json;

import java.util.function.Consumer;

import de.skuzzle.test.snapshots.reflection.Classes;
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
 * </p>
 * <p>
 * Warning: It is discouraged to use any of this builder's methods after {@link #build()}
 * has been called. Further modifications to this builder might be reflected in any
 * objects that have been built earlier. Later API versions might enforce this suggestion
 * by throwing an exception.
 * </p>
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public final class JsonSnapshot implements StructuredDataProvider {

    @Deprecated(forRemoval = true)
    static final boolean LEGACY_WARNING_PRINTED;
    static {
        final boolean placeHolderAvailable = Classes.isClassPresent("de.skuzzle.test.snapshots.data.jsonx.PlaceHolder");
        if (!placeHolderAvailable) {
            System.err.println(
                    "WARNING: Starting from snapshot-tests version 1.10.0, you should depend on 'snapshot-tests-json' module.");
            System.err.println();
            System.err.println("To remove this warning, follow these migration steps:");
            System.err.println();
            System.err.println("- Remove direct dependency to 'snapshot-tests-jackson'");
            System.err.println("- Add direct dependency to 'snapshot-tests-json' instead");
            LEGACY_WARNING_PRINTED = true;
        } else {
            LEGACY_WARNING_PRINTED = false;
        }
    }

    /**
     * Takes Snapshots using jackson {@link ObjectMapper} and compare the results using
     * {@link JSONAssert}.
     */
    public static final StructuredDataProvider json = json().build();

    private final ObjectMapper objectMapper;
    private CompareMode compareMode = CompareMode.STRICT;
    private JsonComparisonRuleBuilder comparisonRuleBuilder = new JsonComparisonRuleBuilder();

    // legacy field, only set in deprecated #withComparator
    private JSONComparator jsonComparator;

    private JsonSnapshot(ObjectMapper objectMapper) {
        this.objectMapper = Arguments.requireNonNull(objectMapper, "objectMapper must not be null");
    }

    /**
     * Creates an instance using a default {@link ObjectMapper} with sensible defaults.
     * The object mapper can be configured further using {@link #configure(Consumer)}.
     *
     * @return A builder for building {@link StructuredData}.
     * @deprecated Since 1.4.0 - Use {@link #json()} instead.
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    @API(status = Status.DEPRECATED, since = "1.4.0")
    public static JsonSnapshot withDefaultObjectMapper() {
        return json();
    }

    /**
     * Creates an instance using a default {@link ObjectMapper} with sensible defaults.
     * The object mapper can be configured further using {@link #configure(Consumer)}.
     *
     * @return A builder for building {@link StructuredData}.
     */
    public static JsonSnapshot json() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModules(new Jdk8Module(), new JavaTimeModule());
        return json(objectMapper);
    }

    /**
     * Creates an instance using the explicitly provided ObjectMapper.
     *
     * @param objectMapper The ObjectMapper to use for taking snapshots.
     * @return A builder for building {@link StructuredData}.
     */
    public static JsonSnapshot json(ObjectMapper objectMapper) {
        return new JsonSnapshot(objectMapper);
    }

    /**
     * Creates an instance using the explicitly provided ObjectMapper.
     *
     * @param objectMapper The ObjectMapper to use for taking snapshots.
     * @return A builder for building {@link StructuredData}.
     * @deprecated Since 1.4.0 - Use {@link #json(ObjectMapper)} instead.
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    @API(status = Status.DEPRECATED, since = "1.4.0")
    public static JsonSnapshot withObjectMapper(ObjectMapper objectMapper) {
        return new JsonSnapshot(objectMapper);
    }

    /**
     * Configure the underlying ObjectMapper by passing in a {@link Consumer}. Can be used
     * to tweak the default {@link ObjectMapper}.
     * <p>
     * The passed Consumer will be called immediately to modify the object mapper in
     * place. That is either the default object mapper or the one that has been passed to
     * {@link #json(ObjectMapper)} during construction of this object.
     * </p>
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
     * <p>
     * Note: this method can not be used in combination with
     * {@link #withComparisonRules(Consumer)} as they will internally overwrite their
     * respective configuration.
     * </p>
     *
     * @param jsonComparator The comparator to use.
     * @return This instance.
     * @deprecated Since 1.2.0 - Use {@link #withComparisonRules(Consumer)} instead.
     * @apiNote This method is likely going to be replaced with a wrapper API around
     *          JSONAssert types. The JSONAssert API is a bit awkward to use and I'd like
     *          to remove it as a visible dependency altogether
     */
    @Deprecated(forRemoval = true, since = "1.2.0")
    @API(status = Status.DEPRECATED, since = "1.2.0")
    public JsonSnapshot withComparator(JSONComparator jsonComparator) {
        this.jsonComparator = Arguments.requireNonNull(jsonComparator, "comparator must not be null");
        return this;
    }

    /**
     * Sets the mode for comparing two json strings. Defaults to
     * {@link CompareMode#STRICT}.
     *
     * @param compareMode The compare mode to use.
     * @return This instance.
     */
    @API(status = Status.STABLE, since = "1.5.0")
    public JsonSnapshot withCompareMode(CompareMode compareMode) {
        this.compareMode = Arguments.requireNonNull(compareMode, "compareMode must not be null");
        return this;
    }

    /**
     * Allows to specify extra comparison rules that are applied to certain paths within
     * the json snapshots.
     * <p>
     * Note: this method can not be used in combination with
     * {@link #withComparator(JSONComparator)} as they will internally overwrite their
     * respective configuration.
     * </p>
     *
     * @param rules A consumer to which a {@link ComparisonRuleBuilder} will be passed.
     * @return This instance.
     */
    @API(status = Status.EXPERIMENTAL, since = "1.2.0")
    public JsonSnapshot withComparisonRules(Consumer<de.skuzzle.test.snapshots.ComparisonRuleBuilder> rules) {
        Arguments.requireNonNull(rules, "rules consumer must not be null");
        final JsonComparisonRuleBuilder comparatorCustomizerImpl = new JsonComparisonRuleBuilder();
        rules.accept(comparatorCustomizerImpl);
        this.comparisonRuleBuilder = comparatorCustomizerImpl;
        return this;
    }

    private JSONComparator determineComparator() {
        return this.jsonComparator == null
                ? this.comparisonRuleBuilder.build(this.compareMode.toJSONCompareMode())
                : this.jsonComparator;
    }

    @Override
    public StructuredData build() {
        final JSONComparator comparator = determineComparator();
        final SnapshotSerializer snapshotSerializer = new JacksonJsonSnapshotSerializer(objectMapper);
        final StructuralAssertions structuralAssertions = new JsonAssertStructuralAssertions(comparator);
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }
}
