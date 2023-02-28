package de.skuzzle.test.snapshots.normalize;

import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.normalize.MethodObjectMembers.PropertyConventions;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Defines how direct members of an actual object instance are discovered. Use any of the
 * static factories in this interface to obtain an instance.
 *
 * @author Simon Taddiken
 * @see ObjectTraversal
 * @apiNote This interface is meant for internal implementations only.
 */
@API(status = Status.EXPERIMENTAL)
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

    /**
     * Internal API method that is used to determine the direct members of the given root
     * object without recursing into children.
     *
     * @param root The root object to inspect.
     * @param collectionParent If the root object was discovered within a collection, this
     *            is a reference to the collection.
     * @param visitorContext Context information for the current traversal.
     * @return Stream of direct children.
     */
    Stream<ObjectMember> directMembersOf(Object root, Object collectionParent, VisitorContext visitorContext);
}
