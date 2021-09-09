package de.skuzzle.test.snapshots.data.xml;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import de.skuzzle.test.snapshots.data.SnapshotSerializer;

public class JaxbXmlSnapshotSerializer implements SnapshotSerializer {

    private final JAXBContext jaxb;

    public JaxbXmlSnapshotSerializer(JAXBContext jaxb) {
        this.jaxb = jaxb;
    }

    @Override
    public String serialize(Object testResult) throws Exception {
        try {
            final StringWriter writer = new StringWriter();
            final Marshaller marshaller = jaxb.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(testResult, writer);
            return writer.toString();
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
