package de.skuzzle.test.snapshots.normalize;

import java.util.stream.Stream;

final class Reflection {

    static Stream<Class<?>> superClassHierarchy(Class<?> child) {
        if (child == Object.class) {
            return Stream.of(child);
        }
        return Stream.concat(Stream.of(child), superClassHierarchy(child.getSuperclass()));
    }

}
