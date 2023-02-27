package de.skuzzle.test.snapshots.data.xml.xmlunit;

import de.skuzzle.test.snapshots.ComparisonRuleBuilder;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.validation.Arguments;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.assertj.XmlAssert;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Configurable {@link StructuralAssertions} relying on xml-unit.
 *
 * @author Simon Taddiken
 * @since 1.5.0
 */
@API(status = Status.INTERNAL, since = "1.5.0")
public final class XmlUnitStructuralAssertions implements StructuralAssertions {

    // null unless customized
    private final Consumer<ComparisonRuleBuilder> rules;
    private final Consumer<CompareAssert> compareAssertConsumer;
    private final Map<String, String> namespaceContext;
    private final XPathDebug xPathDebug;

    public XmlUnitStructuralAssertions(
            Consumer<CompareAssert> compareAssertConsumer,
            Consumer<ComparisonRuleBuilder> rules,
            Map<String, String> namespaceContext,
            XPathDebug xPathDebug) {

        this.rules = rules;
        this.compareAssertConsumer = Arguments.requireNonNull(compareAssertConsumer,
                "compareAssertConsumer must not be null");
        this.xPathDebug = Arguments.requireNonNull(xPathDebug, "xPathDebug must not be null");
        this.namespaceContext = namespaceContext;
    }

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        CompareAssert compareAssert = XmlAssert
                .assertThat(serializedActual)
                .and(storedSnapshot);
        if (rules != null) {
            final XmlUnitComparisonRuleBuilder comparisonRuleBuilder = new XmlUnitComparisonRuleBuilder(
                    namespaceContext, xPathDebug);
            rules.accept(comparisonRuleBuilder);
            compareAssert = compareAssert.withDifferenceEvaluator(comparisonRuleBuilder.build());
        }
        if (namespaceContext != null) {
            compareAssert.withNamespaceContext(namespaceContext);
        }
        try {
            compareAssertConsumer.accept(compareAssert);
        } finally {
            xPathDebug.log(
                    "%nPrevious lines were printed because XPath debugging is enabled. It has been enabled at%n%s%n%n",
                    xPathDebug.enabledAt);
        }
    }

}
