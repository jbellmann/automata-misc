package org.zalando.automata.execution.notifications;

/**
 * Interface representing REST-ful services our steps and sequences may wish to publish too.
 * Implementations of this interface are left to consuming service packages.
 *
 * Classes implementing Notification should assume that the body of the API call will be the
 * serialized POJO, and any parameters should come from the environment.  These assumptions
 * may not hold up in practice, and at the very least a conversation as to whether or not this
 * will work with AppDynamics and Emails should be had.
 *
 * @author abeverage
 *
 */
public interface NotificationService {
    void sendNotification(String message);
    void sendNotification(String message, MessageStatus status);
    void sendNotification(Notification notification);

    void synchronousSendNotification(String message);
    void synchronousSendNotification(String message, MessageStatus status);
    void synchronousSendNotification(Notification notification);
}
