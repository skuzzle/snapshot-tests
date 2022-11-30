package de.skuzzle.test.snapshots.release.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.w3c.dom.Node;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.xpath.JAXPXPathEngine;
import org.xmlunit.xpath.XPathEngine;

import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot;
import de.skuzzle.test.snapshots.directoryparams.FilesFrom;
import de.skuzzle.test.snapshots.directoryparams.PathFilter;
import de.skuzzle.test.snapshots.directoryparams.TestFile;
import de.skuzzle.test.snapshots.junit5.EnableSnapshotTests;

@EnableSnapshotTests
public class FilesAreProperlyProcessedTest {

    private static final String EXPECTED_GROUP_ID = "de.skuzzle.test";
    private static final Pattern MAVEN_VERSION = Pattern.compile("\\d+\\.\\d+\\.\\d+(-\\w+)?");

    private static final class FlattenedPoms implements PathFilter {

        @Override
        public boolean include(Path path) throws IOException {
            return path.getFileName().toString().equals(".flattened-pom.xml");
        }

    }

    private static String xpath(String... path) {
        return "/" + Arrays.stream(path)
                .map(segment -> String.format("*[local-name()='%s']", segment))
                .collect(Collectors.joining("/"));
    }

    private static final String XPATH_DESCRIPTION = xpath("project", "description") + "/text()";
    private static final String XPATH_NAME = xpath("project", "name") + "/text()";
    private static final String XPATH_URL = xpath("project", "url") + "/text()";
    private static final String XPATH_LICENSE = xpath("project", "licenses", "license");
    private static final String XPATH_DEVELOPER = xpath("project", "developers", "developer");
    private static final String XPATH_VERSION = "//*[local-name()='version']/text()";
    private static final String XPATH_GROUP_ID = "//*[local-name()='groupId']/text()";

    final XPathEngine xpath = new JAXPXPathEngine();

    @ParameterizedTest
    @FilesFrom(projectDirectory = "../", recursive = true, filter = FlattenedPoms.class)
    void testFlattendPomsContainAllRequiredInformationForSonatype(TestFile testFile) throws IOException {

        XmlAssert.assertThat(testFile.asText())
                .nodesByXPath(XPATH_NAME)
                .isNotEmpty().allSatisfy(node -> assertThat(node.getTextContent()).isNotEmpty());

        XmlAssert.assertThat(testFile.asText())
                .nodesByXPath(XPATH_DESCRIPTION)
                .isNotEmpty().allSatisfy(node -> assertThat(node.getTextContent()).isNotEmpty());

        XmlAssert.assertThat(testFile.asText())
                .nodesByXPath(XPATH_URL)
                .isNotEmpty().allSatisfy(node -> assertThat(node.getTextContent()).isNotEmpty());

        XmlAssert.assertThat(testFile.asText())
                .nodesByXPath(XPATH_DEVELOPER)
                .isNotEmpty();

        XmlAssert.assertThat(testFile.asText())
                .nodesByXPath(XPATH_LICENSE)
                .isNotEmpty();
    }

    @ParameterizedTest
    @FilesFrom(projectDirectory = "../", recursive = false, extensions = "md")
    void testPlaceholderResolvedInReadmeFiles(TestFile readmeOrReleaseNotes) throws Exception {
        final String contents = readmeOrReleaseNotes.asText();
        assertThat(contents).doesNotContain("${project.version}");
        assertThat(contents).doesNotContain("${version.junit}");
    }

    @Test
    void testBomFilePlaceholdersResolved(Snapshot snapshot) throws Exception {
        final Path flattenedPom = resolveMavenModule("snapshot-tests-bom").resolve(".flattened-pom.xml");
        final String flattenedPomContents = Files.readString(flattenedPom);

        snapshot.assertThat(flattenedPomContents).as(XmlSnapshot.xml()
                .withPrettyPrintStringXml(false)
                .withComparisonRules(rules -> rules
                        .pathAt(XPATH_GROUP_ID).mustMatch(nodeText(EXPECTED_GROUP_ID))
                        .pathAt(XPATH_VERSION).mustMatch(MAVEN_VERSION))
                .compareUsing(xmls -> xmls
                        .normalizeWhitespace()
                        .areIdentical()))
                .matchesSnapshotStructure();
    }

    private Predicate<? super Object> nodeText(String text) {
        return node -> ((Node) node).getTextContent().equals(text);
    }

    private Path resolveMavenModule(String moduleName) throws IOException {
        return Path.of("..", moduleName).toRealPath();
    }
}
