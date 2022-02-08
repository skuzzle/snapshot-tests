package de.skuzzle.test.snapshots.normalize;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

class FieldObjectMembers implements ObjectMembers {

    private static final ObjectMembers INSTANCE = new FieldObjectMembers();

    private FieldObjectMembers() {
        // hidden
    }

    public static ObjectMembers getInstance() {
        return INSTANCE;
    }

    @Override
    public Stream<ObjectMember> directMembersOf(Object root, Object collectionParent, VisitorContext visitorContext) {
        final Class<? extends Object> valueType = root.getClass();
        return Reflection.superClassHierarchy(valueType)
                .map(Class::getDeclaredFields)
                .flatMap(Arrays::stream)
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(field -> new FieldObjectMember(field, root, collectionParent));
    }

    private static final class FieldObjectMember implements ObjectMember {

        private final Object parent;
        private final Object collectionParent;
        private final Field field;

        public FieldObjectMember(Field field, Object parent, Object collectionParent) {
            this.parent = parent;
            this.collectionParent = collectionParent;
            this.field = field;
        }

        @Override
        public Object parent() {
            return parent;
        }

        @Override
        public Optional<Object> collectionParent() {
            return Optional.ofNullable(collectionParent);
        }

        @Override
        public Class<?> valueType() {
            return field.getType();
        }

        @Override
        public boolean hasTypeCompatibleTo(Class<?> type) {
            return type.isAssignableFrom(valueType());
        }

        @Override
        public String name() {
            return field.getName();
        }

        @Override
        public Object value() {
            try {
                field.setAccessible(true);
                return field.get(parent);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new UnsupportedOperationException(
                        String.format("Could not read value of field '%s' from object '%s'", field, parent), e);
            }
        }

        @Override
        public void setValue(Object value) {
            if (isReadonly()) {
                return;
            }
            try {
                field.setAccessible(true);
                field.set(parent, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new UnsupportedOperationException(
                        String.format("Could not set value of field '%s' on object '%s' to '%s'", field, parent, value),
                        e);
            }
        }

        @Override
        public boolean isReadonly() {
            return Modifier.isFinal(field.getModifiers());
        }

        @Override
        public boolean isWriteOnly() {
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(field, System.identityHashCode(parent));
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof FieldObjectMember
                    && parent == ((FieldObjectMember) obj).parent
                    && Objects.equals(field, ((FieldObjectMember) obj).field);
        }

        @Override
        public String toString() {
            return String.format("%s->[%s]%s: %s", parent.getClass().getSimpleName(),
                    valueType().getName(), name(), "" + value());
        }
    }

}
