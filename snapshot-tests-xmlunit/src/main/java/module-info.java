module de.skuzzle.test.snapshots.xmlunit {
    requires de.skuzzle.test.snapshots.core;
    requires de.skuzzle.test.snapshots.common;
    requires org.apiguardian.api;

    requires java.xml;

    requires org.assertj.core;
    requires transitive org.xmlunit;
    requires org.xmlunit.assertj;

    exports de.skuzzle.test.snapshots.data.xmlunit;
}