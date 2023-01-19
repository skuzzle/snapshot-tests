package de.skuzzle.test.snapshots.data.xml.xmlunit;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.Comparison.Detail;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.xpath.XPathEngine;

final class XPathDifferenceEvaluator implements DifferenceEvaluator {

    private final XPathDebug xPathDebug;
    private final String xPath;
    private final XPathEngine xPathEngine;
    private final DifferenceEvaluator matchedXPathDelegate;
    private Set<Node> matchedNodes;
    private final String info;

    XPathDifferenceEvaluator(String info, XPathDebug xPathDebug, XPathEngine xPathEngine, String xPath,
            DifferenceEvaluator matchedXPathDelegate) {
        this.info = info;
        this.xPathDebug = xPathDebug;
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

        final ComparisonResult comparisonResult = matchedXPathDelegate.evaluate(comparison, outcome);

        xPathDebug.log("Applying custom comparison rule to node at %s: %s %s. Result: %s",
                actualDetails.getXPath(), targetNode, info, comparisonResult);

        return comparisonResult;
    }

    private boolean isMatchedByXpath(Node node, String xPath) {
        if (this.matchedNodes == null) {
            final Node root = node.getOwnerDocument() == null ? node : node.getOwnerDocument();
            final Iterable<Node> selectedNodes = xPathEngine.selectNodes(xPath, root);
            final Set<Node> nodes = new HashSet<>();
            selectedNodes.forEach(nodes::add);
            this.matchedNodes = nodes;

            if (xPathDebug.enabled) {
                xPathDebug.log("XPath '%s' matched:", xPath);
                if (matchedNodes.isEmpty()) {
                    xPathDebug.log("    <none>");
                }
                matchedNodes.stream().map(Node::toString).map(s -> "    " + s).forEach(xPathDebug::log);
            }
        }

        return this.matchedNodes.contains(node);
    }

}
