package de.skuzzle.test.snapshots.normalize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

final class SpecialTypesAndValues {

    private SpecialTypesAndValues() {
        // hidden
    }

    public static boolean isEmptyableType(Class<?> originalType) {
        return Map.class.isAssignableFrom(originalType)
                || Collection.class.isAssignableFrom(originalType)
                || Iterable.class.isAssignableFrom(originalType)
                || String.class.isAssignableFrom(originalType)
                || originalType.isArray();
    }

    public static Object getEmptyValueForType(Class<?> originalType) {
        if (SortedMap.class.isAssignableFrom(originalType)) {
            // XXX: this might override the existing comparator in place
            return new TreeMap<>();
        } else if (Map.class.isAssignableFrom(originalType)) {
            return new HashMap<>();
        } else if (SortedSet.class.isAssignableFrom(originalType)) {
            return new TreeSet<>();
        } else if (Set.class.isAssignableFrom(originalType)) {
            return new HashSet<>();
        } else if (Collection.class.isAssignableFrom(originalType) || Iterable.class.isAssignableFrom(originalType)) {
            return new ArrayList<>();
        } else if (String.class.isAssignableFrom(originalType)) {
            return "";
        } else if (originalType.isArray()) {
            return new Object[0];
        }
        return null;
    }

}
