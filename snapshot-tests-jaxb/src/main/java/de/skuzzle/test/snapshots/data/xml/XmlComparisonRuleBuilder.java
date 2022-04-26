package de.skuzzle.test.snapshots.data.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.Comparison.Detail;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.xpath.JAXPXPathEngine;

import de.skuzzle.test.snapshots.ComparisonRuleBuilder;
import de.skuzzle.test.snapshots.validation.Arguments;

final class XmlComparisonRuleBuilder implements ComparisonRuleBuilder {

    private final JAXPXPathEngine xpathEngine = new JAXPXPathEngine();
    private final List<DifferenceEvaluator> customizations = new ArrayList<>();

    @Override
    public ChooseMatcher pathAt(String path) {

        return new ChooseMatcher() {

            @Override
            public ComparisonRuleBuilder mustMatch(Pattern regex) {
                Arguments.requireNonNull(regex, "regex must not be null");
                return mustMatch(actualDetails -> regex.matcher(((Detail) actualDetails).getTarget().getTextContent())
                        .matches());
            }

            @Override
            public ComparisonRuleBuilder ignore() {
                return mustMatch(actualDetails -> true);
            }

            @Override
            public ComparisonRuleBuilder mustMatch(Predicate<? super Object> predicate) {
                Arguments.requireNonNull(predicate, "predicate must not be null");
                customizations.add(new XPathDifferenceEvaluator(xpathEngine, path, new DifferenceEvaluator() {

                    @Override
                    public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {
                        final Detail actualDetails = comparison.getTestDetails();

                        return predicate.test(actualDetails)
                                ? ComparisonResult.EQUAL
                                : ComparisonResult.DIFFERENT;
                    }
                }));
                return XmlComparisonRuleBuilder.this;
            }
        };
    }

    public DifferenceEvaluator build() {
        return DifferenceEvaluators.chain(customizations.toArray(DifferenceEvaluator[]::new));
    }

}
