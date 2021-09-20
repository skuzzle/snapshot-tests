package de.skuzzle.test.snapshots.data.xml;

import org.xmlunit.assertj.XmlAssert;

import de.skuzzle.test.snapshots.StructuralAssertions;

class XmlUnitStructuralAssertions implements StructuralAssertions {

    @Override
    public void assertEquals(String storedSnapshot, String serializedActual) {
        XmlAssert.assertThat(serializedActual).and(storedSnapshot).areIdentical();
    }

}
