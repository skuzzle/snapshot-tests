package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.ListAssert;

import de.skuzzle.test.snapshots.impl.OrphanCollectorHolder.OrphanCollector;

public class MockOrphanCollector implements OrphanCollector {

    public static MockOrphanCollector installNewInstance() {
        final MockOrphanCollector collector = new MockOrphanCollector();
        OrphanCollectorHolder.setCollector(collector);
        return collector;
    }

    public static MockOrphanCollector uninstall() {
        final MockOrphanCollector result = (MockOrphanCollector) OrphanCollectorHolder.getCollector();
        OrphanCollectorHolder.reset();
        return result;
    }

    private final List<OrphanDetectionResult> rawResults = new ArrayList<>();
    private final List<OrphanDetectionResult> postProcessedResults = new ArrayList<>();

    private MockOrphanCollector() {
        // hidden
    }

    public ListAssert<OrphanDetectionResult> rawResults() {
        return assertThat(rawResults);
    }

    public ListAssert<OrphanDetectionResult> results() {
        return assertThat(postProcessedResults);
    }

    @Override
    public void addRawResult(OrphanDetectionResult result) {
        rawResults.add(result);
    }

    @Override
    public void addPostProcessedResult(OrphanDetectionResult result) {
        postProcessedResults.add(result);
    }

}
