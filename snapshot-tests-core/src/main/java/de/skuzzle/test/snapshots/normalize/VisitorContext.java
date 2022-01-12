package de.skuzzle.test.snapshots.normalize;

import java.util.HashSet;
import java.util.Set;

final class VisitorContext {
    private final Set<Object> visitedInstances = new HashSet<>();

    public boolean addVisitedInstance(Object obj) {
        return visitedInstances.add(obj);
    }
}
