package de.skuzzle.difftool;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class GitLineSeparator {

    private static final boolean gitEolDebugging;
    static {
        gitEolDebugging = System.getProperties().keySet().stream().map(Object::toString)
                .anyMatch("giteoldebugging"::equalsIgnoreCase);
    }

    static final LineSeparator GIT_LINE_SEPARATOR = determineGitLineSeparator(GitConfig.DEFAULT);

    static LineSeparator determineGitLineSeparator(GitConfig gitConfig) {
        final String autocrlf = gitConfig.autocrlf();
        if (autocrlf == null) {
            return LineSeparator.SYSTEM;
        }
        switch (autocrlf.toLowerCase(Locale.ROOT)) {
        case "true":
            return LineSeparator.CRLF;
        case "input":
            return LineSeparator.LF;
        case "false":
            final String eol = gitConfig.eol();
            if (eol == null) {
                return LineSeparator.SYSTEM;
            }
            switch (eol.toLowerCase(Locale.ROOT)) {
            case "crlf":
                return LineSeparator.CRLF;
            case "lf":
                return LineSeparator.LF;
            case "native":
            default:
                return LineSeparator.SYSTEM;
            }
        }
        return LineSeparator.SYSTEM;
    }

    static class GitConfig {

        static final GitConfig DEFAULT = new GitConfig();

        private final String fileParameter;

        GitConfig() {
            this(null);
        }

        // Used for testing
        GitConfig(String fileParameter) {
            this.fileParameter = fileParameter;
        }

        String autocrlf() {
            final String gitConfigCommand = makeCommand("git", "config", fileParameter, "core.autocrlf");
            return execute(gitConfigCommand);
        }

        void setAutoCrlf(String value) {
            final String gitConfigCommand = makeCommand("git config", fileParameter, "core.autocrlf", value);
            execute(gitConfigCommand);
        }

        String eol() {
            final String gitConfigCommand = makeCommand("git", "config", fileParameter, "core.eol");
            return execute(gitConfigCommand);
        }

        private String makeCommand(String... parts) {
            return Arrays.stream(parts).filter(Objects::nonNull).collect(Collectors.joining(" "));
        }

        private String execute(String command) {
            final GitCallResult result = executeInternal(command);
            if (gitEolDebugging) {
                System.err.println("GitEolDebugging:");
                System.err.println(result);
            }
            return result.result();
        }

        private static GitCallResult executeInternal(String command) {
            String systemErr = "<empty>";
            String systemOut = "<empty>";
            int exitCode = 0;
            Exception exception = null;
            try {
                final Process exec = Runtime.getRuntime().exec(command);
                exec.waitFor();
                try (var err = exec.getErrorStream()) {
                    final String output = new String(err.readAllBytes());
                    systemErr = output.isEmpty() ? systemErr : output;
                }
                exitCode = exec.exitValue();
                try (var in = exec.getInputStream()) {
                    final String output = new String(in.readAllBytes());
                    systemOut = output.isEmpty() ? systemOut : output;

                }
            } catch (Exception e) {
                exception = e;
            }
            return new GitCallResult(command, exitCode, systemOut, systemErr, exception);
        }

        private static final class GitCallResult {

            private final String command;
            private final int exitCode;
            private final String systemOut;
            private final String systemErr;
            private final Exception exception;

            private GitCallResult(String command, int exitCode, String systemOut, String systemErr,
                    Exception exception) {
                this.command = command;
                this.exitCode = exitCode;
                this.systemOut = trimWhitespaces(systemOut);
                this.systemErr = trimWhitespaces(systemErr);
                this.exception = exception;
            }

            private static final Pattern WHITESPACES = Pattern.compile("\\s+");

            private static String trimWhitespaces(String s) {
                return WHITESPACES.matcher(s).replaceAll("");
            }

            public String result() {
                if (exitCode != 0 || exception != null) {
                    return null;
                }
                return systemOut;
            }

            @Override
            public String toString() {
                final StringBuilder b = new StringBuilder();
                b.append("Result of '").append(command).append("': ").append(exitCode).append(System.lineSeparator())
                        .append("Error Output: ").append(systemErr).append(System.lineSeparator())
                        .append("System Output: ").append(systemOut).append(System.lineSeparator());
                if (exception == null) {
                    b.append("Exception: <none>");
                } else {
                    b.append("Exception: ").append(exception.getMessage());
                }
                return b.toString();
            }
        }
    }

    private GitLineSeparator() {
        // hidden
    }
}
