package de.skuzzle.test.snapshots.data.xml.xmlunit;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(status = Status.INTERNAL, since = "1.9.0")
public final class XPaths {

    private XPaths() {
        // hidden
    }

    /**
     * Creates an absolute XPath starting at the root that only uses local names and thus
     * disregards any namespace prefixes.
     *
     * @param path The path segments.
     * @return The XPath String.
     */
    public static String localNamePath(String... path) {
        return "/" + Arrays.stream(path)
                .map(segment -> String.format("*[local-name()='%s']", segment))
                .collect(Collectors.joining("/"));
    }
}
