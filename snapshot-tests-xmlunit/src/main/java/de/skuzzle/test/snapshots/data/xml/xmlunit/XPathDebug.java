package de.skuzzle.test.snapshots.data.xml.xmlunit;

public final class XPathDebug {

    private static final XPathDebug DISABLED = new XPathDebug(false, null);

    final boolean enabled;
    final Object enabledAt;

    private XPathDebug(boolean enabled, Object enabledAt) {
        this.enabled = enabled;
        this.enabledAt = enabledAt;
    }

    public static XPathDebug disabled() {
        return DISABLED;
    }

    public static XPathDebug enabledAt(Object enabledAt) {
        return new XPathDebug(true, enabledAt);
    }

    void log(String message, Object... args) {
        if (enabled) {
            System.err.printf(message + "%n", args);
        }
    }
}
