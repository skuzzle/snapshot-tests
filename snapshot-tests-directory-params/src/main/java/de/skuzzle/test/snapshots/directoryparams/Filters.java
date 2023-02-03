package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Predicate;

final class Filters {

    static TestFileFilter and(TestFileFilter filter1, TestFileFilter filter2) {
        return (testFile, recursive) -> filter1.include(testFile, recursive) && filter2.include(testFile, recursive);
    }

    static Predicate<TestFile> toPredicate(TestFileFilter filter, boolean recursive) {
        return testFile -> {
            try {
                return filter.include(testFile, recursive);
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    static Predicate<TestDirectory> toPredicate(TestDirectoryFilter filter, boolean recursive) {
        return testDirectory -> {
            try {
                return filter.include(testDirectory, recursive);
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    /**
     * Default {@link TestFileFilter} implementation used by {@link FilesFrom} which
     * accepts all files.
     */
    static final class TestFileFilterAll implements TestFileFilter {

        @Override
        public boolean include(TestFile testFile, boolean recursive) throws IOException {
            return true;
        }
    }

    /**
     * Default {@link TestDirectoryFilter} implementation used by {@link DirectoriesFrom}
     * which either accepts all directories (in non-recursive mode) or only leave
     * director.
     */
    static final class TestDirectoryFilterAll implements TestDirectoryFilter {

        @Override
        public boolean include(TestDirectory testDirectory, boolean recursive) throws IOException {
            return recursive
                    ? !testDirectory.hasSubDirectories()
                    : true;
        }
    }
}
