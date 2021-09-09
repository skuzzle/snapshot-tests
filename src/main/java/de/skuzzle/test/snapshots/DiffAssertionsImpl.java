package de.skuzzle.test.snapshots;

import javax.xml.bind.JAXBContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class DiffAssertionsImpl implements DiffAssertions {

    private final SnapshotImpl snapshot;
    private final Object actual;

    public DiffAssertionsImpl(SnapshotImpl snapshot, Object actual) {
        this.snapshot = snapshot;
        this.actual = actual;
    }

    @Override
    public SerializedDiffAssertions asXml() {
        final JAXBContext jaxbContext = CachedJAXBContexts.getOrCreateContext(actual);
        return asXmlUsing(jaxbContext);
    }

    public SerializedDiffAssertions asXmlUsing(JAXBContext context) {
        return new XmlDiffAssertions(snapshot, actual, context);
    }

    @Override
    public SerializedDiffAssertions asJson() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModules(new Jdk8Module(), new JavaTimeModule());
        return new JsonDiffAssertions(snapshot, actual, objectMapper);
    }

}
