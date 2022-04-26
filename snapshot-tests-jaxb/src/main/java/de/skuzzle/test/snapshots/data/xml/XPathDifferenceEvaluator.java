package de.skuzzle.test.snapshots.data.xml;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
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

        final Collection<Node> matchedNodes = matchedNodes(targetNode);
        if (!matchedNodes.contains(targetNode)) {
            return outcome;
        }
        return matchedXPathDelegate.evaluate(comparison, outcome);
    }

    private Collection<Node> matchedNodes(Node node) {
        if (node == null) {
            return Collections.emptySet();
        }
        final Iterable<Node> selectedNodes = xPathEngine.selectNodes(xPath, node);
        return StreamSupport.stream(selectedNodes.spliterator(), false)
                .collect(Collectors.toUnmodifiableSet());
    }
}
