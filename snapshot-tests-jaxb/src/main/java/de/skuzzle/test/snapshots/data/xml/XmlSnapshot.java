package de.skuzzle.test.snapshots.data.xml;

import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.assertj.core.annotations.Nullable;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;

public final class XmlSnapshot {

    @Nullable
    private final JAXBContext jaxbContext;
    private MarshallerSupplier marshallerSupplier;

    private XmlSnapshot(JAXBContext jaxbContext) {
        this.jaxbContext = jaxbContext;
        this.marshallerSupplier = ctx -> {
            final Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            return marshaller;
        };
    }

    public static XmlSnapshot inferJaxbContext() {
        return new XmlSnapshot(null);
    }

    public static XmlSnapshot with(JAXBContext jaxbContext) {
        return new XmlSnapshot(jaxbContext);
    }

    public XmlSnapshot withMarshaller(MarshallerSupplier marshallerSupplier) {
        this.marshallerSupplier = Objects.requireNonNull(marshallerSupplier, "marshallerSupplier must not be null");
        return this;
    }

    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = JaxbXmlSnapshotSerializer.withExplicitJaxbContext(
                jaxbContext, marshallerSupplier);
        final StructuralAssertions structuralAssertions = new XmlUnitStructuralAssertions();
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }

    static interface MarshallerSupplier {

        Marshaller createMarshaller(JAXBContext jaxbContext) throws JAXBException;
    }
}
