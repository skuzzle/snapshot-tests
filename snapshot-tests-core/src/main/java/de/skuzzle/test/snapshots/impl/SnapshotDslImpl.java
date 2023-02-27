package de.skuzzle.test.snapshots.impl;

import java.lang.reflect.Method;
import java.nio.file.Path;

import de.skuzzle.test.snapshots.Snapshot;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseActual;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseAssertions;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseDataFormat;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseName;
import de.skuzzle.test.snapshots.SnapshotDsl.ChooseStructure;
import de.skuzzle.test.snapshots.SnapshotException;
import de.skuzzle.test.snapshots.SnapshotNaming;
import de.skuzzle.test.snapshots.SnapshotSerializer;
import de.skuzzle.test.snapshots.SnapshotTestResult;
import de.skuzzle.test.snapshots.StructuralAssertions;
import de.skuzzle.test.snapshots.StructuredData;
import de.skuzzle.test.snapshots.StructuredDataProvider;
import de.skuzzle.test.snapshots.data.text.TextSnapshot;
import de.skuzzle.test.snapshots.impl.SnapshotAssertionInput.TerminalOperation;
import de.skuzzle.test.snapshots.validation.Arguments;
import de.skuzzle.test.snapshots.validation.State;

/**
 * DSL implementation that can be injected as {@link Snapshot} instance into test methods.
 *
 * @author Simon
 */
final class SnapshotDslImpl implements Snapshot, ChooseActual, ChooseDataFormat,
        ChooseStructure, ChooseAssertions {

    private final ResultRecorder resultRecorder;
    private final SnapshotConfiguration snapshotConfiguration;
    private final Method testMethod;
    private final DslState state = DslState.initial();

    private Object actual;
    private Path directoryOverride;
    private SnapshotNaming namingStrategy;
    private SnapshotSerializer snapshotSerializer;
    private StructuralAssertions structuralAssertions;

    public SnapshotDslImpl(ResultRecorder resultRecorder, SnapshotConfiguration snapshotConfiguration,
            Method testMethod) {
        this.resultRecorder = Arguments.requireNonNull(resultRecorder,
                "resultRecorder must not be null");
        this.snapshotConfiguration = Arguments.requireNonNull(snapshotConfiguration,
                "snapshotConfiguration must not be null");
        this.testMethod = Arguments.requireNonNull(testMethod, "testMethod must not be null");

        this.resetDSL();
    }

    private void resetDSL() {
        this.state.reset();
        this.actual = null;
        this.directoryOverride = null;
        this.namingStrategy = SnapshotNaming.defaultNaming();
        this.snapshotSerializer = TextSnapshot.text.snapshotSerializer();
        this.structuralAssertions = TextSnapshot.text.structuralAssertions();
    }

    @Override
    public ChooseActual namedAccordingTo(SnapshotNaming namingStrategy) {
        state.append(DslState.NAME_CHOSEN);
        this.namingStrategy = Arguments.requireNonNull(namingStrategy, "namingStrategy must not be null");
        return this;
    }

    @Override
    public ChooseName in(Path directory) {
        state.append(DslState.DIRECTORY_CHOSEN);
        this.directoryOverride = Arguments.requireNonNull(directory, "directory must not be null");
        return this;
    }

    @Override
    public ChooseDataFormat assertThat(Object actual) {
        state.append(DslState.ACTUAL_CHOSEN);
        this.actual = actual;
        return this;
    }

    @Override
    public ChooseAssertions asText() {
        return as(TextSnapshot.text);
    }

    @Override
    public ChooseAssertions as(SnapshotSerializer serializer) {
        state.append(DslState.SERIALIZER_CHOSEN);

        this.snapshotSerializer = Arguments.requireNonNull(serializer, "serializer must not be null");
        return this;
    }

    @Override
    public ChooseStructure as(StructuredDataProvider structuredDataBuilder) {
        state.append(DslState.ASSERTIONS_CHOSEN);
        state.append(DslState.SERIALIZER_CHOSEN);

        final StructuredData structure = Arguments
                .requireNonNull(structuredDataBuilder, "structuredDataBuilder must not be null").build();

        this.snapshotSerializer = structure.snapshotSerializer();
        this.structuralAssertions = structure.structuralAssertions();
        return this;
    }

    private boolean isCustomTextSnapshot() {
        return this.structuralAssertions.getClass().equals(TextSnapshot.text.structuralAssertions().getClass());
    }

    @Override
    public SnapshotTestResult matchesSnapshotText() {
        if (isCustomTextSnapshot()) {
            // prevent surprises when using a customized TextSnapshot instance. We then
            // use the actually configured TextStructuralAssertions instead of the default
            // one
            return this.matchesAccordingTo(structuralAssertions);
        }
        return this.matchesAccordingTo(TextSnapshot.text.structuralAssertions());
    }

    @Override
    public SnapshotTestResult matchesSnapshotStructure() {
        return this.matchesAccordingTo(this.structuralAssertions);
    }

    @Override
    public SnapshotTestResult matchesAccordingTo(StructuralAssertions structuralAssertions) {
        Arguments.requireNonNull(structuralAssertions, "structuralAssertions must not be null");
        return terminal(TerminalOperation.ASSERT, structuralAssertions);
    }

    @Override
    public SnapshotTestResult justUpdateSnapshot() {
        return terminal(TerminalOperation.FORCE_UPDATE, this.structuralAssertions);
    }

    @Override
    public SnapshotTestResult disabled() {
        return terminal(TerminalOperation.DISABLE, this.structuralAssertions);
    }

    @Override
    public SnapshotTestResult disabledBecause(String message) {
        // message param intentionally ignored. It only exists for the call site to tell
        // readers why this assertion has been disabled.
        return terminal(TerminalOperation.DISABLE, this.structuralAssertions);
    }

    private SnapshotTestResult terminal(TerminalOperation operation, StructuralAssertions structuralAssertions) {
        try {
            final SnapshotDslResult dslResult = new SnapshotDslResult(
                    snapshotConfiguration,
                    resultRecorder,
                    testMethod,
                    namingStrategy,
                    actual,
                    operation,
                    snapshotSerializer,
                    directoryOverride);

            final SnapshotAssertionInput assertionInput = dslResult.createAssertionInput();
            final SnapshotAssertionExecutor assertionExecutor = new SnapshotAssertionExecutor();
            final ExecutionLifecycle lifecycle = new DefaultExecutionLifecycle(assertionExecutor, resultRecorder);

            return lifecycle.executeLifecycleWith(structuralAssertions, assertionInput);
        } catch (final Exception e) {
            throw new SnapshotException("Technical problem while performing snapshot test terminal operation", e);
        } finally {
            resetDSL();
        }
    }

    void executeFinalAssertions() throws Exception {
        State.check(state.isInitial(),
                "Detected incomplete DSL usage. Please always call a terminal operation (see JavaDoc of the Snapshot class for details). "
                        + "If you want to temporarily disable a snapshot assertion, use the disabled() terminal operation.");

        resultRecorder.throwIfNotSuccessfulOrCreatedInitiallyOrUpdatedForcefully(snapshotConfiguration.isSoftAssertions());
    }
}
