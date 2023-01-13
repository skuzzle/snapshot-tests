package de.skuzzle.test.snapshots.junit4;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

// "stolen" from https://stackoverflow.com/a/48759584
interface CombinedRule extends TestRule {
    @Override
    default Statement apply(Statement base, Description description) {
        if (description.isTest()) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    before(description);
                    try {
                        base.evaluate();
                        verify(description);
                    } catch (final AssumptionViolatedException e) {
                        onSkippedTest(description);
                        throw e;
                    } catch (final Throwable e) {
                        onFailedTest(description);
                        throw e;
                    } finally {
                        after(description);
                    }
                }
            };
        }
        if (description.isSuite()) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    beforeClass(description);
                    try {
                        base.evaluate();
                        verifyClass(description);
                    } finally {
                        afterClass(description);
                    }
                }
            };
        }
        return base;
    }

    default void before(Description description) throws Exception {
        // let the implementer decide whether this method is useful to implement
    }

    default void after(Description description) throws Exception {
        // let the implementer decide whether this method is useful to implement
    }

    /**
     * Only runs for Tests that pass
     */
    default void verify(Description description) {
        // let the implementer decide whether this method is useful to implement
    }

    default void beforeClass(Description description) throws Exception {
    }

    default void afterClass(Description description) throws Exception {
    }

    /**
     * Only runs for Suites that pass
     */
    default void verifyClass(Description description) {
    }

    default void onSkippedTest(Description description) {

    }

    default void onFailedTest(Description description) {

    }
}