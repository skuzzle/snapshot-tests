package de.skuzzle.test.snapshots.normalize;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

final class VisitorContext {

    private final Set<Object> visitedInstances = Collections.newSetFromMap(new IdentityHashMap<>());

    public boolean alreadyVisisted(Object obj) {
        return visitedInstances.add(obj);
    }

    public boolean isTerminal(Object root) {
        return root.getClass().getPackageName().startsWith("java.")
                || root.getClass().getPackageName().startsWith("javax.");
    }
}
