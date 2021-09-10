package de.skuzzle.test.snapshots.data.text;

import org.assertj.core.api.Assertions;

import de.skuzzle.test.snapshots.data.StructuralAssertions;

public class TextDiffStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        final TextDiff textDiff = TextDiff.diffOf(storedSnapshot, serializedActual);
        if (textDiff.hasDifference()) {
            Assertions.fail("Stored snapshot doesn't match actual result.%nUnified diff:%n%s", textDiff);
        }
    }

}
