package org.zalando.automata.execution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zalando.automata.execution.notifications.MessageStatus;
import org.zalando.automata.execution.notifications.NotificationService;

import java.util.List;

/**
 * @author maryefyev
 */
public class SimpleContext extends DefaultContext {

    private static final Logger log = LoggerFactory.getLogger(SimpleContext.class);
    private List<NotificationService> notificationServices;

    public SimpleContext() {

    }

    public SimpleContext(final String contextId, List<NotificationService> notifiers) {
        super(contextId);

        this.notificationServices = notifiers;
    }

    @Override
    public void logInfo(final String message) {
        log.info(message);
    }

    @Override
    public void logError(final String error) {
        log.error(error);
    }

    @Override
    public void logError(final String message, final Throwable throwable) {
        log.error(message, throwable);
    }

    @Override
    public void log(final MessageStatus status, final String message) {
        log(status, message, null);
    }

    @Override
    public void log(final MessageStatus status, final String message, final Throwable throwable) {
        switch (status) {
        case FAIL:
            log.error(message, throwable);
            break;
        case INFO:
        case SUCCESS:
            log.info(message, throwable);
            break;
        case WARNING:
            log.warn(message, throwable);
        }
    }

    @Override
    public void sendNotification(String message) {
        sendNotification(MessageStatus.INFO, message);
    }

    @Override
    public void sendNotification(MessageStatus status, String message) {
        for (NotificationService service : notificationServices) {
            service.sendNotification(message, status);
        }
    }

    @Override
    public void sendNotification(MessageStatus status, String message, Throwable throwable) {
        String notificationMessage = message;
        if (throwable != null) {
            notificationMessage = String.join("\n", message, throwable.toString());
        }
        sendNotification(status, notificationMessage);
    }
}
