package de.skuzzle.test.snapshots.data.text;

import de.skuzzle.test.snapshots.StructuralAssertions;

final class TextDiffStructuralAssertions implements StructuralAssertions {

    private final DiffInterpreter diffInterpreter;

    TextDiffStructuralAssertions(DiffInterpreter diffInterpreter) {
        this.diffInterpreter = diffInterpreter;
    }

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        final TextDiff textDiff = TextDiff.diffOf(diffInterpreter, storedSnapshot, serializedActual);
        if (textDiff.hasDifference()) {
            throw new TextDiffAssertionError("Stored snapshot doesn't match actual result.", textDiff);
        }
    }
}
