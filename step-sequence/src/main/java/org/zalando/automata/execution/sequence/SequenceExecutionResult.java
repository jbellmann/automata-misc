package org.zalando.automata.execution.sequence;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.zalando.automata.execution.step.StepExecutionResult;
import org.zalando.automata.execution.step.StepExecutionStatus;

/**
 * Created by maryefyev on 07.12.15.
 */
public class SequenceExecutionResult {
    
    private String sequenceName;

    private Optional<String> sequenceId = Optional.empty();
    
    private SequenceExecutionStatus executionStatus = SequenceExecutionStatus.IN_PROGRESS;
    
    private final LocalDateTime started = LocalDateTime.now();
    
    private List<StepExecutionResult> stepResults = new LinkedList<>();

    public SequenceExecutionResult(final String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public SequenceExecutionResult(final String sequenceName, final String sequenceId) {
        this.sequenceName = sequenceName;
        this.sequenceId = Optional.of(sequenceId);
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public SequenceExecutionStatus addStepResult(final StepExecutionResult stepExecutionResult, final boolean isLastStep){        
        if(this.executionStatus.equals(SequenceExecutionStatus.SUCCESS)){
            throw new SequenceResultBuildException("Sequence in status SUCCESS does not accept further step results");
        }
        
        if(this.executionStatus.equals(SequenceExecutionStatus.FAILED) 
                && !stepExecutionResult.getStepExecutionStatus().equals(StepExecutionStatus.SKIPPED)){
            //if previous step has failed, it is not allowed to run further steps
            throw new SequenceResultBuildException("FAILED sequence accepts only tests with status SKIPPED");
        } else {
            this.stepResults.add(stepExecutionResult);
        }
        
        if(this.executionStatus.equals(SequenceExecutionStatus.FAILED)){
            return this.executionStatus;
        } else {

            if(stepExecutionResult.getStepExecutionStatus().equals(StepExecutionStatus.FAILED)){
                this.executionStatus = SequenceExecutionStatus.FAILED;
                return SequenceExecutionStatus.FAILED;
            } else {
                if (isLastStep){
                    this.executionStatus = SequenceExecutionStatus.SUCCESS;
                    return SequenceExecutionStatus.SUCCESS;
                } else {
                    return SequenceExecutionStatus.IN_PROGRESS;
                }
            }
        }      
    }

    public Optional<String> getSequenceId() {
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
                .append("[ ID ] ").append(this.sequenceId.orElse("UNKNOWN")).append("\n")
                .append("[ SEQUENCE ] ").append(this.sequenceName).append("\n")                
                .append("[ STARTED ] ").append(this.started).append("\n")
                .append("[ STATUS ] ").append(this.executionStatus).append("\n");
        
        this.stepResults.stream()
                .filter(step -> step.getStepExecutionStatus().equals(StepExecutionStatus.FAILED) ||
                        step.getStepExecutionStatus().equals(StepExecutionStatus.WARNING))
                .forEach(step -> builder.append(" { ").append(step).append(" } "));
        
        return builder.toString();
    }
}
