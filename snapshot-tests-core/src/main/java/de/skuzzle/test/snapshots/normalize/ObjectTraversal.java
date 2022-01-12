package de.skuzzle.test.snapshots.normalize;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Allows to recursively iterate all members of an actual object. Each member is wrapped
 * into a {@link ObjectMember} instance from which it can be read/modified. How members
 * are discovered is defined by the {@link ObjectMembers} strategy interface.
 * <p>
 * Regardless of the used strategy, certain types are always handled specially when
 * discovered while iterating an object's members:
 * <ul>
 * <li>We never recurse into types of the packages <code>java.*</code> or
 * <code>javax.*</code></li>
 * <li>As such, we never recurse into primitive wrappers</li>
 * <li>When discovering a {@link Collection} type, an {@link Iterable} type or an array
 * type, we do not recurse into the actual type but iterate the contained elements
 * instead</li>
 * </ul>
 * <p>
 * The actual order of traversal is undefined but deterministic.
 * <p>
 * Traversal gracefully handles cycles within the object graph. Every actual encountered
 * object instance is only visited once, regardless of how often it is referenced within
 * the object graph.
 *
 * @author Simon Taddiken
 * @see ObjectMember
 * @see ObjectMembers
 */
public final class ObjectTraversal {

    // private static final Set<Class<?>> PRIMITIVES = Set.of(
    // Boolean.class,
    // Boolean.TYPE,
    // Byte.class,
    // Byte.TYPE,
    // Short.class,
    // Short.TYPE,
    // Integer.class,
    // Integer.TYPE,
    // Long.class,
    // Long.TYPE,
    // Float.class,
    // Float.TYPE,
    // Double.class,
    // Double.TYPE);

    /**
     * Creates a lazily populated Stream of {@link ObjectMember object members} that are
     * recursively reachable from the given root object.
     *
     * @param root Root object from which members shall be discovered - may be null.
     * @param strategy Strategy for discovering members. Can be obtained from static
     *            factories in {@link ObjectMembers} itself.
     * @return Stream of members.
     */
    public static Stream<ObjectMember> members(Object root, ObjectMembers strategy) {
        Objects.requireNonNull(strategy, "strategy must not be null");

        final var context = new VisitorContext();
        return membersOf(root, root, context, strategy)
                .sorted(Comparator.comparing(ObjectMember::name))
                .flatMap(member -> Stream.concat(
                        Stream.of(member),
                        membersOf(member.value(), null, context, strategy)
                                .sorted(Comparator.comparing(ObjectMember::name))));
    }

    public static void applyActions(Object root, ObjectMembers strategy, ObjectMemberAction... actions) {
        applyActions(root, strategy, Arrays.asList(actions));
    }

    public static void applyActions(Object root, ObjectMembers strategy, Collection<ObjectMemberAction> actions) {
        Stream<ObjectMember> members = members(root, strategy);

        for (final ObjectMemberAction action : actions) {
            members = action.applyTo(members);
        }
        members.forEach(action -> {
        });
    }

    private static Stream<ObjectMember> membersOf(Object root, Object collectionParent, VisitorContext context,
            ObjectMembers strategy) {
        if (root == null || !context.addVisitedInstance(root)) {
            return Stream.empty();
        } else if (root instanceof Collection<?>) {
            final Collection<?> c = (Collection<?>) root;
            return c.stream().flatMap(element -> membersOf(element, c, context, strategy));
        } else if (root.getClass().isArray()) {
            final Object[] c = (Object[]) root;
            return Arrays.stream(c).flatMap(element -> membersOf(element, c, context, strategy));
        } else if (root instanceof Iterable<?>) {
            final Iterable<?> it = (Iterable<?>) root;
            final Spliterator<?> spliterator = it.spliterator();
            return StreamSupport.stream(spliterator, false)
                    .flatMap(element -> membersOf(element, spliterator, context, strategy));
        } else if (!shouldRecurse(root)) {
            return Stream.empty();
        }

        return strategy.directMembersOf(root, collectionParent, context);
    }

    static boolean shouldRecurse(Object root) {
        return !(root.getClass().getPackageName().startsWith("java.")
                || root.getClass().getPackageName().startsWith("javax."));
    }

}
