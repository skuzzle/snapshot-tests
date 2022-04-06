package de.skuzzle.test.snapshots.normalize;

import static de.skuzzle.test.snapshots.normalize.Generators.dateLike;
import static de.skuzzle.test.snapshots.normalize.Generators.deterministicUUID;
import static de.skuzzle.test.snapshots.normalize.Strings.consistentlyReplace;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class StringsTest {

    @Test
    void testConsistentlyReplaceUUID() {
        final String uuid1 = UUID.randomUUID().toString();
        final String uuid2 = UUID.randomUUID().toString();

        final String base = uuid1 + " " + uuid2 + " " + uuid2;
        final String result = StringNormalization.withModifications(
                consistentlyReplace(Generators.UUID, deterministicUUID()))
                .apply(base);

        assertThat(result)
                .isEqualTo(
                        "00000000-0000-0000-0000-000000000000 00000000-0000-0000-0000-000000000001 00000000-0000-0000-0000-000000000001");
    }

    @Test
    void testConsistentlyReplaceDateDays() {
        final String base = "1987-12-09 1987-12-10 1987-12-09";
        final Function<String, String> replaceFunction = consistentlyReplace(
                Pattern.compile("\\d{4}-\\d{2}-\\d{2}"),
                dateLike(DateTimeFormatter.ofPattern("yyyy-MM-dd"), ChronoUnit.DAYS));

        assertThat(replaceFunction.apply(base)).isEqualTo("1970-01-01 1970-01-02 1970-01-01");
    }

    @Test
    void testConsistentlyReplaceDateYears() {
        final String base = "1987-12-09 1987-12-10 1987-12-09";
        final Function<String, String> replaceFunction = consistentlyReplace(
                Pattern.compile("\\d{4}-\\d{2}-\\d{2}"),
                dateLike(DateTimeFormatter.ofPattern("yyyy-MM-dd"), ChronoUnit.YEARS));

        assertThat(replaceFunction.apply(base)).isEqualTo("1970-01-01 1971-01-01 1970-01-01");
    }
}
