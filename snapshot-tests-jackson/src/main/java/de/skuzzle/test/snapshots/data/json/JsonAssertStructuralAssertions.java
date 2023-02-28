package de.skuzzle.test.snapshots.data.json;

import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.StructuralAssertions;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

final class JsonAssertStructuralAssertions implements StructuralAssertions {

    private final JSONComparator comparator;

    JsonAssertStructuralAssertions(JSONComparator comparator) {
        this.comparator = comparator;
    }

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, SnapshotException {
        try {
            JSONAssert.assertEquals(storedSnapshot, serializedActual, comparator);
        } catch (final Exception e) {
            throw new SnapshotException("Error while asserting for json equality", e);
        }
    }

}
