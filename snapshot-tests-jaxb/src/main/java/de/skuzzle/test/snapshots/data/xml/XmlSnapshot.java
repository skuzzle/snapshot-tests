package de.skuzzle.test.snapshots.data.xml;

import java.util.Map;
import java.util.function.Consumer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.diff.DifferenceEvaluator;

import de.skuzzle.test.snapshots.ComparisonRuleBuilder;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;
import de.skuzzle.test.snapshots.data.xml.xmlunit.XPathDebug;
import de.skuzzle.test.snapshots.data.xml.xmlunit.XmlUnitStructuralAssertions;
import de.skuzzle.test.snapshots.reflection.Classes;
import de.skuzzle.test.snapshots.reflection.StackTraces;
import de.skuzzle.test.snapshots.validation.Arguments;
import de.skuzzle.test.snapshots.validation.State;

/**
 * {@link StructuredData} builder for serializing test results to XML, relying on JAXB and
 * XML-Unit.
 * <p>
 * You can either use a pre-configured default instance via {@link #xml} or use any of the
 * static factory methods to customize the construction.
 *
 * @author Simon Taddiken
 */
@API(status = Status.STABLE)
public final class XmlSnapshot implements StructuredDataProvider {

    @Deprecated(forRemoval = true)
    static final boolean LEGACY_WARNING_PRINTED;
    static {
        final boolean placeHolderAvailable = Classes.isClassPresent("de.skuzzle.test.snapshots.data.xmlx.PlaceHolder");
        if (!placeHolderAvailable) {
            System.err.println(
                    "DEPRECATION WARNING: Starting from snapshot-tests version 1.10.0, you should depend on 'snapshot-tests-xml-legacy' module.");
            System.err.println();
            System.err.println("To remove this warning, follow these migration steps:");
            System.err.println();
            System.err.println("- Remove direct dependency to 'snapshot-tests-jaxb'");
            System.err.println("- Add direct dependency to 'snapshot-tests-xml-legacy' instead");
            LEGACY_WARNING_PRINTED = true;
        } else {
            LEGACY_WARNING_PRINTED = false;
        }
    }

    /**
     * Simple default {@link StructuredData} instance which infers the JAXB context from a
     * test's actual result object.
     * <p>
     * If you need control over how the {@link JAXBContext} and the {@link Marshaller} are
     * being set up, use the static factory methods in {@link XmlSnapshot} instead of this
     * static constant.
     *
     * @see #xml()
     */
    public static final StructuredDataProvider xml = xml().build();

    // If left null, the JAXBContext will be inferred from the actual test result.
    private JAXBContext jaxbContext;
    // Creates the Marshaller from the JAXBContext
    private MarshallerSupplier marshallerSupplier;
    // Defines how snapshots are being asserted on using xml-unit
    private Consumer<CompareAssert> compareAssertConsumer = CompareAssert::areIdentical;
    // null unless customized
    private Consumer<ComparisonRuleBuilder> rules = null;
    // used only when actual test result is already a string
    private boolean prettyPrintStringXml = true;
    // Allows to print debug information for custom rule xpaths
    private XPathDebug xPathDebug = XPathDebug.disabled();
    // namespaces to be used in XPath expressions when using custom rules
    private Map<String, String> namespaceContext = null;

    private XmlSnapshot() {
        this.marshallerSupplier = ctx -> {
            final Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            return marshaller;
        };
    }

    /**
     * Tries to infer the JAXBContext from the passed in actual test result.
     *
     * @return A builder for building {@link StructuredData}.
     * @deprecated Since 1.4.0 - Use {@link #xml()} instead.
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    @API(status = Status.DEPRECATED, since = "1.4.0")
    public static XmlSnapshot inferJaxbContext() {
        return xml();
    }

    /**
     * Creates a new XML {@link StructuredDataProvider} which will try to infer the
     * {@link JAXBContext} from the actual test result.
     *
     * @return A builder for building {@link StructuredData}.
     * @since 1.4.0
     */
    @API(status = Status.STABLE, since = "1.4.0")
    public static XmlSnapshot xml() {
        return new XmlSnapshot();
    }

    /**
     * Uses the given JAXBContext as entry point for serializing snapshots.
     *
     * @param jaxbContext The JAXBContext to use.
     * @return A builder for building {@link StructuredData}.
     * @deprecated Since 1.4.0 - Use {@link #withJAXBContext(JAXBContext)} instead.
     */
    @Deprecated(since = "1.4.0", forRemoval = true)
    @API(status = Status.DEPRECATED, since = "1.4.0")
    public static XmlSnapshot with(JAXBContext jaxbContext) {
        return xml().withJAXBContext(jaxbContext);
    }

    /**
     * Uses the given {@link JAXBContext} instead of trying to infer it from the test
     * result.
     *
     * @param jaxbContext The JAXBContext to use.
     * @return This builder instance.
     */
    public XmlSnapshot withJAXBContext(JAXBContext jaxbContext) {
        this.jaxbContext = Arguments.requireNonNull(jaxbContext, "jaxbContext must not be null");
        return this;
    }

    /**
     * Supplies the {@link Marshaller} which will be used to serialize the snapshot to
     * xml.
     *
     * @param marshallerSupplier The supplier.
     * @return This builder instance.
     */
    public XmlSnapshot withMarshaller(MarshallerSupplier marshallerSupplier) {
        this.marshallerSupplier = Arguments.requireNonNull(marshallerSupplier, "marshallerSupplier must not be null");
        return this;
    }

    /**
     * Only taken into account if you directly pass a String into the snapshot test which
     * is already a XML is does not need to be serialized. In this case, you can advise
     * the framework to pretty print the passed in string before persisting it as a
     * snapshot.
     * <p>
     * For non-xml input (java classes that need to be serialized), pretty printing can be
     * controlled via cusomization of the marshaller using
     * {@link #withMarshaller(MarshallerSupplier)}.
     * <p>
     * Defaults to true.
     *
     * @param prettyPrintStringXml Whether to pretty print XML strings.
     * @return This build instance.
     * @since 1.6.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.6.0")
    public XmlSnapshot withPrettyPrintStringXml(boolean prettyPrintStringXml) {
        this.prettyPrintStringXml = prettyPrintStringXml;
        return this;
    }

    /**
     * Defines which Xml-Assert assertion method will actually be used. Defaults to
     * {@link CompareAssert#areIdentical()}.
     * <p>
     * You can also use this to apply further customizations to the CompareAssert. Consult
     * the xml-unit documentation for further information.
     * <p>
     * Note: if you also use {@link #withComparisonRules(Consumer)}, you can <b>not</b>
     * use {@link CompareAssert#withDifferenceEvaluator(DifferenceEvaluator)} here, as
     * your {@linkplain DifferenceEvaluator} will always be overridden by the one that is
     * configured in {@linkplain #withComparisonRules(Consumer)}.
     *
     * @param xmls Consumes the {@link CompareAssert} which compares the actual and
     *            expected xml.
     * @return This builder instance.
     */
    @API(status = Status.EXPERIMENTAL)
    public XmlSnapshot compareUsing(Consumer<CompareAssert> xmls) {
        this.compareAssertConsumer = Arguments.requireNonNull(xmls, "CompareAssert consumer must not be null");
        return this;
    }

    /**
     * Enables a simple debug output to System.out for the xpaths that are used in
     * {@link #withComparisonRules(Consumer)}. This will print out all the nodes that are
     * matched by the xpaths that are used in custom comparison rules.
     * <p>
     * Note that this method must be called before calling
     * {@link #withComparisonRules(Consumer)}.
     *
     * @param enableXPathDebugging Whether to enable debug output for xpaths used in
     *            {@link #withComparisonRules(Consumer)}.
     * @return This instance.
     * @since 1.6.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.6.0")
    public XmlSnapshot withEnableXPathDebugging(boolean enableXPathDebugging) {
        State.check(this.rules == null,
                "xpath debugging must be enabled before specifying custom comparison rules");
        this.xPathDebug = enableXPathDebugging
                ? XPathDebug.enabledAt(StackTraces.findImmediateCaller())
                : XPathDebug.disabled();
        return this;
    }

    /**
     * Allows to specify extra comparison rules that are applied to certain paths within
     * the xml snapshots.
     * <p>
     * Paths on the {@link ComparisonRuleBuilder} must conform to standard XPath syntax.
     * You can enable debug output for xpath expressions using
     * {@link #withEnableXPathDebugging(boolean)}. Note that debug output must be enabled
     * before calling this method.
     * <p>
     * If you intend to use custom rules for XMLs containing namespaces, you need to
     * configure a namespace context via {@link #withXPathNamespaceContext(Map)}.
     *
     * <pre>
     * XmlSnapshot.xml()
     *         .withNamespaceContext(Map.of("ns1", "foo:1", "ns2", "foo:2"))
     *         .withComparisonRules(rules -&gt; rules
     *                 .pathAt("/ns1:root/ns2:child/text()").ignore())
     * </pre>
     *
     * The above XPath custom rule would match <code>"some text"</code> in the given XML:
     *
     * <pre>
     * &lt;whatever:root xmlns:whatever="foo:1" xmlns:doesntmatter="foo:2"&gt;
     *     &lt;doesntmatter:child&gt;some text&lt;/doesntmatter:child&gt;
     * &lt;/whatever:root&gt;
     * </pre>
     *
     * As demonstrated here, you only need to make sure that you are providing the correct
     * namespace URIs. The prefix names that you use in the XPath do not need to match the
     * prefix names within the matched documents.
     *
     * <p>
     * Note: This will customize the {@link DifferenceEvaluator} that is used. Thus you
     * can not use this method in combination with {@link #compareUsing(Consumer)} if you
     * intend to use an own {@link DifferenceEvaluator}.
     *
     * @param rules A consumer to which a {@link ComparisonRuleBuilder} will be passed.
     * @return This instance.
     * @since 1.3.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.3.0")
    public XmlSnapshot withComparisonRules(Consumer<ComparisonRuleBuilder> rules) {
        Arguments.requireNonNull(rules, "rules consumer must not be null");
        this.rules = rules;
        return this;
    }

    /**
     * Allows to set a namespace context by providing a mapping from prefixes to xml
     * namespace URIs.
     * <p>
     * If you intend to use {@link #withComparisonRules(Consumer) custom comparison rules}
     * on XMLs with namespaces it is mandatory to define a namespace context. XPath
     * comparison does not automatically fall back to just using local names.
     * <p>
     * The prefixes that you can configure here are only relevant to the XPath expression
     * itself and do not need to match the prefixes of the compared XMLs. However, the
     * URIs must be identical.
     * <p>
     * Note: You must set the namespace context before configuring the custom rules.
     *
     * @param namespaceContext A mapping of prefixes to xml namespace URIs.
     * @return This instance.
     * @see #withComparisonRules(Consumer)
     * @since 1.9.0
     */
    @API(status = Status.EXPERIMENTAL, since = "1.9.0")
    public XmlSnapshot withXPathNamespaceContext(Map<String, String> namespaceContext) {
        State.check(this.rules == null,
                "namespaceContext must be enabled before specifying custom comparison rules");
        this.namespaceContext = Arguments.requireNonNull(namespaceContext, "namespaceContext must not be null");
        return this;
    }

    @Override
    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = JaxbXmlSnapshotSerializer.withExplicitJaxbContext(
                jaxbContext, marshallerSupplier, prettyPrintStringXml);

        final StructuralAssertions structuralAssertions = new XmlUnitStructuralAssertions(
                compareAssertConsumer,
                rules,
                namespaceContext,
                xPathDebug);
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }

    @FunctionalInterface
    public interface MarshallerSupplier {

        Marshaller createMarshaller(JAXBContext jaxbContext) throws JAXBException;
    }
}
