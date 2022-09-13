package de.skuzzle.test.snapshots.data.text;

import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.StructuralAssertions;

final class TextDiffStructuralAssertions implements StructuralAssertions {

    private final DiffInterpreter diffInterpreter;

    TextDiffStructuralAssertions(DiffInterpreter diffInterpreter) {
        this.diffInterpreter = diffInterpreter;
    }

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        final boolean hasDifference = TextDiff.diffOf(diffInterpreter, storedSnapshot, serializedActual)
                .hasDifference();
        if (hasDifference) {
            throw new AssertionFailedError("Stored snapshot doesn't match actual result.", storedSnapshot,
                    serializedActual);
        }
    }
}
