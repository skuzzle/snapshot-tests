package de.skuzzle.test.snapshots.data.json;

import java.util.regex.Pattern;

public interface ComparatorCustomizer {

    ChooseMatcher jsonPathAt(String path);

    public interface ChooseMatcher {

        ComparatorCustomizer ignore();

        ComparatorCustomizer mustMatch(Pattern regex);

    }

}
