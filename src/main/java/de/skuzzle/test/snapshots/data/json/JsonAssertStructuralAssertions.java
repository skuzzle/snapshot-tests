package de.skuzzle.test.snapshots.data.json;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import de.skuzzle.test.snapshots.data.StructuralAssertions;

class JsonAssertStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) throws AssertionError, JSONException {
        JSONAssert.assertEquals(storedSnapshot, serializedActual, JSONCompareMode.STRICT);
    }

}
