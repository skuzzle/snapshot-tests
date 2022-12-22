package de.skuzzle.test.snapshots.data.html;

import java.util.function.Consumer;

import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.diff.DifferenceEvaluator;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.validation.Arguments;

final class HtmlStructuralAssertions implements StructuralAssertions {

    private final DifferenceEvaluator differenceEvaluator;
    private final Consumer<CompareAssert> compareAssertConsumer;

    public HtmlStructuralAssertions(Consumer<CompareAssert> compareAssertConsumer,
            DifferenceEvaluator differenceEvaluator) {
        this.differenceEvaluator = differenceEvaluator;
        this.compareAssertConsumer = Arguments.requireNonNull(compareAssertConsumer,
                "compareAssertConsumer must not be null");
    }

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, SnapshotException {
        final W3CDom w3cDom = new W3CDom();
        final Document parsedSnapshot = Jsoup.parse(storedSnapshot);
        final Document parsedActual = Jsoup.parse(serializedActual);

        CompareAssert compareAssert = XmlAssert
                .assertThat(w3cDom.fromJsoup(parsedSnapshot))
                .and(w3cDom.fromJsoup(parsedActual));
        if (differenceEvaluator != null) {
            compareAssert = compareAssert.withDifferenceEvaluator(differenceEvaluator);
        }

        compareAssertConsumer.accept(compareAssert);
    }

}
