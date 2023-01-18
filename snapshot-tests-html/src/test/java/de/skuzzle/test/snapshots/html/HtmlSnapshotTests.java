package de.skuzzle.test.snapshots.html;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.data.html.HtmlSnapshot;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class HtmlSnapshotTests {

    @Test
    void testCompareHtmlPrettyPrinted(Snapshot snapshot) throws Exception {
        final SnapshotTestResult result = snapshot
                .assertThat("<html><body someAttribute=\"hehe\"><h1>Test</h1></body></html>")
                .as(HtmlSnapshot.html().withPrettyPrintSnapshot(true))
                .matchesSnapshotStructure();

        final String snapshotText = result.serializedActual();
        assertThat(snapshotText).isEqualTo(""
                + "<html>\n"
                + "    <head></head>\n"
                + "    <body someattribute=\"hehe\">\n"
                + "        <h1>Test</h1>\n"
                + "    </body>\n"
                + "</html>");
    }

    @Test
    void testCompareHtmlWithCustomComparisonRulePattern(Snapshot snapshot) throws Exception {
        snapshot
                .assertThat("<html><body><h1>Not_Test</h1></body></html>")
                .as(HtmlSnapshot.html()
                        .withComparisonRules(rules -> rules
                                .pathAt("/html/body/h1/text()").mustMatch(Pattern.compile("\\w+"))))
                .matchesSnapshotStructure();
    }

    @Test
    void testCompareHtmlWithCustomComparisonRuleIgnore(Snapshot snapshot) throws Exception {
        snapshot
                .assertThat("<html><body><h1>Test 123</h1></body></html>")
                .as(HtmlSnapshot.html()
                        .withComparisonRules(rules -> rules
                                .pathAt("/html/body/h1/text()").ignore()))
                .matchesSnapshotStructure();
    }
}
