package de.skuzzle.test.snapshots;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.xmlunit.assertj.XmlAssert;

class XmlDiffAssertions extends AbstractSerializedDiffAssertions {

    private final JAXBContext jaxb;

    public XmlDiffAssertions(SnapshotImpl snapshot, Object actual,
            JAXBContext jaxb) {
        super(snapshot, actual);
        this.jaxb = jaxb;
    }

    @Override
    protected void compareToSnapshot(String storedSnapshot, String serializedActual) throws Exception {
        XmlAssert.assertThat(serializedActual).and(storedSnapshot).areIdentical();
    }

    @Override
    protected String serializeToString(Object object) {
        try {
            final StringWriter writer = new StringWriter();
            final Marshaller marshaller = jaxb.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, writer);
            return writer.toString();
        } catch (final JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
