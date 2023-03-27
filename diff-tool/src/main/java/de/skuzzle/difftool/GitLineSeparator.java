package de.skuzzle.difftool;

import java.util.Locale;

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

        String autocrlf() {
            return execute("git config core.autocrlf");
        }

        String eol() {
            return execute("git config core.eol");
        }

        static String execute(String command) {
            try {
                final Process exec = Runtime.getRuntime().exec(command);
                try (var err = exec.getErrorStream()) {
                    err.readAllBytes();
                }
                try (var in = exec.getInputStream()) {
                    return new String(in.readAllBytes());
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    private GitLineSeparator() {
        // hidden
    }
}
