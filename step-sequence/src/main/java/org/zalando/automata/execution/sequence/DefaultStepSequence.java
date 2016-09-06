package org.zalando.automata.execution.sequence;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import org.zalando.automata.execution.Context;
import org.zalando.automata.execution.step.Step;
import org.zalando.automata.execution.step.StepExecutionResult;

/**
 * @author maryefyev
 */
public class DefaultStepSequence implements StepSequence{

    private final static String START_MESSAGE = "STARTED";
    private final static String NEXT_STEP_MESSAGE = "EXECUTING STEP:";
    private final static String FINISHED_MESSAGE = "FINISHED";
    private final static String FAILED_MESSAGE = "FAILED";
    private final static String SKIPPED_MESSAGE = "SKIPPED";
    private final static String ROLLBACK_MESSAGE = "ROLLED BACK:";
    private final static String SPACER = "  ";

    private final Queue<Step> sequence = new LinkedList<>();

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void add(final Step step) {
        this.sequence.add(step);
    }

    @Override
    public SequenceExecutionResult execute(final Context context) {
        final SequenceExecutionResult sequenceExecutionResult;
        if(context.getContextId().isPresent()){
            sequenceExecutionResult = new SequenceExecutionResult(getName(), context.getContextId().get());
        } else {
            sequenceExecutionResult = new SequenceExecutionResult(getName());
        }

        final String loggerPrefix = buildLoggerMessagePrefix(getName(), context.getContextId());

        context.logInfo(loggerPrefix + SPACER + START_MESSAGE);

        while(!sequence.isEmpty()){
            final Step step = this.sequence.poll();
            context.logInfo(loggerPrefix + SPACER + NEXT_STEP_MESSAGE + SPACER + step.getName());

            StepExecutionResult stepExecutionResult = null;

            try {
                step.execute(context);
            } catch (Exception e) {
                context.logError(e.getMessage(), e);
                stepExecutionResult = StepExecutionResult.buildStepResultWithException(step.getName(), step.getActionOnFailure(), e.getMessage());
            }

            final SequenceExecutionStatus status = sequenceExecutionResult
                    .addStepResult(stepExecutionResult == null ?
                            StepExecutionResult.buildTestSuccessResult(getName()) : stepExecutionResult, sequence.isEmpty());

            if(status == SequenceExecutionStatus.ROLLBACK){
                context.logInfo(loggerPrefix + SPACER + step.getName() + SPACER + ROLLBACK_MESSAGE);

                while(!sequence.isEmpty()){
                    Step skippedStep = sequence.poll();
                    context.logInfo(loggerPrefix + SPACER + skippedStep.getName() + SPACER + SKIPPED_MESSAGE);
                    sequenceExecutionResult.addStepResult(StepExecutionResult.buildTestSkippedResult(skippedStep.getName()), false);
                }
                context.logInfo(loggerPrefix + SPACER + FAILED_MESSAGE);
            }

            if(status == SequenceExecutionStatus.FAILED){
                context.logInfo(loggerPrefix + SPACER + step.getName() + SPACER + FAILED_MESSAGE);
                while(!sequence.isEmpty()){
                    Step skippedStep = sequence.poll();
                    context.logInfo(loggerPrefix + SPACER + skippedStep.getName() + SPACER + SKIPPED_MESSAGE);
                    sequenceExecutionResult.addStepResult(StepExecutionResult.buildTestSkippedResult(skippedStep.getName()), false);
                }
                context.logInfo(loggerPrefix + SPACER + FAILED_MESSAGE);
            }

            if(status == SequenceExecutionStatus.SUCCESS){
                context.logInfo(loggerPrefix + SPACER + FINISHED_MESSAGE);
            }
        }
        return sequenceExecutionResult;
    }

    private static String buildLoggerMessagePrefix(final String name, final Optional<String> contextId){
        return  contextId.isPresent() ? "[ " + contextId.get() + " | " + name + " ]" : "[ " + name + " ]";
    }
}
