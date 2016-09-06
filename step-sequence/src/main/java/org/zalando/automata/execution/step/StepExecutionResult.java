package org.zalando.automata.execution.step;

import java.util.Optional;

/**
 * @author maryefyev
 */
public class StepExecutionResult {

    private String stepName;
    private StepExecutionStatus stepExecutionStatus;
    private Optional<String> errorMessage = Optional.empty();

    public StepExecutionResult(final String stepName,
                               final StepExecutionStatus stepExecutionStatus) {
        this.stepName = stepName;
        this.stepExecutionStatus = stepExecutionStatus;
    }

    public StepExecutionStatus getStepExecutionStatus() {
        return this.stepExecutionStatus;
    }

    public String getStepName() {
        return stepName;
    }

    public Optional<String> getErrorMessage() {
        return errorMessage;
    }

    public static StepExecutionResult buildTestSuccessResult(final String stepName){
        return new StepExecutionResult(stepName, StepExecutionStatus.SUCCESS);
    }

    public static StepExecutionResult buildTestSkippedResult(final String stepName){
        return new StepExecutionResult(stepName, StepExecutionStatus.SKIPPED);
    }

    public static StepExecutionResult buildStepResultWithException(final String stepName,
                                                                   final ActionOnFailure actionOnFailure,
                                                                   final String errorMessage){
        StepExecutionResult stepExecutionResult;

        if(actionOnFailure == ActionOnFailure.FAIL){
            stepExecutionResult = new StepExecutionResult(stepName, StepExecutionStatus.FAILED);
        } else if (actionOnFailure == ActionOnFailure.ROLLBACK) {
            stepExecutionResult = new StepExecutionResult(stepName, StepExecutionStatus.ROLLBACK);
        } else {
            stepExecutionResult = new StepExecutionResult(stepName, StepExecutionStatus.WARNING);
        }

        stepExecutionResult.errorMessage = Optional.ofNullable(errorMessage);

        return stepExecutionResult;
    }

    public String toString(){
        final StringBuilder stringBuilder = new StringBuilder()
                .append("[ STEP ] ").append(this.stepName).append("\n")
                .append("[ STATUS ] ").append(this.stepExecutionStatus).append("\n");

        if(this.errorMessage.isPresent()){
            stringBuilder.append("[ ERROR ] ").append(this.errorMessage.get());
        }
        return stringBuilder.toString();
    }
}
