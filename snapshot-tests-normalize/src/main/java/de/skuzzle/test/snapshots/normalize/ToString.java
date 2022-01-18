package de.skuzzle.test.snapshots.normalize;

final class ToString {

    public static String identityToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(obj));
    }
}
