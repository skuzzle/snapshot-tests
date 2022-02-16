package de.skuzzle.test.snapshots.impl;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.util.List;

import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Execution;

class MetaTest {

    public TestResult expectTestcase(Class<?> testClass) {
        final EngineExecutionResults executionResults = EngineTestKit
                .engine("junit-jupiter")
                .selectors(selectClass(testClass))
                .execute();

        return new TestResult(executionResults, testClass);
    }

    static class TestResult {
        private final EngineExecutionResults executionResults;

        private TestResult(EngineExecutionResults executionResults, Class<?> testClass) {
            this.executionResults = executionResults;
        }

        private Execution onlyExecution() {
            final List<Execution> executions = executionResults.testEvents().executions().list();
            if (executions.size() != 1) {
                throw new IllegalArgumentException("Expected a single execution but found: " + executions);
            }
            return executions.get(0);
        }

        public void toSucceed() throws Throwable {
            final Execution execution = onlyExecution();

            final Status status = execution.getTerminationInfo().getExecutionResult().getStatus();
            if (status != Status.SUCCESSFUL) {
                throw execution.getTerminationInfo().getExecutionResult().getThrowable().get();
            }
        }

        public AbstractThrowableAssert<?, ? extends Throwable> toFailWithExceptionWhich() {
            final Throwable throwable = onlyExecution().getTerminationInfo().getExecutionResult().getThrowable()
                    .orElseThrow(() -> new AssertionError("Expected test to throw an exception but none was thrown"));
            return Assertions.assertThat(throwable);
        }
    }
}
