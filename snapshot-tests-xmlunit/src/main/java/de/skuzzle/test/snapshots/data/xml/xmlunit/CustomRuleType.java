package de.skuzzle.test.snapshots.data.xml.xmlunit;

enum CustomRuleType {
    /**
     * XPaths for ignore type rules are only evaluated when a mismatched has been found.
     * Semantics are that those rules can never upgrade a match to a mismatch. Thus those
     * rule type allow for extra optimizations.
     */
    IGNORE,

    /**
     * For comparison rules that perform custom matching. Semantics are that those rules
     * can either downgrade a mismatch to a match or upgrade a match to a mismatch.
     */
    CUSTOM_MATCH;
}
