package de.skuzzle.test.snapshots.data.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotSerializer;

final class HtmlSnapshotSerializer implements SnapshotSerializer {

    private final boolean prettyPrintSnapshot;

    public HtmlSnapshotSerializer(boolean prettyPrintSnapshot) {
        this.prettyPrintSnapshot = prettyPrintSnapshot;
    }

    @Override
    public String serialize(Object testResult) throws SnapshotException {
        final String body = testResult.toString();
        if (prettyPrintSnapshot) {
            final Document document = Jsoup.parse(body);
            document.outputSettings().prettyPrint(true).indentAmount(4);
            return document.toString();
        }
        return body;
    }

}
