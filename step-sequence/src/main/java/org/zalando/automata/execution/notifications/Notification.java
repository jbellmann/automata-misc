package org.zalando.automata.execution.notifications;

/**
 * @author abeverage
 */
public interface Notification {
    String getMessage();
    MessageStatus getStatus();

    Notification withMessage(String Message);
    Notification withStatus(MessageStatus status);
    Notification withFailResult();
    Notification withSuccessResult();
    Notification withWarningResult();
    Notification withInfoResult();
}
