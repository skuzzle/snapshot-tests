package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.support.ReflectionSupport;

class DirectoriesFromArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<DirectoriesFrom> {

    private DirectoriesFrom directoryContents;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return directoryContents.recursive()
                ? provideArgumentsRecursive(context)
                : provideArgumentsFlat(context);
    }

    private Stream<? extends Arguments> provideArgumentsFlat(ExtensionContext context) throws Exception {
        final Path inputFileDirectory = determineDirectory().toAbsolutePath().toRealPath();
        final PathFilter filter = PathFilter.fromPredicate(Files::isDirectory)
                .and(additionalFilter());

        return Files.list(inputFileDirectory)
                .filter(filter.toPredicate())
                .map(TestDirectory::new)
                .sorted(Comparator.comparing(TestDirectory::name))
                .map(Arguments::of);
    }

    private Stream<? extends Arguments> provideArgumentsRecursive(ExtensionContext context) throws Exception {
        final Path inputFileDirectory = determineDirectory().toAbsolutePath().toRealPath();
        final PathFilter filter = PathFilter.fromPredicate(Files::isDirectory)
                .and(additionalFilter());

        final IsTestCaseDirectoryStrategy testCaseDirectoryStrategy = isTestCaseDirectoryStrategy();

        return Files.walk(inputFileDirectory)
                .filter(filter.toPredicate())
                .map(TestDirectory::new)
                .filter(testCaseDirectoryStrategy::determineIsTestCaseDirectory)
                .sorted(Comparator.comparing(TestDirectory::name))
                .map(Arguments::of);
    }

    private PathFilter additionalFilter() {
        return ReflectionSupport.newInstance(directoryContents.filter());
    }

    private IsTestCaseDirectoryStrategy isTestCaseDirectoryStrategy() {
        return ReflectionSupport.newInstance(directoryContents.isTestcaseDeterminedBy());
    }

    private Path determineDirectory() throws IOException {
        final String legacyDir = directoryContents.directory();
        final String testResourcesDir = directoryContents.testResourcesDirectory();
        final String projectDir = directoryContents.projectDirectory();

        return AnnotationDirectoryResolver.resolveDirectory(legacyDir, projectDir, testResourcesDir);
    }

    @Override
    public void accept(DirectoriesFrom t) {
        this.directoryContents = t;
    }

}
