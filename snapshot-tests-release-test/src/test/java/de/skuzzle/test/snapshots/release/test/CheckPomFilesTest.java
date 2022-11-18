package de.skuzzle.test.snapshots.release.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot;

@EnableSnapshotTests
public class CheckPomFilesTest {

    private static final String EXPECTED_GROUP_ID = "de.skuzzle.test";
    private static final Pattern MAVEN_VERSION = Pattern.compile("\\d+\\.\\d+\\.\\d+(-\\w+)?");

    @Test
    void testBomFilePlaceholdersResolved(Snapshot snapshot) throws Exception {
        final Path flattenedPom = resolveMavenModule("snapshot-tests-bom").resolve(".flattened-pom.xml");
        final String flattenedPomContents = Files.readString(flattenedPom);
        snapshot.assertThat(flattenedPomContents)
                .as(XmlSnapshot.xml()
                        .withPrettyPrintStringXml(false)
                        .withComparisonRules(rule -> rule
                                .pathAt("//*[local-name()='groupId']/text()").mustMatch(nodeText(EXPECTED_GROUP_ID))
                                .pathAt("//*[local-name()='version']/text()").mustMatch(MAVEN_VERSION)))
                .matchesSnapshotStructure();
    }

    private Predicate<? super Object> nodeText(String expectedTextContent) {
        return objectWhichIsANode -> expectedTextContent.equals(((Node) objectWhichIsANode).getTextContent());

    }

    private Path resolveMavenModule(String moduleName) throws IOException {
        return Path.of("..", moduleName).toRealPath();
    }
}
