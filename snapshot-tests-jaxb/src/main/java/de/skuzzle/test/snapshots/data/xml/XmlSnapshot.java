package de.skuzzle.test.snapshots.data.xml;

import java.util.Objects;
import java.util.function.Consumer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.assertj.core.annotations.Nullable;
import org.xmlunit.assertj.CompareAssert;

import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;

/**
 * {@link StructuredData} builder for serializing test results to XML, relying on JAXB and
 * XML-Unit.
 * <p>
 * You can either use a pre-configured default instance via {@link #xml} or use any of the
 * static factory methods to customize the construction.
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public final class XmlSnapshot implements StructuredDataProvider {

    /**
     * Simple default {@link StructuredData} instance which infers the JAXB context from a
     * test's actual result object.
     * <p>
     * If you need control over how the {@link JAXBContext} and the {@link Marshaller} are
     * being set up, use the static factory methods in {@link XmlSnapshot} instead of this
     * static constant.
     */
    public static final StructuredDataProvider xml = inferJaxbContext().build();

    // If left null, the JAXBContext will be inferred from the actual test result.
    @Nullable
    private final JAXBContext jaxbContext;
    // Creates the Marshaller from the JAXBContext
    private MarshallerSupplier marshallerSupplier;
    // Defines how snapshots are being asserted on using xml-unit
    private Consumer<CompareAssert> compareAssertConsumer = CompareAssert::areIdentical;

    private XmlSnapshot(JAXBContext jaxbContext) {
        this.jaxbContext = jaxbContext;
        this.marshallerSupplier = ctx -> {
            final Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            return marshaller;
        };
    }

    /**
     * Tries to infer the JAXBContext from the passed in actual test result.
     *
     * @return A builder for building {@link StructuredData}.
     */
    public static XmlSnapshot inferJaxbContext() {
        return new XmlSnapshot(null);
    }

    /**
     * Uses the given JAXBContext as entry point for serializing snapshots.
     *
     * @param jaxbContext The JAXBContext to use.
     * @return A builder for building {@link StructuredData}.
     */
    public static XmlSnapshot with(JAXBContext jaxbContext) {
        return new XmlSnapshot(Objects.requireNonNull(jaxbContext, "jaxbContext must not be null"));
    }

    /**
     * Supplies the {@link Marshaller} which will be used to serialize the snapshot to
     * xml.
     *
     * @param marshallerSupplier The supplier.
     * @return This builder instance.
     */
    public XmlSnapshot withMarshaller(MarshallerSupplier marshallerSupplier) {
        this.marshallerSupplier = Objects.requireNonNull(marshallerSupplier, "marshallerSupplier must not be null");
        return this;
    }

    /**
     * Defines which Xml-Assert assertion method will actually be used. Defaults to
     * {@link CompareAssert#areIdentical()}.
     *
     * @param xmls Consumes the {@link CompareAssert} which compares the actual and
     *            expected xml.
     * @return This builder instance.
     */
    @API(status = Status.EXPERIMENTAL)
    public XmlSnapshot compareUsing(Consumer<CompareAssert> xmls) {
        this.compareAssertConsumer = Objects.requireNonNull(xmls, "CompareAssert consumer must not be null");
        return this;
    }

    @Override
    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = JaxbXmlSnapshotSerializer.withExplicitJaxbContext(
                jaxbContext, marshallerSupplier);
        final StructuralAssertions structuralAssertions = new XmlUnitStructuralAssertions(compareAssertConsumer);
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }

    @FunctionalInterface
    static interface MarshallerSupplier {

        Marshaller createMarshaller(JAXBContext jaxbContext) throws JAXBException;
    }
}
