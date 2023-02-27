package de.skuzzle.test.snapshots.data.xml.xmlunit;

import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.Comparison.Detail;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.xpath.XPathEngine;

import java.util.function.Predicate;

/**
 * DifferenceEvaluator which treats nodes differently depending on whether they were matched by an xpath expression.
 *
 * <p>
 * For performance reasons, we execute the xpath against the root document only once when required. The matched nodes
 * are then cached in a Set for fast access. This makes an instance of this class stateful and not reusable for
 * comparing different XML documents.
 * </p>
 */
final class XPathDifferenceEvaluator implements DifferenceEvaluator {

    private final XPathDebug xPathDebug;
    private final String info;
    private final CachedXPath cachedXPath;
    private final CustomRuleType ruleType;
    private final Predicate<? super Object> predicate;

    XPathDifferenceEvaluator(CustomRuleType ruleType, String info, XPathDebug xPathDebug, XPathEngine xPathEngine,
            String xPath, Predicate<? super Object> predicate) {

        this.ruleType = ruleType;
        this.info = info;
        this.xPathDebug = xPathDebug;
        this.predicate = predicate;
        this.cachedXPath = new CachedXPath(xPath, xPathEngine, xPathDebug);
    }

    @Override
    public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {
        // For IGNORE type rules, we can skip evaluation of the XPath if there is no difference.
        // In best case (whole document matches) we do not need to evaluate the XPath at all
        // https://github.com/skuzzle/snapshot-tests/issues/77
        if (ruleType == CustomRuleType.IGNORE && outcome != ComparisonResult.DIFFERENT) {
            return outcome;
        }

        final Detail actualDetails = comparison.getTestDetails();
        final Node targetNode = actualDetails.getTarget();
        if (!cachedXPath.isMatched(targetNode)) {
            return outcome;
        }

        final ComparisonResult comparisonResult = predicate.test(targetNode)
                ? ComparisonResult.EQUAL
                : ComparisonResult.DIFFERENT;

        xPathDebug.log("Applying custom comparison rule to node at %s: %s %s. Result: %s",
                actualDetails.getXPath(), targetNode, info, comparisonResult);

        return comparisonResult;
    }

}
