package de.skuzzle.test.snapshots.data.html;

import java.util.function.Consumer;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.diff.DifferenceEvaluator;

import de.skuzzle.test.snapshots.ComparisonRuleBuilder;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;
import de.skuzzle.test.snapshots.data.xmlunit.XmlUnitComparisonRuleBuilder;
import de.skuzzle.test.snapshots.data.xmlunit.XmlUnitStructuralAssertions;
import de.skuzzle.test.snapshots.validation.Arguments;

@API(status = Status.EXPERIMENTAL, since = "1.5.0")
public final class HtmlSnapshot implements StructuredDataProvider {

    public static final StructuredDataProvider html = html().build();

    // Defines how snapshots are being asserted on using xml-unit
    private Consumer<CompareAssert> compareAssertConsumer = CompareAssert::areIdentical;
    // null unless customized
    private DifferenceEvaluator differenceEvaluator;

    public static HtmlSnapshot html() {
        return new HtmlSnapshot();
    }

    private HtmlSnapshot() {
        // hidden
    }

    public HtmlSnapshot compareUsing(Consumer<CompareAssert> xmls) {
        this.compareAssertConsumer = Arguments.requireNonNull(xmls, "CompareAssert consumer must not be null");
        return this;
    }

    public HtmlSnapshot withComparisonRules(Consumer<ComparisonRuleBuilder> rules) {
        Arguments.requireNonNull(rules, "rules consumer must not be null");
        final XmlUnitComparisonRuleBuilder comparatorCustomizerImpl = new XmlUnitComparisonRuleBuilder();
        rules.accept(comparatorCustomizerImpl);
        this.differenceEvaluator = comparatorCustomizerImpl.build();
        return this;
    }

    @Override
    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = Object::toString;
        final StructuralAssertions structuralAssertions = new XmlUnitStructuralAssertions(compareAssertConsumer,
                differenceEvaluator);
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }

}
