package de.skuzzle.test.snapshots.normalize;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

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
@API(status = Status.EXPERIMENTAL)
public final class ObjectTraversal {

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
        Arguments.requireNonNull(strategy, "strategy must not be null");

        final var context = new VisitorContext();
        return membersOfRecursive(root, null, context, strategy)
                .filter(member -> !context.isTerminal(member.parent())
                        || member instanceof TerminalTypeInCollection)
                .sorted(Comparator.comparing(ObjectMember::name));
    }

    private static Stream<ObjectMember> membersOfRecursive(Object root, Object collectionParent, VisitorContext context,
            ObjectMembers strategy) {
        return Stream.of(root)
                .flatMap(start -> membersOf(start, collectionParent, context, strategy))
                .flatMap(member -> Stream.concat(
                        Stream.of(member),
                        membersOfRecursive(member.value(), collectionParent, context, strategy)));
    }

    private static Stream<ObjectMember> membersOf(Object root, Object collectionParent, VisitorContext context,
            ObjectMembers strategy) {
        if (root == null || !context.alreadyVisisted(root)) {
            return Stream.empty();

        } else if (root instanceof Collection<?>) {
            final Collection<?> c = (Collection<?>) root;
            return c.stream().flatMap(element -> membersOfRecursive(element, c, context, strategy));

        } else if (root.getClass().isArray()) {
            final Object[] c = (Object[]) root;
            return Arrays.stream(c).flatMap(element -> membersOfRecursive(element, c, context, strategy));

        } else if (root instanceof Iterable<?>) {
            final Iterable<?> it = (Iterable<?>) root;
            final Spliterator<?> spliterator = it.spliterator();
            return StreamSupport.stream(spliterator, false)
                    .flatMap(element -> membersOfRecursive(element, spliterator, context, strategy));

        } else if (root instanceof Map<?, ?>) {
            final Map<?, ?> map = (Map<?, ?>) root;
            return Stream.concat(
                    membersOfRecursive(map.keySet(), collectionParent, context, strategy),
                    membersOfRecursive(map.values(), collectionParent, context, strategy));

        } else if (context.isTerminal(root)) {
            if (collectionParent != null) {
                return Stream.of(new TerminalTypeInCollection(root, collectionParent));
            }
            return Stream.empty();
        }

        return strategy.directMembersOf(root, collectionParent, context);
    }

    private static final class TerminalTypeInCollection implements ObjectMember {

        private final Object value;
        private final Object collectionParent;

        public TerminalTypeInCollection(Object value, Object collectionParent) {
            this.value = Arguments.requireNonNull(value);
            this.collectionParent = Arguments.requireNonNull(collectionParent);
        }

        @Override
        public Object parent() {
            return collectionParent;
        }

        @Override
        public Optional<Object> collectionParent() {
            return Optional.of(collectionParent);
        }

        @Override
        public String name() {
            return value.toString();
        }

        @Override
        public Class<?> valueType() {
            return value.getClass();
        }

        @Override
        public Object value() {
            return value;
        }

        @Override
        public void setValue(Object value) {

        }

        @Override
        public boolean isReadonly() {
            return true;
        }

        @Override
        public boolean isWriteOnly() {
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(System.identityHashCode(value), System.identityHashCode(collectionParent));
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof TerminalTypeInCollection
                    && value == ((TerminalTypeInCollection) obj).value
                    && collectionParent == ((TerminalTypeInCollection) obj).collectionParent;
        }

        @Override
        public String toString() {
            return String.format("Terminal in %s->[%s]%s: %s",
                    collectionParent.getClass().getSimpleName(),
                    valueType().getName(), name(), "" + value());
        }
    }
}
