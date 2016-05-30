package org.zalando.automata.execution.notifications;

/**
 * Concept of a message status for downstream messaging systems.
 *
 * @author abeverage
 */
public enum MessageStatus {
    ERROR,
    FAIL,
    INFO,
    SUCCESS,
    WARNING
}
