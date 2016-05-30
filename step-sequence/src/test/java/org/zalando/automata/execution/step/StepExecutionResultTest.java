package org.zalando.automata.execution.step;

import org.junit.Test;
import org.zalando.automata.execution.step.ActionOnFailure;
import org.zalando.automata.execution.step.StepExecutionResult;
import org.zalando.automata.execution.step.StepExecutionStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by maryefyev on 26.02.16.
 */
public class StepExecutionResultTest {
    
    @Test
    public void successfulResult(){
        final String stepName = "stepName";
        final StepExecutionResult result = StepExecutionResult.buildTestSuccessResult(stepName);
        
        assertEquals(stepName, result.getStepName());
        assertEquals(StepExecutionStatus.SUCCESS, result.getStepExecutionStatus());
        assertFalse(result.getErrorMessage().isPresent());
    }

    @Test
    public void skippedResult(){
        final String stepName = "stepName";
        final StepExecutionResult result = StepExecutionResult.buildTestSkippedResult(stepName);

        assertEquals(stepName, result.getStepName());
        assertEquals(StepExecutionStatus.SKIPPED, result.getStepExecutionStatus());
        assertFalse(result.getErrorMessage().isPresent());
    }

    @Test
    public void exceptionResultFail(){
        final String stepName = "stepName";
        final String exceptionMessage = "oops";
        
        final StepExecutionResult result = StepExecutionResult.buildStepResultWithException(stepName, ActionOnFailure.FAIL, exceptionMessage);

        assertEquals(stepName, result.getStepName());
        assertEquals(StepExecutionStatus.FAILED, result.getStepExecutionStatus());
        assertTrue(result.getErrorMessage().isPresent());
        assertEquals(exceptionMessage, result.getErrorMessage().get());
    }

    @Test
    public void exceptionResultProceed(){
        final String stepName = "stepName";
        final String exceptionMessage = "oops";

        final StepExecutionResult result = StepExecutionResult.buildStepResultWithException(stepName, ActionOnFailure.PROCEED, exceptionMessage);

        assertEquals(stepName, result.getStepName());
        assertEquals(StepExecutionStatus.WARNING, result.getStepExecutionStatus());
        assertTrue(result.getErrorMessage().isPresent());
        assertEquals(exceptionMessage, result.getErrorMessage().get());
    }
}
