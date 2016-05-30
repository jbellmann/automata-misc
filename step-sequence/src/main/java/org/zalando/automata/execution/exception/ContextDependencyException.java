package org.zalando.automata.execution.exception;

/**
 * Created by maryefyev on 22.02.16.
 */
public class ContextDependencyException extends SequenceException {
    public ContextDependencyException(final String message) {
        super(message);
    }
}
