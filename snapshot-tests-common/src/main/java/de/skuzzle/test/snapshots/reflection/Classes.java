package de.skuzzle.test.snapshots.reflection;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Utilities for working with {@link Class classes}.
 * 
 * @author Simon Taddiken
 * @since 1.8.0
 */
@API(status = Status.INTERNAL, since = "1.8.0")
public final class Classes {

    /**
     * Detects whether a class with given full qualified name is available on the
     * classpath of the given classloader.
     * 
     * @param classloader The classloader.
     * @param classname The classname.
     * @return Whether the class is available.
     */
    public static boolean isClassPresent(ClassLoader classloader, String classname) {
        Arguments.check(classloader != null, "classloader must not be null");
        Arguments.check(classname != null, "classname must not be null");
        try {
            classloader.loadClass(classname);
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Detects whether a class with given full qualified name is available on the
     * classpath of the current classloader.
     * 
     * @param classname The classname.
     * @return Whether the class is available.
     */
    public static boolean isClassPresent(String classname) {
        return isClassPresent(Classes.class.getClassLoader(), classname);
    }
}
