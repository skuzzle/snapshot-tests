package de.skuzzle.test.snapshots.data.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

final class StringXmlPrettyPrint {

    private static final TransformerFactory TRANS = TransformerFactory.newInstance();

    public static String prettyPrint(String doc) {
        try {
            final Transformer tf = createTransformer();
            // initialize StreamResult with File object to save to file
            final StreamResult result = new StreamResult(new StringWriter());
            final StreamSource source = new StreamSource(new StringReader(doc));
            tf.transform(source, result);
            return result.getWriter().toString()
                    .replace("?><", String.format("?>%n<")) // soap
                    .replace("\" xmlns:", String.format("\"%n\t\t\txmlns:"));
        } catch (final TransformerException ex) {
            return null;
        }
    }

    private static Transformer createTransformer() throws TransformerException {
        final Transformer transformer = TRANS.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        return transformer;
    }
}
