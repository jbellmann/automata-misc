package org.zalando.automata.execution.step;

/**
 * Created by maryefyev on 13.10.15.
 */
public class StepExecutionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StepExecutionException(final String message) {
        super(message);
    }

    public StepExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
