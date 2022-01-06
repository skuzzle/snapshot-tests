package de.skuzzle.test.snapshots.directoryparams;

import java.nio.file.Path;

final class FileExtension {

    private FileExtension() {
        // hidden
    }

    public static String withoutLeadingDot(Path file) {
        final String fileName = file.getFileName().toString();
        final int dotIdx = fileName.lastIndexOf('.');
        return dotIdx < 0
                ? ""
                : fileName.substring(dotIdx + 1);
    }

    public static String includingLeadingDot(Path file) {
        return "." + withoutLeadingDot(file);
    }
}
