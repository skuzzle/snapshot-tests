package de.skuzzle.test.snapshots.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import de.skuzzle.test.snapshots.reflection.StackTraces.Caller;

import org.junit.jupiter.api.Test;

class StackTracesTest {

    @Test
    void testGetCaller() {
        final Caller caller = methodThatWantsToKnowItsCaller();
        assertThat(caller.toString()).isEqualTo(
                "de.skuzzle.test.snapshots.reflection.StackTracesTest.testGetCaller(StackTracesTest.java:13)");
    }

    private Caller methodThatWantsToKnowItsCaller() {
        return StackTraces.findImmediateCaller();
    }

}
