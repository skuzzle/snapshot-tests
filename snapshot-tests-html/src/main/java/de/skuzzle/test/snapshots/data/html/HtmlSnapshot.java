package de.skuzzle.test.snapshots.data.html;

import java.util.function.Consumer;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.xmlunit.assertj.CompareAssert;
import org.xmlunit.diff.DifferenceEvaluator;

import de.skuzzle.test.snapshots.ComparisonRuleBuilder;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;
import de.skuzzle.test.snapshots.data.xmlunit.XmlUnitComparisonRuleBuilder;
import de.skuzzle.test.snapshots.data.xmlunit.XmlUnitStructuralAssertions;
import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Allows to create and compare snapshots from HTML strings. Please note that HTML
 * comparison only works on String input.
 * <p>
 * You can either use a pre-configured default instance via {@link #html} or use the
 * static factory method {@link #html()} to customize the construction.
 *
 * <pre>
 * &#64;EnableSnapshotTests
 * class HtmlSnapshotTest {
 *
 *     &#64;Test
 *     void testHtlmSnapshot(Snapshot snapshot) {
 *         final String htmlString = testSubject.renderHtml(...);
 *         snapshot.assertThat(htmlString)
 *             .as(HtmlSnapshot.html()
 *                 .withPrettyPrintSnapshot(true))
 *             .matchesSnapshotStructure();
 *     }
 * }
 * </pre>
 *
 * @author Simon Taddiken
 * @since 1.5.0
 */
@API(status = Status.EXPERIMENTAL, since = "1.5.0")
public final class HtmlSnapshot implements StructuredDataProvider {
    /**
     * Simple default {@link StructuredData} instance with disabled pretty printing. If
     * you need to customize the comparison behavior, use {@link #html()} instead.
     *
     * @see #html()
     */
    public static final StructuredDataProvider html = html().build();

    // Defines how snapshots are being asserted on using xml-unit
    private Consumer<CompareAssert> compareAssertConsumer = CompareAssert::areIdentical;
    // null unless customized
    private DifferenceEvaluator differenceEvaluator;

    private boolean prettyPrintSnapshot = false;

    /**
     * Creates a new builder instance on which custom comparison behavior can be
     * configured if required.
     * <p>
     * You can use {@link #html} if you don't need to apply any customizations.
     * </p>
     *
     * @return A new {@link HtmlSnapshot} instance.
     * @see html
     */
    public static HtmlSnapshot html() {
        return new HtmlSnapshot();
    }

    private HtmlSnapshot() {
        // hidden
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
     *            expected html.
     * @return This builder instance.
     */
    public HtmlSnapshot compareUsing(Consumer<CompareAssert> xmls) {
        this.compareAssertConsumer = Arguments.requireNonNull(xmls, "CompareAssert consumer must not be null");
        return this;
    }

    /**
     * Allows to specify extra comparison rules that are applied to certain paths within
     * the html snapshots.
     * <p>
     * Paths on the {@link ComparisonRuleBuilder} must conform to standard XPath syntax.
     * <p>
     * Note: This will customize the {@link DifferenceEvaluator} that is used. Thus you
     * can not use this method in combination with {@link #withComparisonRules(Consumer)}
     * if you intend to use an own {@link DifferenceEvaluator}.
     *
     * @param rules A consumer to which a {@link ComparisonRuleBuilder} will be passed.
     * @return This instance.
     */
    public HtmlSnapshot withComparisonRules(Consumer<ComparisonRuleBuilder> rules) {
        Arguments.requireNonNull(rules, "rules consumer must not be null");
        final XmlUnitComparisonRuleBuilder comparatorCustomizerImpl = new XmlUnitComparisonRuleBuilder();
        rules.accept(comparatorCustomizerImpl);
        this.differenceEvaluator = comparatorCustomizerImpl.build();
        return this;
    }

    /**
     * Sets whether to pretty print the HTML snapshot before serializing.
     * <p>
     * Pretty printing is disabled by default because it requires the HTML to be parsed
     * multiple times which might slow down your test. If this is not an issue for you,
     * pretty printing is advisable, as it will result in better human readable snapshot
     * files and also better readable diffs.
     * </p>
     * <p>
     * It is also notable that pretty printing might modify/sanitize the input HTML string
     * during parsing. So the output might not be exactly equal to your input. This is a
     * side effect of parsing the HTML into a valid DOM object.
     * </p>
     *
     * @param prettyPrintSnapshot Whether to pretty print the snapshots. Defaults to
     *            <code>false</code>.
     * @return This instance.
     */
    public HtmlSnapshot withPrettyPrintSnapshot(boolean prettyPrintSnapshot) {
        this.prettyPrintSnapshot = prettyPrintSnapshot;
        return this;
    }

    @Override
    public StructuredData build() {
        final SnapshotSerializer snapshotSerializer = new HtmlSnapshotSerializer(prettyPrintSnapshot);
        final StructuralAssertions structuralAssertions = new XmlUnitStructuralAssertions(compareAssertConsumer,
                differenceEvaluator);
        return StructuredData.with(snapshotSerializer, structuralAssertions);
    }

}
