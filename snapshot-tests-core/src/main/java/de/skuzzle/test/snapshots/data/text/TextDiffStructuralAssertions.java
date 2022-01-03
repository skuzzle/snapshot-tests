package de.skuzzle.test.snapshots.data.text;

import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.StructuralAssertions;

public class TextDiffStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        final TextDiff textDiff = TextDiff.diffOf(storedSnapshot, serializedActual);
        if (textDiff.hasDifference()) {
            throw new AssertionFailedError(
                    String.format("Stored snapshot doesn't match actual result.%nUnified diff:%n%s", textDiff),
                    storedSnapshot, serializedActual);
        }
    }

}
