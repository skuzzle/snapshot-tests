package de.skuzzle.test.snapshots.data.xml;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot.MarshallerSupplier;
import de.skuzzle.test.snapshots.validation.Arguments;

final class JaxbXmlSnapshotSerializer implements SnapshotSerializer {

    private final JAXBContext jaxb;
    private final MarshallerSupplier marshallerSupplier;
    private final boolean prettyPrintXmlStrings;

    private JaxbXmlSnapshotSerializer(JAXBContext jaxb, MarshallerSupplier marshallerSupplier,
            boolean prettyPrintXmlStrings) {
        this.jaxb = jaxb;
        this.marshallerSupplier = Arguments.requireNonNull(marshallerSupplier);
        this.prettyPrintXmlStrings = prettyPrintXmlStrings;
    }

    public static SnapshotSerializer withExplicitJaxbContext(
            JAXBContext jaxb,
            MarshallerSupplier marshallerSupplier, boolean prettyPrintXmlStrings) {
        return new JaxbXmlSnapshotSerializer(jaxb, marshallerSupplier, prettyPrintXmlStrings);
    }

    private JAXBContext inferJaxbContext(Object object) {
        return jaxb == null
                ? CachedJAXBContexts.getOrCreateContext(object)
                : jaxb;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Object wrapIntoRootObject(Object testResult) {
        if (testResult.getClass().isAnnotationPresent(XmlRootElement.class) || testResult instanceof JAXBElement<?>) {
            return testResult;
        }

        return new JAXBElement<>(
                new QName(testResult.getClass().getSimpleName()),
                (Class) testResult.getClass(),
                testResult);
    }

    @Override
    public String serialize(Object testResult) throws SnapshotException {
        try {
            if (testResult instanceof String) {
                if (prettyPrintXmlStrings) {
                    return StringXmlPrettyPrint.prettyPrint(testResult.toString());
                }
                return testResult.toString();
            }

            final JAXBContext jaxbContext = inferJaxbContext(testResult);
            final StringWriter writer = new StringWriter();
            final Marshaller marshaller = marshallerSupplier.createMarshaller(jaxbContext);

            final Object wrapped = wrapIntoRootObject(testResult);
            marshaller.marshal(wrapped, writer);
            return writer.toString();
        } catch (final JAXBException e) {
            throw new SnapshotException("Error serializing object to XML: " + testResult, e);
        }
    }

}
