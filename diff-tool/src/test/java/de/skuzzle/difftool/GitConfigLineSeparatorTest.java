package de.skuzzle.difftool;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GitConfigLineSeparatorTest {

    static class TestGitConfig extends GitLineSeparator.GitConfig {
        private final String autocrlf;
        private final String eol;

        TestGitConfig(String autocrlf, String eol) {
            this.autocrlf = autocrlf;
            this.eol = eol;
        }

        @Override
        public String autocrlf() {
            return autocrlf;
        }

        @Override
        public String eol() {
            return eol;
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null,null,SYSTEM",
            "true, null, CRLF",
            "input,null,LF",
            "false,crlf,CRLF",
            "false,lf,LF",
            "false,native,SYSTEM",
            "false,null,SYSTEM"
    })
    void determineLineSeparatorFromGitConfig(String autocrlf, String eol, LineSeparator expected) {
        final TestGitConfig git = new TestGitConfig(autocrlf, eol);
        LineSeparator gitLineSeparator = GitLineSeparator.determineGitLineSeparator(git);
        assertThat(gitLineSeparator).isEqualTo(expected);
    }

}
