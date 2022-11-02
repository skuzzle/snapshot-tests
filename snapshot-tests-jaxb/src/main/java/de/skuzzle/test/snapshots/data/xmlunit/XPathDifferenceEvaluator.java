package de.skuzzle.test.snapshots.data.xmlunit;

import java.util.stream.StreamSupport;

import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.Comparison.Detail;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.xpath.XPathEngine;

final class XPathDifferenceEvaluator implements DifferenceEvaluator {

    private final String xPath;
    private final XPathEngine xPathEngine;
    private final DifferenceEvaluator matchedXPathDelegate;

    public XPathDifferenceEvaluator(XPathEngine xPathEngine, String xPath, DifferenceEvaluator matchedXPathDelegate) {
        this.xPath = xPath;
        this.xPathEngine = xPathEngine;
        this.matchedXPathDelegate = matchedXPathDelegate;
    }

    @Override
    public ComparisonResult evaluate(Comparison comparison, ComparisonResult outcome) {
        final Detail actualDetails = comparison.getTestDetails();
        final Node targetNode = actualDetails.getTarget();

        if (!isMatchedByXpath(targetNode, this.xPath)) {
            return outcome;
        }
        return matchedXPathDelegate.evaluate(comparison, outcome);
    }

    private boolean isMatchedByXpath(Node node, String xPath) {
        final Iterable<Node> selectedNodes = xPathEngine.selectNodes(xPath, node);
        return StreamSupport.stream(selectedNodes.spliterator(), false)
                .anyMatch(node::equals);
    }
}
