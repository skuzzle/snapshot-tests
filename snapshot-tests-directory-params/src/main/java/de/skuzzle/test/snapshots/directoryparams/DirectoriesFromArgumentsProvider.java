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

class DirectoriesFromArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<DirectoriesFrom> {

    private DirectoriesFrom directoryContents;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final Path inputFileDirectory = determineDirectory().toAbsolutePath();
        final PathFilter filter = PathFilter.fromPredicate(Files::isDirectory)
                .and(additionalFilter());

        return streamFiles(inputFileDirectory)
                .filter(filter.toPredicate())
                .map(TestDirectory::new)
                .sorted(Comparator.comparing(TestDirectory::name))
                .map(Arguments::of);
    }

    private Stream<Path> streamFiles(Path root) throws IOException {
        return Files.list(root);
    }

    private PathFilter additionalFilter() {
        return ReflectionSupport.newInstance(directoryContents.filter());
    }

    private Path determineDirectory() throws IOException {
        return DirectoryResolver.resolve(directoryContents.directory());
    }

    @Override
    public void accept(DirectoriesFrom t) {
        this.directoryContents = t;
    }

}
