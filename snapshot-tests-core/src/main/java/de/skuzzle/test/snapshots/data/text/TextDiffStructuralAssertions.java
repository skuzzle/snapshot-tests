package de.skuzzle.test.snapshots.data.text;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.data.text.TextDiff.Settings;

final class TextDiffStructuralAssertions implements StructuralAssertions {

    private final Settings settings;

    public TextDiffStructuralAssertions(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, SnapshotException {
        final TextDiff textDiff = TextDiff.compare(settings, storedSnapshot, serializedActual);
        if (textDiff.changesDetected()) {
            throw new TextDiffAssertionError("Stored snapshot doesn't match actual result.", textDiff);
        }
    }

}
