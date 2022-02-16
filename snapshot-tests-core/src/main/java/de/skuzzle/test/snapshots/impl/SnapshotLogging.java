package de.skuzzle.test.snapshots.impl;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

/**
 * Minimal internal logging abstraction that helps us with testing. By default, simply
 * delegates to <code>System.getLogger</code>.
 *
 * @author Simon Taddiken
 * @since 1.1.0
 */
@API(status = Status.INTERNAL, since = "1.1.0")
final class SnapshotLogging {

    private static final SnapshotLoggerFactory DEFAULT_FACTORY = new JdkSnapshotLoggerFactory();
    private static volatile SnapshotLoggerFactory FACTORY = DEFAULT_FACTORY;

    static SnapshotLogger getLogger(Class<?> type) {
        return FACTORY.getLogger(type);
    }

    static void setFactory(SnapshotLoggerFactory factory) {
        FACTORY = Objects.requireNonNull(factory, "factory must not be null");
    }

    static void resetFactory() {
        setFactory(DEFAULT_FACTORY);
    }

    @API(status = Status.INTERNAL, since = "1.1.0")
    interface SnapshotLoggerFactory {

        SnapshotLogger getLogger(Class<?> type);
    }

    @API(status = Status.INTERNAL, since = "1.1.0")
    interface SnapshotLogger {
        void info(String message, Object... params);

        void warn(String message, Object... params);
    }

    private static final class JdkSnapshotLoggerFactory implements SnapshotLoggerFactory {

        @Override
        public SnapshotLogger getLogger(Class<?> type) {
            return new SnapshotLogger() {

                private final Logger JDK_LOGGER = System.getLogger(type.getName());

                @Override
                public void warn(String message, Object... params) {
                    JDK_LOGGER.log(Level.WARNING, message, params);
                }

                @Override
                public void info(String message, Object... params) {
                    JDK_LOGGER.log(Level.WARNING, message, params);

                }
            };
        }

    }
}
