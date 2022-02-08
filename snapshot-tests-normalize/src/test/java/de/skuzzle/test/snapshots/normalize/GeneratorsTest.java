package de.skuzzle.test.snapshots.normalize;

import org.junit.jupiter.api.Test;

public class GeneratorsTest {

    @Test
    void testUuidLike() throws Exception {
        final String apply = Generators.uuidLike().apply(100000000, null);
        System.out.println(apply);
    }
}
