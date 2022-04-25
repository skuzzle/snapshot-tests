package de.skuzzle.test.snapshots.data.xml;

import java.util.function.Consumer;

import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.diff.DifferenceEvaluator;

import de.skuzzle.test.snapshots.StructuralAssertions;

final class XmlUnitStructuralAssertions implements StructuralAssertions {

    private final DifferenceEvaluator differenceEvaluator;
    private final Consumer<CompareAssert> compareAssertConsumer;

    public XmlUnitStructuralAssertions(Consumer<CompareAssert> compareAssertConsumer,
            DifferenceEvaluator differenceEvaluator) {
        this.differenceEvaluator = differenceEvaluator;
        this.compareAssertConsumer = compareAssertConsumer;
    }

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        final CompareAssert compareAssert = XmlAssert
                .assertThat(serializedActual)
                .and(storedSnapshot)
                .withDifferenceEvaluator(differenceEvaluator);
        compareAssertConsumer.accept(compareAssert);
    }

}
