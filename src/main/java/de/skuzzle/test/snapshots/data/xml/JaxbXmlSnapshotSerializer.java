package de.skuzzle.test.snapshots.data.xml;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.skuzzle.test.snapshots.data.SnapshotException;
import de.skuzzle.test.snapshots.data.SnapshotSerializer;
import de.skuzzle.test.snapshots.data.xml.JaxbStructuredData.MarshallerSupplier;

class JaxbXmlSnapshotSerializer implements SnapshotSerializer {

    private final JAXBContext jaxb;
    private final MarshallerSupplier marshallerSupplier;

    public JaxbXmlSnapshotSerializer(JAXBContext jaxb, MarshallerSupplier marshallerSupplier) {
        this.jaxb = jaxb;
        this.marshallerSupplier = marshallerSupplier;
    }

    @Override
    public String serialize(Object testResult) throws SnapshotException {
        try {
            final StringWriter writer = new StringWriter();
            final Marshaller marshaller = marshallerSupplier.createMarshaller(jaxb);
            marshaller.marshal(testResult, writer);
            return writer.toString();
        } catch (final JAXBException e) {
            throw new SnapshotException("Error serializing object to XML: " + testResult, e);
        }
    }

}
