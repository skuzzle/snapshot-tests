package de.skuzzle.test.snapshots.directoryparams;

/**
 * 
 * @author Simon Taddiken
 * @since 1.9.0
 */
final class DefaultIsTestCaseDirectoryStrategy implements IsTestCaseDirectoryStrategy {

    @Override
    public boolean determineIsTestCaseDirectory(TestDirectory directory) {
        return !directory.hasSubDirectories();
    }

}
