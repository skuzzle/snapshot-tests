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

class FilesFromArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<FilesFrom> {

    private FilesFrom filesFrom;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final Path inputFileDirectory = determineDirectory().toAbsolutePath().toRealPath();
        final boolean isRecursive = filesFrom.recursive();

        final TestFileFilter filter = Filters.and(
                PathFilterExtensions.extensions(filesFrom.extensions()),
                testFileFilter());

        final Stream<TestFile> files = isRecursive
                ? provideArgumentsRecursive(inputFileDirectory)
                : provideArgumentsFlat(inputFileDirectory);

        return files
                .filter(Filters.toPredicate(filter, isRecursive))
                .sorted(Comparator.comparing(TestFile::name))
                .map(Arguments::of);
    }

    private Stream<TestFile> provideArgumentsFlat(Path inputFileDirectory) throws Exception {
        return Files.list(inputFileDirectory)
                .filter(Files::isRegularFile)
                .map(TestFile::new);
    }

    private Stream<TestFile> provideArgumentsRecursive(Path inputFileDirectory) throws Exception {
        return Files.walk(inputFileDirectory)
                .filter(Files::isRegularFile)
                .map(TestFile::new);
    }

    private TestFileFilter testFileFilter() {
        return ReflectionSupport.newInstance(filesFrom.filter());
    }

    private Path determineDirectory() throws IOException {
        final String legacyDir = filesFrom.directory();
        final String testResourcesDir = filesFrom.testResourcesDirectory();
        final String projectDir = filesFrom.projectDirectory();

        return AnnotationDirectoryResolver.resolveDirectory(legacyDir, projectDir, testResourcesDir);
    }

    @Override
    public void accept(FilesFrom t) {
        this.filesFrom = t;
    }

}
