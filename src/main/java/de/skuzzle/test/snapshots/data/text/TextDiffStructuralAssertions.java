package de.skuzzle.test.snapshots.data.text;

import org.assertj.core.api.Assertions;

import de.skuzzle.test.snapshots.data.StructuralAssertions;

public class TextDiffStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) throws Exception {
        final TextDiff textDiff = TextDiff.diffOf(storedSnapshot, serializedActual);
        if (textDiff.hasDifference()) {
            Assertions.assertThat(serializedActual)
                    .as("Stored snapshot doesn't match actual result.%nDiff:%n%s", textDiff)
                    .isEqualTo(storedSnapshot);
        }
    }

}
