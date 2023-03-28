package de.skuzzle.test.snapshots.junit5;

import de.skuzzle.test.snapshots.reflection.Classes;

/**
 * Detects whether the new -junit5 module is on the class path and logs a warning if not.
 *
 * @author Simon Taddiken
 */
@Deprecated(since = "1.8.0", forRemoval = true)
final class DetectJunit5Module {

    private static final boolean PLACEHOLDER_AVAILABLE = Classes
            .isClassPresent("de.skuzzle.test.snapshots.junit5x.PlaceHolder");
    private static final boolean JUNIT5_AVAILABLE = Classes.isClassPresent("org.junit.jupiter.api.Test");
    static volatile boolean WARNING_PRINTED = false;

    public DetectJunit5Module() {
        if (!PLACEHOLDER_AVAILABLE && JUNIT5_AVAILABLE) {
            System.err.println(
                    "DEPRECATION WARNING: Starting from snapshot-tests version 1.8.0, you should depend on 'snapshot-tests-junit5' module.");
            System.err.println();
            System.err.println("To remove this warning message, follow these simple migration steps:");
            System.err.println();
            System.err.println("- Remove direct dependency to 'snapshot-tests-core' [optional]");
            System.err.println("- Add dependency to 'snapshot-tests-junit5' [mandatory]");
            System.err.println("  If you are using Maven:");
            System.err.println("    <dependency>");
            System.err.println("        <groupId>de.skuzzle.test</groupId>");
            System.err.println("        <artifactId>snapshot-tests-junit5</artifactId>");
            System.err.println("    </dependency>");
            System.err.println();
            System.err.println("  If you are using Gradle (groovy):");
            System.err.println("    testImplementation 'de.skuzzle.test:snapshot-tests-junit5'");
            System.err.println();
            System.err.println("  If you are using Gradle (kotlin):");
            System.err.println("    testImplementation(\"de.skuzzle.test:snapshot-tests-junit5\")");
            System.err.println();
            System.err.println(
                    "If you don't follow this advice, your build will not compile with the next major version of snaphot-tests!");
            System.err.println();
            System.err.println("This warning will disapear once you have added the respective dependency");
            System.err.println();
            System.err.println();
            WARNING_PRINTED = true;
        }
    }
}
