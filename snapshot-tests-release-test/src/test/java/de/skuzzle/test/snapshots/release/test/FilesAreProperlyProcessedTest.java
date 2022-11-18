package de.skuzzle.test.snapshots.release.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.xpath.JAXPXPathEngine;
import org.xmlunit.xpath.XPathEngine;

import de.skuzzle.test.snapshots.directoryparams.FilesFrom;
import de.skuzzle.test.snapshots.directoryparams.PathFilter;
import de.skuzzle.test.snapshots.directoryparams.TestFile;

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
    private static final String XPATH_VERSION = "//*[local-name()='version']/text()";
    private static final String XPATH_GROUP_ID = "//*[local-name()='groupId']/text()";

    final XPathEngine xpath = new JAXPXPathEngine();

    @ParameterizedTest
    @FilesFrom(directory = "../../../../", recursive = true, filter = FlattenedPoms.class)
    void testFlattendPomsContainAllRequiredInformationForSonatype(TestFile testFile) throws IOException {

        XmlAssert.assertThat(testFile.asText()).nodesByXPath(XPATH_NAME)
                .isNotEmpty().allSatisfy(node -> assertThat(node.getTextContent()).isNotEmpty());

        XmlAssert.assertThat(testFile.asText()).nodesByXPath(XPATH_DESCRIPTION)
                .isNotEmpty().allSatisfy(node -> assertThat(node.getTextContent()).isNotEmpty());
    }

    @ParameterizedTest
    @FilesFrom(directory = "../../../../", recursive = false, extensions = "md")
    void testPlaceholderResolvedInReadmeFiles(TestFile readmeOrReleaseNotes) throws Exception {
        final String contents = readmeOrReleaseNotes.asText();
        assertThat(contents).doesNotContain("${project.version}");
        assertThat(contents).doesNotContain("${version.junit}");
    }

    @Test
    void testBomFilePlaceholdersResolved() throws Exception {
        final Path flattenedPom = resolveMavenModule("snapshot-tests-bom").resolve(".flattened-pom.xml");
        final String flattenedPomContents = Files.readString(flattenedPom);

        XmlAssert.assertThat(flattenedPomContents).nodesByXPath(XPATH_GROUP_ID)
                .allSatisfy(node -> assertThat(node.getTextContent()).isEqualTo(EXPECTED_GROUP_ID));

        XmlAssert.assertThat(flattenedPomContents).nodesByXPath(XPATH_VERSION)
                .allSatisfy(node -> assertThat(node.getTextContent()).matches(MAVEN_VERSION));

    }

    private Path resolveMavenModule(String moduleName) throws IOException {
        return Path.of("..", moduleName).toRealPath();
    }
}
