package de.skuzzle.test.snapshots.data.json;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Builder for customizing how snapshot files are compared against the actual test result.
 *
 * @author Simon Taddiken
 * @since 1.2.0
 */
@API(status = Status.EXPERIMENTAL, since = "1.2.0")
public interface ComparisonRuleBuilder {

    /**
     * Specify the path for which to apply the custom comparison rule.
     *
     * @param path The dot separated path.
     * @return A DSL Object for specifying how to compare values rooted at that path.
     */
    ChooseMatcher jsonPathAt(String path);

    /**
     * Allows to choose the comparison for the value.
     *
     * @author Simon Taddiken
     */
    @API(status = Status.EXPERIMENTAL, since = "1.2.0")
    public interface ChooseMatcher {

        /**
         * Ignores values during comparison.
         *
         * @return DSL object for specifying further rules.
         */
        ComparisonRuleBuilder ignore();

        /**
         * String representation of the value must match the given regex.
         *
         * @param regex The regex.
         * @return DSL object for specifying further rules.
         */
        ComparisonRuleBuilder mustMatch(Pattern regex);

        /**
         * The value must match the given predicate.
         *
         * @param predicate The predicate.
         * @return DSL object for specifying further rules.
         */
        ComparisonRuleBuilder mustMatch(Predicate<? super Object> predicate);
    }

}
