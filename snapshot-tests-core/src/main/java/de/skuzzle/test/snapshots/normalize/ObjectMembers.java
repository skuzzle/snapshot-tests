package de.skuzzle.test.snapshots.normalize;

import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.normalize.MethodObjectMembers.PropertyConventions;

/**
 * Defines how direct members of an actual object instance are discovered.
 *
 * @author Simon Taddiken
 * @see ObjectTraversal
 */
public interface ObjectMembers {

    static ObjectMembers fieldBased() {
        return FieldObjectMembers.getInstance();
    }

    static ObjectMembers usingJavaBeansConventions() {
        return MethodObjectMembers.usingJavaBeansConventions();
    }

    static ObjectMembers using(Function<Method, PropertyConventions> conventions) {
        return MethodObjectMembers.using(conventions);
    }

    Stream<ObjectMember> directMembersOf(Object root, Object hiddenParent, VisitorContext visitorContext);
}
