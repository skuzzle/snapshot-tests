package de.skuzzle.test.snapshots.data.xml;

import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;

public final class JaxbStructuredData {

    private final JAXBContext jaxbContext;
    private MarshallerSupplier marshallerSupplier;

    private JaxbStructuredData(JAXBContext jaxbContext) {
        this.jaxbContext = Objects.requireNonNull(jaxbContext, "jaxbContext must not be null");
        this.marshallerSupplier = ctx -> {
            final Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            return marshaller;
        };
    }

    public static JaxbStructuredData inferJaxbContext(Object xmlElement) {
        return new JaxbStructuredData(CachedJAXBContexts.getOrCreateContext(xmlElement));
    }

    public static JaxbStructuredData with(JAXBContext jaxbContext) {
        return new JaxbStructuredData(jaxbContext);
    }

    public JaxbStructuredData withMarshaller(MarshallerSupplier marshallerSupplier) {
        this.marshallerSupplier = Objects.requireNonNull(marshallerSupplier, "marshallerSupplier must not be null");
        return this;
    }

    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = new JaxbXmlSnapshotSerializer(jaxbContext, marshallerSupplier);
        final StructuralAssertions structuralAssertions = new XmlUnitStructuralAssertions();
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }

    static interface MarshallerSupplier {

        Marshaller createMarshaller(JAXBContext jaxbContext) throws JAXBException;
    }
}
