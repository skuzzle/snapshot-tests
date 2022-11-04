package de.skuzzle.test.snapshots.data.html;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.xmlunit.assertj.XmlAssert;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.StructuralAssertions;

final class HtmlStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, SnapshotException {
        final W3CDom w3cDom = new W3CDom();
        final Document parsedSnapshot = Jsoup.parse(storedSnapshot);
        final Document parsedActual = Jsoup.parse(serializedActual);

        XmlAssert.assertThat(w3cDom.fromJsoup(parsedSnapshot))
                .and(w3cDom.fromJsoup(parsedActual))
                .areIdentical();
    }

}
