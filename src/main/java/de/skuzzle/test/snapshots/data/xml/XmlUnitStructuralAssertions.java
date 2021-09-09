package de.skuzzle.test.snapshots.data.xml;

import org.xmlunit.assertj.XmlAssert;

import de.skuzzle.test.snapshots.data.StructuralAssertions;

public class XmlUnitStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) throws Exception {
        XmlAssert.assertThat(serializedActual).and(storedSnapshot).areIdentical();
    }

}
