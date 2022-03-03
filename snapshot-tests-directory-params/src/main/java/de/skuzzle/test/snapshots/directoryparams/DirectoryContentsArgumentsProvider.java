package de.skuzzle.test.snapshots.directoryparams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import de.skuzzle.test.snapshots.io.DirectoryResolver;

class DirectoryContentsArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<FilesFrom> {

    private FilesFrom directoryContents;

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        final Path inputFileDirectory = determineDirectory().toAbsolutePath();

        return streamFiles(inputFileDirectory)
                .filter(this::includeFile)
                .map(TestFile::new)
                .sorted(Comparator.comparing(TestFile::name))
                .map(Arguments::of);
    }

    private Stream<Path> streamFiles(Path root) throws IOException {
        return directoryContents.recursive()
                ? Files.walk(root)
                : Files.list(root);
    }

    private boolean includeFile(Path file) {
        return directoryContents.extensions().length == 0 ||
                Arrays.stream(directoryContents.extensions())
                        .anyMatch(configuredExtension -> matchesExtension(configuredExtension, file));
    }

    private boolean matchesExtension(String configuredExtension, Path file) {
        final String normalizedExtension = configuredExtension.startsWith(".")
                ? configuredExtension
                : "." + configuredExtension;
        final String filesExtension = FileExtension.includingLeadingDot(file);
        return filesExtension.equalsIgnoreCase(normalizedExtension);
    }

    private Path determineDirectory() throws IOException {
        return DirectoryResolver.resolve(directoryContents.directory());
    }

    @Override
    public void accept(FilesFrom t) {
        this.directoryContents = t;
    }

}
