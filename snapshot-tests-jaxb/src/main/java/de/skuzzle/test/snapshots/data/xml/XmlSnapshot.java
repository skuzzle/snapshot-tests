package de.skuzzle.test.snapshots.data.xml;

import java.util.Objects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.assertj.core.annotations.Nullable;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataBuilder;

/**
 * {@link StructuredData} builder for serializing test results to XML, relying on JAXB.
 *
 * @author Simon Taddiken
 */
public final class XmlSnapshot implements StructuredDataBuilder {

    /**
     * Simple default {@link StructuredData} instance which infers the JAXB context from a
     * test's actual result object.
     */
    public static final StructuredData xml = inferJaxbContext().build();

    // If left null, the JAXBContext will be inferred from the actual test result.
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

    @Override
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
