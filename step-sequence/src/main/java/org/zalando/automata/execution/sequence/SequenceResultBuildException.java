package org.zalando.automata.execution.sequence;

/**
 * Created by maryefyev on 07.12.15.
 */
public class SequenceResultBuildException  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SequenceResultBuildException(final String message) {
        super(message);
    }
}
