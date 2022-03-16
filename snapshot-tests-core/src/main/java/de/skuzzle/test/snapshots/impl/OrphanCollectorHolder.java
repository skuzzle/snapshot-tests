package de.skuzzle.test.snapshots.impl;

/**
 * Statically collects {@link OrphanDetectionResult}s which helps us with testing.
 *
 * @author Simon Taddiken
 * @since 1.2.1
 */
final class OrphanCollectorHolder {

    private static final OrphanCollector NO_OP = new OrphanCollector() {

        @Override
        public void addRawResult(OrphanDetectionResult result) {

        }

        @Override
        public void addPostProcessedResult(OrphanDetectionResult result) {

        }
    };

    private static volatile OrphanCollector collector = NO_OP;

    public static OrphanCollector getCollector() {
        return collector;
    }

    public static void reset() {
        collector = NO_OP;
    }

    public static void setCollector(OrphanCollector collector) {
        OrphanCollectorHolder.collector = collector;
    }

    interface OrphanCollector {

        void addRawResult(OrphanDetectionResult result);

        void addPostProcessedResult(OrphanDetectionResult result);
    }
}
