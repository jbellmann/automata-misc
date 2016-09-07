package org.zalando.automata.execution.sequence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zalando.automata.execution.step.StepExecutionResult;
import org.zalando.automata.execution.step.StepExecutionStatus;

import static org.zalando.automata.execution.sequence.SequenceExecutionStatus.*;

/**
 * Created by maryefyev on 07.12.15.
 */
public class SequenceExecutionResult {

    private final String sequenceName;
    private final String sequenceId;
    private final LocalDateTime started;
    private final List<StepExecutionResult> stepResults;

    private SequenceExecutionStatus executionStatus = IN_PROGRESS;

    public SequenceExecutionResult(final String sequenceName) {
        this(sequenceName, null);
    }

    public SequenceExecutionResult(final String sequenceName, final String sequenceId) {
        this.sequenceName = sequenceName;
        this.sequenceId = sequenceId;
        this.started = LocalDateTime.now();
        this.stepResults = new ArrayList<>();
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public SequenceExecutionStatus addStepResult(final StepExecutionResult stepResult, final boolean isLastStep){
        //  Add the step result.  If the sequence has already failed or rolled back, only skipped steps can be accepted.
        //  If the sequence has already succeeded, no steps are accepted.
        //  Are we being overly defensive here?  Shouldn't the executor handle this?
        if(SUCCESS.equals(executionStatus)) {
            throw new SequenceResultBuildException("Sequence in status SUCCESS does not accept further step results.");
        } else if ((FAILED.equals(executionStatus) || ROLLBACK.equals(executionStatus))
                && !StepExecutionStatus.SKIPPED.equals(stepResult.getStepExecutionStatus())) {
            throw new SequenceResultBuildException("Sequence in failed or rollback state can accepts only steps with status SKIPPED.");
        } else {
            stepResults.add(stepResult);

        }

        //  TODO: these should be the same enum - no time to update all the downstream consumers right now though.
        if (FAILED.equals(executionStatus) || StepExecutionStatus.FAILED.equals(stepResult.getStepExecutionStatus())) {
            return executionStatus = FAILED;
        }

        if (ROLLBACK.equals(executionStatus) || StepExecutionStatus.ROLLBACK.equals(stepResult.getStepExecutionStatus())) {
            return executionStatus = ROLLBACK;
        }

        if (isLastStep) {
            return executionStatus = SUCCESS;
        } else {
            return executionStatus = IN_PROGRESS;
        }
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public SequenceExecutionStatus getExecutionStatus() {
        return this.executionStatus;
    }

    public List<StepExecutionResult> getStepResults() {
        return Collections.unmodifiableList(this.stepResults);
    }

    public LocalDateTime getStarted() {
        return this.started;
    }

    public String getPrintableResult(){
        final StringBuilder builder = new StringBuilder()
                .append("[ ID ] ").append(sequenceId).append("\n")
                .append("[ SEQUENCE ] ").append(sequenceName).append("\n")
                .append("[ STARTED ] ").append(started).append("\n")
                .append("[ STATUS ] ").append(executionStatus).append("\n");

        this.stepResults.stream()
                .filter(step -> step.getStepExecutionStatus().equals(StepExecutionStatus.FAILED) ||
                        step.getStepExecutionStatus().equals(StepExecutionStatus.WARNING))
                .forEach(step -> builder.append(" { ").append(step).append(" } "));

        return builder.toString();
    }
}
