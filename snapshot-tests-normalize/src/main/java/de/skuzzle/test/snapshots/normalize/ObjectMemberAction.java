package de.skuzzle.test.snapshots.normalize;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * An action that can be applied to a {@link ObjectMember} which is matched by a
 * {@link Predicate}.
 *
 * @author Simon Taddiken
 */
@API(status = Status.EXPERIMENTAL)
public final class ObjectMemberAction {

    private final Predicate<ObjectMember> predicate;
    private final Consumer<ObjectMember> action;

    private ObjectMemberAction(Predicate<ObjectMember> predicate, Consumer<ObjectMember> action) {
        this.predicate = predicate;
        this.action = action;
    }

    public static ChoosePredicateBuilder members() {
        return new ChoosePredicateBuilder();
    }

    Stream<ObjectMember> applyTo(Stream<ObjectMember> stream) {
        return stream.peek(objectMember -> {
            if (predicate.test(objectMember)) {
                action.accept(objectMember);
            }
        });
    }

    public static class ChoosePredicateBuilder {

        public ChooseActionBuilder withValueTypeCompatibleTo(Class<?> type) {
            return where(objectMember -> objectMember.hasTypeCompatibleTo(type));
        }

        public ChooseActionBuilder withValueEqualTo(Object obj) {
            return where(objectMember -> Objects.equals(obj, objectMember.value()));
        }

        public ChooseActionBuilder withStringValueMatching(Pattern pattern) {
            return where(objectMember -> objectMember.hasTypeCompatibleTo(String.class)
                    && objectMember.value() != null
                    && pattern.matcher(objectMember.value().toString()).matches());
        }

        public ChooseActionBuilder withStringValueMatching(String pattern) {
            return withStringValueMatching(Pattern.compile(pattern));
        }

        public ChooseActionBuilder any() {
            return where(objectMember -> true);
        }

        public ChooseActionBuilder where(Predicate<ObjectMember> predicate) {
            return new ChooseActionBuilder(predicate);
        }

    }

    public static class ChooseActionBuilder {

        private final Predicate<ObjectMember> predicate;

        private ChooseActionBuilder(Predicate<ObjectMember> predicate) {
            this.predicate = Arguments.requireNonNull(predicate, "predicate must not be null");
        }

        public ObjectMemberAction consumeWith(Consumer<ObjectMember> action) {
            return new ObjectMemberAction(predicate, Arguments.requireNonNull(action, "action must not be null"));
        }

        public ObjectMemberAction mapValueTo(Function<? super Object, ? extends Object> transformer) {
            return consumeWith(objectMember -> objectMember.setValue(transformer.apply(objectMember.value())));
        }

        public ObjectMemberAction setValueTo(Object value) {
            return consumeWith(objectMember -> objectMember.setValue(value));
        }

        public ObjectMemberAction setValueToNull() {
            return setValueTo(null);
        }

        public ObjectMemberAction setToEmptyValue() {
            return consumeWith(objectMember -> {
                final Object emptyValue = SpecialTypesAndValues.getEmptyValueForType(objectMember.valueType());
                if (emptyValue != null) {
                    objectMember.setValue(emptyValue);
                }
            });
        }

        public ObjectMemberAction removeFromParent() {
            return consumeWith(objectMember -> {
                final Object containerCollection = objectMember.collectionParent().orElse(null);
                if (containerCollection == null) {
                    objectMember.setValue(null);
                } else if (containerCollection instanceof Collection<?>) {
                    ((Collection<?>) containerCollection).remove(objectMember.parent());
                }
            });
        }

        public ObjectMemberAction consistentlyReplaceWith(
                BiFunction<Integer, ? super Object, ? extends Object> generator) {
            final Map<Object, Object> replacements = new HashMap<>();
            return consumeWith(objectMember -> {
                final Object value = objectMember.value();
                final Object replacement = replacements.computeIfAbsent(replacements,
                        key -> generator.apply(replacements.size(), value));
                objectMember.setValue(replacement);
            });
        }
    }
}
