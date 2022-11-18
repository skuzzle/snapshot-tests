package de.skuzzle.test.snapshots.data.xmlunit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.Comparison.Detail;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.xpath.JAXPXPathEngine;

import de.skuzzle.test.snapshots.ComparisonRuleBuilder;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Builds x-path based comparison rules for xml/html comparison.
 *
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL, since = "1.5.0")
public final class XmlUnitComparisonRuleBuilder implements ComparisonRuleBuilder {

    private final boolean enableXPathDebugging;
    private final JAXPXPathEngine xpathEngine = new JAXPXPathEngine();
    private final List<DifferenceEvaluator> customizations = new ArrayList<>();

    public XmlUnitComparisonRuleBuilder(boolean enableXPathDebugging) {
        this.enableXPathDebugging = enableXPathDebugging;
    }

    @Override
    public ChooseMatcher pathAt(String path) {
        Arguments.requireNonNull(path, "path must not be null");
        return new ChooseMatcher() {

            @Override
            public ComparisonRuleBuilder mustMatch(Pattern regex) {
                Arguments.requireNonNull(regex, "regex must not be null");
                return mustMatch(actualDetails -> regex.matcher(((Node) actualDetails).getTextContent())
                        .matches());
            }

            @Override
            public ComparisonRuleBuilder ignore() {
                return mustMatch(actualDetails -> true);
            }

            @Override
            public ComparisonRuleBuilder mustMatch(Predicate<? super Object> predicate) {
                Arguments.requireNonNull(predicate, "predicate must not be null");
                customizations.add(new XPathDifferenceEvaluator(enableXPathDebugging, xpathEngine, path,
                        new DifferenceEvaluator() {

                            @Override
                            public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {
                                final Detail actualDetails = comparison.getTestDetails();
                                final Node targetNode = actualDetails.getTarget();

                                return predicate.test(targetNode)
                                        ? ComparisonResult.EQUAL
                                        : ComparisonResult.DIFFERENT;
                            }
                        }));
                return XmlUnitComparisonRuleBuilder.this;
            }
        };
    }

    /**
     * Builds a xml-unit {@link DifferenceEvaluator} which obeys the configured rules. the
     * returned instance can be used for constructing a
     * {@link XmlUnitStructuralAssertions} instance.
     *
     * @return A new {@link DifferenceEvaluator}.
     */
    public DifferenceEvaluator build() {
        return DifferenceEvaluators.chain(customizations.toArray(DifferenceEvaluator[]::new));
    }

}
