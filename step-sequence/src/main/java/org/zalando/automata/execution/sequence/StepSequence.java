package org.zalando.automata.execution.sequence;

import org.zalando.automata.execution.Context;
import org.zalando.automata.execution.step.Step;

/**
 * Simple sequence of steps to execute
 * Created by maryefyev on 13.10.15.
 */
public interface StepSequence{
    void add(final Step step);
    String getName();
    SequenceExecutionResult execute(final Context context);
}
