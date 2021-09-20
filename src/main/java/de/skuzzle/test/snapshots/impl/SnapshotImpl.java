package de.skuzzle.test.snapshots.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;

import de.skuzzle.test.snapshots.SnapshotAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChoseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.Snapshot;
import de.skuzzle.test.snapshots.SnapshotResult;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotStatus;
import de.skuzzle.test.snapshots.StructuralAssertions;

class SnapshotImpl implements Snapshot {

    private final ExtensionContext extensionContext;
    private int counter;
    private final List<SnapshotResult> results = new ArrayList<>();

    public SnapshotImpl(ExtensionContext extensionContext) {
        this.extensionContext = extensionContext;
    }

    @Override
    public ChoseDataFormat assertThat(Object actual) {
        return new SnapshotDslImpl(this, actual);
    }

    private boolean isForceUpdateSnapshots() {
        return extensionContext.getRequiredTestClass()
                .getAnnotation(SnapshotAssertions.class)
                .forceUpdateSnapshots();
    }

    private boolean isSoftAssertions() {
        return extensionContext.getRequiredTestClass()
                .getAnnotation(SnapshotAssertions.class)
                .softAssertions();
    }

    private String determineNextSnapshotName() {
        return extensionContext.getRequiredTestMethod().getName() + "_" + counter++;
    }

    private Path determineSnapshotDirecotry() throws IOException {
        return DirectoryResolver.resolveSnapshotDirectory(extensionContext);
    }

    private Path determineSnapshotFile(String snapshotName) throws IOException {
        return determineSnapshotDirecotry().resolve(snapshotName + ".snapshot");
    }

    public List<SnapshotResult> results() {
        return results;
    }

    SnapshotResult justUpdateSnapshotWith(SnapshotSerializer snapshotSerializer, Object actual) throws Exception {
        final String snapshotName = determineNextSnapshotName();
        final Path snapshotFile = determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        Files.writeString(snapshotFile, serializedActual, StandardCharsets.UTF_8);
        final SnapshotResult result = SnapshotResult.of(snapshotFile, SnapshotStatus.UPDATED_FORCEFULLY,
                serializedActual);
        results.add(result);
        return result;
    }

    SnapshotResult executeAssertionWith(SnapshotSerializer snapshotSerializer,
            StructuralAssertions structuralAssertions,
            Object actual) throws Throwable {
        final String snapshotName = determineNextSnapshotName();
        final Path snapshotFile = determineSnapshotFile(snapshotName);
        final String serializedActual = snapshotSerializer.serialize(actual);

        final boolean forceUpdateSnapshots = isForceUpdateSnapshots();
        final boolean alreadyExisted = Files.exists(snapshotFile);

        final SnapshotResult result;
        if (forceUpdateSnapshots || !alreadyExisted) {
            Files.writeString(snapshotFile, serializedActual, StandardCharsets.UTF_8);

            final SnapshotStatus status = alreadyExisted
                    ? SnapshotStatus.UPDATED_FORCEFULLY
                    : SnapshotStatus.CREATED_INITIALLY;
            result = SnapshotResult.of(snapshotFile, status, serializedActual);
        } else {
            final String storedSnapshot = Files.readString(snapshotFile, StandardCharsets.UTF_8);
            result = assertEquality(structuralAssertions, storedSnapshot, serializedActual)
                    .map(assertionError -> SnapshotResult.forFailedTest(snapshotFile, storedSnapshot, assertionError))
                    .orElseGet(() -> SnapshotResult.of(snapshotFile, SnapshotStatus.ASSERTED, storedSnapshot));
        }
        this.results.add(result);

        if (!isSoftAssertions() && result.failure().isPresent()) {
            throw result.failure().orElseThrow();
        }

        return result;
    }

    private Optional<Throwable> assertEquality(StructuralAssertions structuralAssertions, String storedSnapshot,
            String serializedActual) throws Exception {
        try {
            structuralAssertions.assertEquals(storedSnapshot, serializedActual);
            return Optional.empty();
        } catch (final AssertionError e) {
            // Compare again using 'assertEquals' but retain the original error message.
            // This enables diff support in major IDEs.
            // Also, FIXME: https://github.com/skuzzle/snapshot-tests/issues/4
            // Original stack trace is lost here
            try {
                Assertions.assertThat(serializedActual)
                        .as(e.getMessage())
                        .isEqualTo(storedSnapshot);
            } catch (final AssertionError e1) {
                return Optional.of(e1);
            }
        }
        throw new IllegalStateException("not reachable");
    }

    boolean wasUpdatedForcefully() {
        return results.stream().map(SnapshotResult::status).anyMatch(SnapshotStatus.UPDATED_FORCEFULLY::equals);
    }

    boolean wasCreatedInitially() {
        return results.stream().map(SnapshotResult::status).anyMatch(SnapshotStatus.CREATED_INITIALLY::equals);
    }

    String getTestClassName() {
        return extensionContext.getRequiredTestClass().getName();
    }
}
