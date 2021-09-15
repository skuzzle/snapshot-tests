package de.skuzzle.test.snapshots.data.json;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.StructuralAssertions;

class JsonAssertStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, SnapshotException {
        try {
            JSONAssert.assertEquals(storedSnapshot, serializedActual, JSONCompareMode.STRICT);
        } catch (final Exception e) {
            throw new SnapshotException("Error while asserting for json equality", e);
        }
    }

}
