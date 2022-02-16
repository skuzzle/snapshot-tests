package de.skuzzle.test.snapshots.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.assertj.core.api.AbstractStringAssert;
import org.opentest4j.AssertionFailedError;

import de.skuzzle.test.snapshots.impl.SnapshotLogging.SnapshotLogger;
import de.skuzzle.test.snapshots.impl.SnapshotLogging.SnapshotLoggerFactory;

/**
 * Replacecs the {@link SnapshotLogger} with a static instance that is able to collect log
 * events and perform assertions on them.
 * <p>
 * Exchanging the Logger is NOT thread safe and should thus not be used in parallel tests.
 *
 * @author Simon Taddiken
 */
class MockSnapshotLoggerFactory implements SnapshotLoggerFactory {

    private MockSnapshotLoggerFactory() {
        // hidden
    }

    @Override
    public MockSnapshotLogger getLogger(Class<?> type) {
        return MockSnapshotLogger.INSTANCE;
    }

    public static MockSnapshotLogger install() {
        final MockSnapshotLoggerFactory factory = new MockSnapshotLoggerFactory();
        SnapshotLogging.setFactory(factory);

        MockSnapshotLogger.INSTANCE.loggedMessages.clear();
        return MockSnapshotLogger.INSTANCE;
    }

    public static void uninstall() {
        SnapshotLogging.resetFactory();
    }

    public static final class MockSnapshotLogger implements SnapshotLogger {

        private static final MockSnapshotLogger INSTANCE = new MockSnapshotLogger();

        private final List<LoggedMessage> loggedMessages = new ArrayList<>();

        private MockSnapshotLogger() {
            // hidden
        }

        public List<LoggedMessage> getLoggedMessages() {
            return Collections.unmodifiableList(this.loggedMessages);
        }

        public LoggedMessage assertContainsExactlyOneEventWhere(Predicate<LoggedMessage> test) {
            final List<LoggedMessage> findings = loggedMessages.stream()
                    .filter(test)
                    .collect(Collectors.toList());
            if (findings.isEmpty()) {
                throw new AssertionFailedError(
                        "Expected to contain exactly one logged message with given predicate but none was found");
            } else if (findings.size() > 1) {
                throw new AssertionFailedError(
                        "Expected to contain exactly one logged message with given predicate but found "
                                + findings.size() + ":\n" + findings);
            }
            return findings.get(0);
        }

        public void assertContainsNoEventWhere(Predicate<LoggedMessage> test) {
            loggedMessages.stream()
                    .filter(test)
                    .findFirst()
                    .ifPresent(event -> {
                        throw new AssertionFailedError(
                                "Expected to contain no logged message with given predicate but at least one was found: \n"
                                        + event);
                    });
        }

        @Override
        public void info(String message, Object... params) {
            loggedMessages.add(new LoggedMessage("info", message, params));
        }

        @Override
        public void warn(String message, Object... params) {
            loggedMessages.add(new LoggedMessage("warn", message, params));
        }

    }

    public static final class LoggedMessage {
        private final String level;
        private final String message;
        private final Object[] params;

        private LoggedMessage(String level, String message, Object[] params) {
            this.level = level;
            this.message = message;
            this.params = params;
        }

        public String getLevel() {
            return this.level;
        }

        public String getMessage() {
            return this.message;
        }

        public Object[] getParams() {
            return this.params;
        }

        public LoggedMessage assertMessageMatches(Consumer<AbstractStringAssert<?>> message) {
            final AbstractStringAssert<?> assertThat2 = assertThat(this.message);
            message.accept(assertThat2);
            return this;
        }

        public boolean hasLevel(String level) {
            return level.equals(this.level);
        }

        public boolean containsParam(Object param) {
            return Arrays.asList(params).contains(param);
        }

        public boolean containsParamWhere(Predicate<Object> test) {
            return Arrays.stream(params).anyMatch(test);
        }

        public boolean messageMatches(Predicate<String> predicate) {
            return predicate.test(message);
        }

        @Override
        public String toString() {
            return level + ": " + message + "\n" + Arrays.toString(params);
        }
    }
}
