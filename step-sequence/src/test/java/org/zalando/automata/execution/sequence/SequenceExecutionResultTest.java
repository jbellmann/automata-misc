package org.zalando.automata.execution.sequence;

import org.junit.Test;
import org.zalando.automata.execution.sequence.SequenceExecutionResult;
import org.zalando.automata.execution.sequence.SequenceExecutionStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by maryefyev on 26.02.16.
 */
public class SequenceExecutionResultTest {
    
    @Test
    public void constructorNoId(){
        final String name = "name";
        
        final SequenceExecutionResult result = new SequenceExecutionResult(name);
        
        assertEquals(name, result.getSequenceName());
        assertEquals(SequenceExecutionStatus.IN_PROGRESS, result.getExecutionStatus());
        assertFalse(result.getSequenceId().isPresent());
        assertEquals(0, result.getStepResults().size());
    }

    @Test
    public void constructorWithId(){
        final String name = "name";
        final String id = "id";

        final SequenceExecutionResult result = new SequenceExecutionResult(name, id);

        assertEquals(name, result.getSequenceName());
        assertTrue(result.getSequenceId().isPresent());
        assertEquals(id, result.getSequenceId().get());
        assertEquals(0, result.getStepResults().size());
        assertEquals(SequenceExecutionStatus.IN_PROGRESS, result.getExecutionStatus());
    }
}
