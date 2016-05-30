package org.zalando.automata.execution;

import java.io.PrintStream;

import org.zalando.automata.execution.notifications.MessageStatus;

/**
 * Created by maryefyev on 08.02.16.
 */
public class CliContext extends DefaultContext {

    private static final PrintStream log = System.out;

    public CliContext() {}

    public CliContext(final String contextId) {
        super(contextId);
    }

    @Override
    public void logInfo(final String message) {
        log.println(message);
    }

    @Override
    public void logError(final String message) {
        log.println(message);
    }

    @Override
    public void logError(final String message, final Throwable throwable) {
        log.println(message);
        throwable.printStackTrace();
    }

    @Override
    public void log(MessageStatus status, String message) {
        logInfo(message);
    }

    @Override
    public void log(final MessageStatus status, final String message, final Throwable throwable) {
        log.println(String.join("\n", status + ": " + message, throwable.toString()));
    }

    @Override
    public void sendNotification(String message) {

    }

    @Override
    public void sendNotification(MessageStatus status, String message) {

    }

    @Override
    public void sendNotification(MessageStatus status, String message, Throwable throwable) {

    }
}
