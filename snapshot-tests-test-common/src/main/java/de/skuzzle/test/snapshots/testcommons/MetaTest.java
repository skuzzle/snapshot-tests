package de.skuzzle.test.snapshots.testcommons;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.apiguardian.api.API;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Execution;

@API(status = API.Status.INTERNAL, since = "1.8.0")
public final class MetaTest {

    private static final String JUNIT4_ENGINE = "junit-vintage";
    private static final String JUNIT5_ENGINE = "junit-jupiter";

    private final String engineName;

    private MetaTest(String engineName) {
        this.engineName = engineName;
    }

    public static MetaTest junit4() {
        return new MetaTest(JUNIT4_ENGINE);
    }

    public static MetaTest junit5() {
        return new MetaTest(JUNIT5_ENGINE);
    }

    public void executeTestcasesIn(Class<?> testClass) {
        expectTestcase(testClass);
    }

    public TestResult expectTestcase(Class<?> testClass) {
        final EngineExecutionResults executionResults = EngineTestKit
                .engine(engineName)
                .selectors(selectClass(testClass))
                .execute();

        return new TestResult(executionResults, testClass);
    }

    public static void assumeMetaTest() {
        final boolean isMetaTest = containedInStacktrace();
        Assumptions.assumeThat(isMetaTest)
                .as("This test cas will be executed as meta-test case")
                .isTrue();
    }

    private static boolean containedInStacktrace() {
        return StackWalker.getInstance().walk(stack -> stack
                .anyMatch(stackFrame -> stackFrame.getClassName().equals(MetaTest.class.getName())
                        && stackFrame.getMethodName().equals("expectTestcase")
                        || stackFrame.getMethodName().equals("executeTestcasesIn")));
    }

    public static class TestResult {
        private final EngineExecutionResults executionResults;

        private TestResult(EngineExecutionResults executionResults, Class<?> testClass) {
            this.executionResults = executionResults;
        }

        private Collection<Execution> allExecutions() {
            final List<Execution> executions = executionResults.testEvents().executions().list();
            if (executions.isEmpty()) {
                throw new IllegalArgumentException("Expected a single execution but found: " + executions);
            }
            return executions;
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
            final Execution onlyExecution = onlyExecution();
            return expectExecutionToFail(onlyExecution);
        }

        private AbstractThrowableAssert<?, ? extends Throwable> expectExecutionToFail(Execution execution) {
            final Throwable throwable = execution.getTerminationInfo().getExecutionResult().getThrowable()
                    .orElseThrow(() -> new AssertionError("Expected test to throw an exception but none was thrown"));
            return Assertions.assertThat(throwable);
        }

        public void toAllFailWithExceptionWhich(Consumer<AbstractThrowableAssert<?, ? extends Throwable>> matches) {
            allExecutions().forEach(execution -> {
                final AbstractThrowableAssert<?, ? extends Throwable> throwableAssert = expectExecutionToFail(
                        execution);
                matches.accept(throwableAssert);
            });
        }
    }
}
