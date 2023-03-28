package de.skuzzle.test.snapshots.data.xml.xmlunit;

import java.util.HashSet;
import java.util.Set;

import de.skuzzle.test.snapshots.validation.Arguments;

import org.w3c.dom.Node;
import org.xmlunit.xpath.XPathEngine;

/**
 * Collects and caches the matches of a single XPath within a single documents for fast
 * access.
 */
final class CachedXPath {

    private final String xPath;
    private final XPathEngine xPathEngine;
    private final XPathDebug xPathDebug;

    // Lazy initialized during first call do isMatchedBy...
    private Node rootNode;
    private Set<Node> matchedNodes;

    CachedXPath(String xPath, XPathEngine xPathEngine, XPathDebug xPathDebug) {
        this.xPath = xPath;
        this.xPathEngine = xPathEngine;
        this.xPathDebug = xPathDebug;
    }

    /**
     * Tests whether the given node is matched by the XPath.
     * <p>
     * Lazily executes the XPath on the first call to this method.
     * </p>
     *
     * @param node The node to test.
     * @return Whether the node is matched.
     */
    boolean isMatched(Node node) {
        final Node rootNode = getRootNode(node);
        if (this.matchedNodes == null) {
            this.rootNode = rootNode;
            this.matchedNodes = executeXPath(node, xPath, xPathEngine, xPathDebug);
        }
        Arguments.check(this.rootNode.equals(rootNode),
                "Found that CachedXPath has been applied to two different documents");

        return this.matchedNodes.contains(node);
    }

    private Set<Node> executeXPath(Node node, String xPath, XPathEngine xPathEngine, XPathDebug xPathDebug) {
        final Node root = getRootNode(node);
        final Iterable<Node> selectedNodes = xPathEngine.selectNodes(xPath, root);
        final Set<Node> nodes = new HashSet<>();
        selectedNodes.forEach(nodes::add);

        if (xPathDebug.enabled) {
            xPathDebug.log("XPath '%s' matched:", xPath);
            if (nodes.isEmpty()) {
                xPathDebug.log("    <none>");
            }
            nodes.stream().map(Node::toString).map(s -> "    " + s).forEach(xPathDebug::log);
        }
        return nodes;
    }

    private static Node getRootNode(Node node) {
        return node.getOwnerDocument() == null ? node : node.getOwnerDocument();
    }
}
