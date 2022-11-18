package de.skuzzle.test.snapshots.release.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.skuzzle.test.snapshots.EnableSnapshotTests;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.data.xml.XmlSnapshot;

@EnableSnapshotTests
public class CheckPomFilesTest {

    private static final Pattern MAVEN_VERSION = Pattern.compile("\\d+\\.\\d+\\.\\d+(-\\w+)?");

    @Test
    void testBomFilePlaceholdersResolved(Snapshot snapshot) throws Exception {
        final Path flattenedPom = resolveMavenModule("snapshot-tests-bom").resolve(".flattened-pom.xml");
        final String flattenedPomContents = Files.readString(flattenedPom);
        snapshot.assertThat(flattenedPomContents)
                .as(XmlSnapshot.xml()
                        .withPrettyPrintStringXml(false)
                        .withComparisonRules(rule -> rule
                                .pathAt("/project/dependencyManagement/dependencies/dependency/groupId/text()")
                                .mustMatch("de.skuzzle.test"::equals)
                                .pathAt("/project/dependencyManagement/dependencies/dependency/version/text()")
                                .mustMatch(MAVEN_VERSION)))
                .matchesSnapshotStructure();
    }

    private Path resolveMavenModule(String moduleName) throws IOException {
        return Path.of("..", moduleName).toRealPath();
    }
}
