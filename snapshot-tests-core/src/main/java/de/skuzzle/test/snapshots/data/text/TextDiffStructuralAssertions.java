package de.skuzzle.test.snapshots.data.text;

import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.StructuralAssertions;

final class TextDiffStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        if (!storedSnapshot.equals(serializedActual)) {
            throw new AssertionFailedError("Stored snapshot doesn't match actual result.", storedSnapshot, serializedActual);
        }
    }
}
