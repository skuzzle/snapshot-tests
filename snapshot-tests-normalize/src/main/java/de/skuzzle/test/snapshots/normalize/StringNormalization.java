package de.skuzzle.test.snapshots.normalize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

import de.skuzzle.test.snapshots.validation.Arguments;

/**
 * Simply collects multiple string modifications (for example as retrieved from
 * {@link Strings} class) and applies all of them in order to a String.
 *
 * @author Simon Taddiken
 * @since 1.3.0
 */
@API(status = Status.EXPERIMENTAL, since = "1.3.0")
public final class StringNormalization implements Function<String, String> {

    private final List<Function<String, String>> modifications = new ArrayList<>();

    private StringNormalization(Collection<? extends Function<String, String>> modifications) {
        this.modifications.addAll(modifications);
    }

    @SafeVarargs
    public static StringNormalization withModifications(Function<String, String>... modifications) {
        Arguments.requireNonNull(modifications, "modifications must not be null");
        return new StringNormalization(Arrays.asList(modifications));
    }

    public static StringNormalization withModifications(Collection<? extends Function<String, String>> modifications) {
        Arguments.requireNonNull(modifications, "modifications must not be null");
        return new StringNormalization(modifications);
    }

    public StringNormalization addModification(Function<String, String> modification) {
        this.modifications.add(Arguments.requireNonNull(modification, "modification must not be null"));
        return this;
    }

    @Override
    public String apply(String s) {
        String s1 = Arguments.requireNonNull(s, "string must not be null");
        for (final Function<String, String> mod : modifications) {
            s1 = mod.apply(s1);
        }
        return s1;
    }

}
