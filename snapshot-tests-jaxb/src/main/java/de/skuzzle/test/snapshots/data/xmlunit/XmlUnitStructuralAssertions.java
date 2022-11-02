package de.skuzzle.test.snapshots.data.xmlunit;

import java.util.function.Consumer;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.diff.DifferenceEvaluator;

import de.skuzzle.test.snapshots.StructuralAssertions;

@API(status = Status.INTERNAL, since = "1.5.0")
public final class XmlUnitStructuralAssertions implements StructuralAssertions {

    private final DifferenceEvaluator differenceEvaluator;
    private final Consumer<CompareAssert> compareAssertConsumer;

    public XmlUnitStructuralAssertions(Consumer<CompareAssert> compareAssertConsumer,
            DifferenceEvaluator differenceEvaluator) {
        this.differenceEvaluator = differenceEvaluator;
        this.compareAssertConsumer = compareAssertConsumer;
    }

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        CompareAssert compareAssert = XmlAssert
                .assertThat(serializedActual)
                .and(storedSnapshot);
        if (differenceEvaluator != null) {
            compareAssert = compareAssert.withDifferenceEvaluator(differenceEvaluator);
        }
        compareAssertConsumer.accept(compareAssert);
    }

}
