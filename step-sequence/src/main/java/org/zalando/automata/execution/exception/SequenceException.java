package org.zalando.automata.execution.exception;

/**
 *  @author maryefyev 
 */
public abstract class SequenceException extends Exception {
    public SequenceException(final String message) {
        super(message);
    }

    public SequenceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SequenceException(final Throwable cause) {
        super(cause);
    }

    public SequenceException() {
    }
}
