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

import de.skuzzle.test.snapshots.io.DirectoryResolver;

class FilesFromArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<FilesFrom> {

    private FilesFrom filesFrom;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final Path inputFileDirectory = determineDirectory().toAbsolutePath().toRealPath();
        final PathFilter filter = PathFilter.fromPredicate(Files::isRegularFile)
                .and(PathFilterExtensions.extensions(filesFrom.extensions()))
                .and(additionalFilter());

        return streamFiles(inputFileDirectory)
                .filter(filter.toPredicate())
                .map(TestFile::new)
                .sorted(Comparator.comparing(TestFile::name))
                .map(Arguments::of);
    }

    private Stream<Path> streamFiles(Path root) throws IOException {
        return filesFrom.recursive()
                ? Files.walk(root)
                : Files.list(root);
    }

    private PathFilter additionalFilter() {
        return ReflectionSupport.newInstance(filesFrom.filter());
    }

    private Path determineDirectory() throws IOException {
        if (!filesFrom.otherDirectory().isEmpty()) {
            return Path.of(filesFrom.otherDirectory());
        }
        return DirectoryResolver.resolve(filesFrom.directory());
    }

    @Override
    public void accept(FilesFrom t) {
        this.filesFrom = t;
    }

}
