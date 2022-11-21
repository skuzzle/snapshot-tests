package de.skuzzle.test.snapshots.data.xmlunit;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;
import org.xmlunit.diff.Comparison;
import org.xmlunit.diff.Comparison.Detail;
import org.xmlunit.diff.ComparisonResult;
import org.xmlunit.diff.DifferenceEvaluator;
import org.xmlunit.xpath.XPathEngine;

final class XPathDifferenceEvaluator implements DifferenceEvaluator {

    private final boolean enableXPathDebugging;
    private final String xPath;
    private final XPathEngine xPathEngine;
    private final DifferenceEvaluator matchedXPathDelegate;
    private Set<Node> matchedNodes;

    public XPathDifferenceEvaluator(boolean enableXPathDebugging, XPathEngine xPathEngine, String xPath,
            DifferenceEvaluator matchedXPathDelegate) {
        this.enableXPathDebugging = enableXPathDebugging;
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

        if (enableXPathDebugging) {
            System.out.println("Applying custom comparison rule to node: " + targetNode);
        }
        return matchedXPathDelegate.evaluate(comparison, outcome);
    }

    private boolean isMatchedByXpath(Node node, String xPath) {
        if (this.matchedNodes == null) {
            final Node root = node.getOwnerDocument() == null ? node : node.getOwnerDocument();
            final Iterable<Node> selectedNodes = xPathEngine.selectNodes(xPath, root);
            final Set<Node> nodes = new HashSet<>();
            selectedNodes.forEach(nodes::add);
            this.matchedNodes = nodes;

            if (enableXPathDebugging) {
                System.out.printf("Xpath '%s' matched:%n", xPath);
                matchedNodes.forEach(System.out::println);
            }
        }

        return this.matchedNodes.contains(node);
    }
}
