package de.skuzzle.test.snapshots.normalize;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class NormalizeTest {

    private static final Pattern uuid = Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})");

    @Test
    void testConsistentlyReplace() throws Exception {
        final String uuid0 = UUID.randomUUID().toString();
        final String uuid1 = UUID.randomUUID().toString();
        final String s = uuid0 + " and " + uuid1 + " and " + uuid1;
        final String result = Normalize.consistentlyReplace(uuid, (i, m) -> "uuid" + i).apply(s);
        assertThat(result).isEqualTo("uuid0 and uuid1 and uuid1");
    }
}
