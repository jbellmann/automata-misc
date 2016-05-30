package org.zalando.automata.execution;

import java.util.Optional;

import org.zalando.automata.execution.notifications.MessageStatus;

/**
 * Goes through a sequence of steps.
 *
 * Created by maryefyev on 13.10.15.
 */
public interface Context {
    Optional<String> getContextId();

    Object get(String key);

    Context put(String key, Object value);

    void log(final MessageStatus status, final String message);

    void log(final MessageStatus status, final String message, final Throwable throwable);

    void logInfo(String message);

    void logError(String message);

    void logError(final String message, final Throwable throwable);

    void sendNotification(final String message);

    void sendNotification(final MessageStatus status, final String message);

    void sendNotification(final MessageStatus status, final String message, final Throwable throwable);
}
