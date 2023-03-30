package de.skuzzle.difftool;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

final class GitLineSeparator {

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

        static String execute(String command) {
            try {
                final Process exec = Runtime.getRuntime().exec(command);
                exec.waitFor();
                try (var err = exec.getErrorStream()) {
                    err.readAllBytes();
                }
                if (exec.exitValue() != 0) {
                    return null;
                }
                try (var in = exec.getInputStream()) {
                    final String output = new String(in.readAllBytes());
                    // https://github.com/skuzzle/snapshot-tests/issues/93
                    // Result comes back with extra line break
                    return trimWhitespaces(output.toLowerCase(Locale.ROOT));
                }
            } catch (Exception e) {
                return null;
            }
        }

        private static final Pattern WHITESPACES = Pattern.compile("\\s+");

        static String trimWhitespaces(String s) {
            return WHITESPACES.matcher(s).replaceAll("");
        }
    }

    private GitLineSeparator() {
        // hidden
    }
}
