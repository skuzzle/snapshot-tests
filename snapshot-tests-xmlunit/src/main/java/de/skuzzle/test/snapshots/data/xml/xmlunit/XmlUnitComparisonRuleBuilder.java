package de.skuzzle.test.snapshots.data.xml.xmlunit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import de.skuzzle.test.snapshots.ComparisonRuleBuilder;
import de.skuzzle.test.snapshots.validation.Arguments;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.w3c.dom.Node;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.diff.DifferenceEvaluators;
import org.xmlunit.xpath.JAXPXPathEngine;

/**
 * Builds x-path based comparison rules for xml/html comparison.
 *
 * @author Simon Taddiken
 */
@API(status = Status.INTERNAL, since = "1.5.0")
public final class XmlUnitComparisonRuleBuilder implements ComparisonRuleBuilder {

    private final XPathDebug xPathDebug;
    private final JAXPXPathEngine xpathEngine = new JAXPXPathEngine();
    private final List<DifferenceEvaluator> customizations = new ArrayList<>();

    /**
     * Creates a new rule builder.
     *
     * @param namespaceContext Nullable namespace context
     * @param xPathDebug Whether to print debug messages about matched nodes
     */
    public XmlUnitComparisonRuleBuilder(Map<String, String> namespaceContext, XPathDebug xPathDebug) {
        this.xPathDebug = Arguments.requireNonNull(xPathDebug);
        if (namespaceContext != null) {
            this.xpathEngine.setNamespaceContext(namespaceContext);
        }
    }

    @Override
    public ChooseMatcher pathAt(String path) {
        Arguments.requireNonNull(path, "path must not be null");
        return new ChooseMatcher() {

            @Override
            public ComparisonRuleBuilder mustMatch(Pattern regex) {
                Arguments.requireNonNull(regex, "regex must not be null");
                return mustMatchWithInfo(actualDetails -> regex.matcher(((Node) actualDetails).getTextContent())
                        .matches(), "must match pattern: " + regex, CustomRuleType.CUSTOM_MATCH);
            }

            @Override
            public ComparisonRuleBuilder ignore() {
                return mustMatchWithInfo(actualDetails -> true, "will be ignored", CustomRuleType.IGNORE);
            }

            @Override
            public ComparisonRuleBuilder mustMatch(Predicate<? super Object> predicate) {
                Arguments.requireNonNull(predicate, "predicate must not be null");
                return mustMatchWithInfo(predicate, "must match custom predicate: " + predicate,
                        CustomRuleType.CUSTOM_MATCH);
            }

            private ComparisonRuleBuilder mustMatchWithInfo(Predicate<? super Object> predicate, String info,
                    CustomRuleType ruleType) {
                Arguments.requireNonNull(predicate, "predicate must not be null");
                customizations.add(new XPathDifferenceEvaluator(ruleType, info,
                        xPathDebug, xpathEngine, path, predicate));
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
        final List<DifferenceEvaluator> differenceEvaluators = new ArrayList<>();
        differenceEvaluators.add(DifferenceEvaluators.Default);
        differenceEvaluators.addAll(customizations);
        return DifferenceEvaluators.chain(differenceEvaluators.toArray(DifferenceEvaluator[]::new));
    }

}
