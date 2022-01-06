package de.skuzzle.test.snapshots.data.xml;

import java.io.StringWriter;
import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.assertj.core.annotations.Nullable;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot.MarshallerSupplier;

final class JaxbXmlSnapshotSerializer implements SnapshotSerializer {

    @Nullable
    private final JAXBContext jaxb;
    private final MarshallerSupplier marshallerSupplier;

    private JaxbXmlSnapshotSerializer(JAXBContext jaxb, MarshallerSupplier marshallerSupplier) {
        this.jaxb = jaxb;
        this.marshallerSupplier = Objects.requireNonNull(marshallerSupplier);
    }

    public static SnapshotSerializer withExplicitJaxbContext(
            @Nullable JAXBContext jaxb,
            MarshallerSupplier marshallerSupplier) {
        return new JaxbXmlSnapshotSerializer(jaxb, marshallerSupplier);
    }

    public static JaxbXmlSnapshotSerializer withAutomaticJaxbContext(MarshallerSupplier marshallerSupplier) {
        return new JaxbXmlSnapshotSerializer(null, marshallerSupplier);
    }

    private JAXBContext inferJaxbContext(Object object) {
        return jaxb == null
                ? CachedJAXBContexts.getOrCreateContext(object)
                : jaxb;
    }

    @Override
    public String serialize(Object testResult) throws SnapshotException {
        try {
            final JAXBContext jaxbContext = inferJaxbContext(testResult);
            final StringWriter writer = new StringWriter();
            final Marshaller marshaller = marshallerSupplier.createMarshaller(jaxbContext);
            marshaller.marshal(testResult, writer);
            return writer.toString();
        } catch (final JAXBException e) {
            throw new SnapshotException("Error serializing object to XML: " + testResult, e);
        }
    }

}
