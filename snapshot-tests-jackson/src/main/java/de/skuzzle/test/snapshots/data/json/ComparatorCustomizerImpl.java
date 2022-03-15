package de.skuzzle.test.snapshots.data.json;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.LocationAwareValueMatcher;
import org.skyscreamer.jsonassert.ValueMatcherException;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;

import de.skuzzle.test.snapshots.validation.Arguments;

final class ComparatorCustomizerImpl implements ComparisonRuleBuilder {

    private final List<Customization> customizations = new ArrayList<>();

    @Override
    public ChooseMatcher pathAt(String path) {

        return new ChooseMatcher() {

            @Override
            public ComparisonRuleBuilder mustMatch(Pattern regex) {
                Arguments.requireNonNull(regex, "regex must not be null");
                customizations
                        .add(new Customization(path, new LocationAwareValueMatcher<>() {

                            @Override
                            public boolean equal(Object o1, Object o2) {
                                throw new UnsupportedOperationException();
                            }

                            @Override
                            public boolean equal(String prefix, Object actual, Object snapshot,
                                    JSONCompareResult result)
                                    throws ValueMatcherException {

                                final boolean match = actual != null && regex.matcher(actual.toString()).matches()
                                        && snapshot != null && regex.matcher(snapshot.toString()).matches();
                                if (!match) {
                                    result.fail(String.format(
                                            "Expected actual value '%s' of field '%s'  to match the pattern '%s'",
                                            actual.toString(), prefix, regex.toString()));
                                }
                                return match;
                            }
                        }));
                return ComparatorCustomizerImpl.this;
            }

            @Override
            public ComparisonRuleBuilder ignore() {
                customizations.add(new Customization(path, (o1, o2) -> true));
                return ComparatorCustomizerImpl.this;
            }

            @Override
            public ComparisonRuleBuilder mustMatch(Predicate<? super Object> predicate) {
                Arguments.requireNonNull(predicate, "predicate must not be null");
                customizations.add(new Customization(path, (o1, o2) -> predicate.test(o1) && predicate.test(o2)));
                return ComparatorCustomizerImpl.this;
            }
        };
    }

    public JSONComparator build() {
        return new CustomComparator(JSONCompareMode.STRICT, customizations.toArray(Customization[]::new));
    }

}
