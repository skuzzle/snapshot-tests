package de.skuzzle.test.snapshots.normalize;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Provides reflection based {@link ObjectMember} implementation that consists of a
 * matching pair of a getter and a setter methods. Getter and Setter methods are matched
 * using an implementation of {@link PropertyConventions}. A default implementation is
 * provided which follows the well known java beans convention for naming properties.
 *
 * @author Simon Taddiken
 */
class MethodObjectMembers implements ObjectMembers {

    private final Function<Method, PropertyConventions> conventionsConstructor;

    private MethodObjectMembers(Function<Method, PropertyConventions> conventionsConstructor) {
        this.conventionsConstructor = Arguments.requireNonNull(conventionsConstructor,
                "conventionsConstructor must not be null");
    }

    public static ObjectMembers usingJavaBeansConventions() {
        return new MethodObjectMembers(JavaBeansPropertyConventions::new);
    }

    public static ObjectMembers using(Function<Method, PropertyConventions> conventionsConstructor) {
        return new MethodObjectMembers(conventionsConstructor);
    }

    @Override
    public Stream<ObjectMember> directMembersOf(Object root, Object collectionParent, VisitorContext visitorContext) {
        final Class<? extends Object> valueType = root.getClass();
        return Reflection.superClassHierarchy(valueType)
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .map(conventionsConstructor)
                .filter(PropertyConventions::includeInTraversal)
                .collect(Collectors.groupingBy(PropertyConventions::canonicalName,
                        toMethodObjectMember(root, collectionParent)))
                .values().stream()
                .map(ObjectMember.class::cast);
    }

    public interface PropertyConventions {
        Method method();

        boolean isGetter();

        boolean isSetter();

        default boolean includeInTraversal() {
            return isGetter() || isSetter();
        }

        String canonicalName();
    }

    private static Collector<PropertyConventions, ?, MethodObjectMember> toMethodObjectMember(Object root,
            Object collectionParent) {
        final class GetterAndSetter {
            private String canonicalName;
            private Method getter;
            private Method setter;
        }

        return Collector.of(GetterAndSetter::new,
                (getterAndSetter, property) -> {
                    getterAndSetter.canonicalName = property.canonicalName();
                    if (property.isGetter())
                        getterAndSetter.getter = property.method();
                    else
                        getterAndSetter.setter = property.method();
                },
                (gas1, gas2) -> gas1,
                gas -> new MethodObjectMember(gas.canonicalName, root, collectionParent, gas.getter, gas.setter));
    }

    private static class JavaBeansPropertyConventions implements PropertyConventions {
        private final Method method;

        public JavaBeansPropertyConventions(Method method) {
            this.method = method;
        }

        @Override
        public Method method() {
            return this.method;
        }

        @Override
        public boolean isGetter() {
            final var name = method.getName();
            return method.getParameterCount() == 0 &&
                    (name.startsWith("get") && name.length() > 3 && !name.equals("getClass") ||
                            name.startsWith("is") && name.length() > 2);
        }

        @Override
        public boolean isSetter() {
            final var name = method.getName();
            return method.getParameterCount() == 1 &&
                    name.startsWith("set") && name.length() > 3;
        }

        private int prefixLength() {
            final var name = method.getName();
            return name.startsWith("is")
                    ? 2
                    : 3;
        }

        @Override
        public String canonicalName() {
            return lowerCaseFirstChar(method.getName().substring(prefixLength()));
        }

        private String lowerCaseFirstChar(String s) {
            final StringBuilder builder = new StringBuilder(s);
            builder.setCharAt(0, Character.toLowerCase(s.charAt(0)));
            return builder.toString();
        }
    }

    private static final class MethodObjectMember implements ObjectMember {

        private final String name;
        private final Object parent;
        private final Object collectionParent;

        // At least one of these are non-null
        private final Method getter;
        private final Method setter;

        private MethodObjectMember(String name, Object parent, Object collectionParent, Method getter, Method setter) {
            this.name = Arguments.requireNonNull(name, "member name must not be null");
            this.parent = Arguments.requireNonNull(parent, "member's parent must not be null");
            this.collectionParent = collectionParent;

            this.getter = getter;
            this.setter = setter;
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
        public String name() {
            return name;
        }

        @Override
        public Class<?> valueType() {
            if (getter != null) {
                return getter.getReturnType();
            } else {
                return setter.getParameterTypes()[0];
            }
        }

        @Override
        public Object value() {
            if (isWriteOnly()) {
                // Note: As last resort we could try to find and read the corresponding
                // field instead of throwing directly
                /*
                 * throw new UnsupportedOperationException(String.format(
                 * "Could not read value of property '%s' because no getter was found on parent object '%s'"
                 * , name, parent));
                 */
                return null;
            }
            try {
                getter.setAccessible(true);
                return getter.invoke(parent);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new UnsupportedOperationException(
                        String.format("Could not read value of getter '%s' from object '%s'", getter, parent), e);
            }
        }

        @Override
        public void setValue(Object value) {
            if (isReadonly()) {
                // Note: As last resort we could try to find and set the corresponding
                // field instead of throwing directly
                /*
                 * throw new UnsupportedOperationException(String.format(
                 * "Could not set value of property '%s' because no setter was found on parent object '%s'"
                 * , name, parent));
                 */
                return;
            }

            try {
                setter.setAccessible(true);
                setter.invoke(parent, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new UnsupportedOperationException(
                        String.format("Could not set value of setter '%s' on object '%s' to '%s'",
                                setter, parent, value),
                        e);
            }
        }

        @Override
        public boolean isReadonly() {
            return setter == null;
        }

        @Override
        public boolean isWriteOnly() {
            return getter == null;
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, System.identityHashCode(parent));
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj instanceof MethodObjectMember
                    && parent == ((MethodObjectMember) obj).parent
                    && Objects.equals(name, ((MethodObjectMember) obj).name);
        }

        @Override
        public String toString() {
            return String.format("%s->[%s]%s: %s", parent.getClass().getSimpleName(),
                    valueType().getName(), name(), "" + value());
        }
    }

}
