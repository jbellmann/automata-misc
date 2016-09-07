package org.zalando.automata.execution.step;

import org.zalando.automata.execution.Context;
import org.zalando.automata.execution.exception.ContextDependencyException;

/**
 * @author maryefyev
 */
public abstract class AbstractStep implements Step {

    protected Context context;
    private ActionOnFailure actionOnFailure; //if exception in this step should lead to a sequence failure

    public AbstractStep() {
        this.actionOnFailure = ActionOnFailure.FAIL;
    }

    public AbstractStep(final ActionOnFailure actionOnFailure) {
        this.actionOnFailure = actionOnFailure;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    public ActionOnFailure getActionOnFailure() {
        return this.actionOnFailure;
    }

    public void setActionOnFailure(ActionOnFailure action) {
        this.actionOnFailure = action;
    }

    /**
     * Empty implementation by default.
     * @param context
     * @throws Exception
     */
    protected void beforeExecution(final Context context) throws Exception {
    }

    /**
     * Empty implementation by default.
     *
     * @param context
     * @throws Exception
     */
    protected void afterExecution(final Context context) {
    }

    @Override
    public void execute(final Context context) throws Exception {
        this.context = context;
        beforeExecution(context);
        doExecute(context);
        afterExecution(context);
    }

    protected void logInfo(final String message) {
        this.context.logInfo(message);
    }

    protected void logError(final String message) {
        this.context.logError(message);
    }

    protected void logError(final String message, final Throwable throwable) {
        this.context.logError(message, throwable);
    }

    protected abstract void doExecute(final Context context) throws Exception;

    protected Context getContext() {
        return this.context;
    }

    protected void verifyContextValue(final String contextLookUpValue, final Class<?> aClass) throws ContextDependencyException {
        final Object object = context.get(contextLookUpValue);
        if (object == null || object.getClass() != aClass) {
            throw new ContextDependencyException("Context dependency check failed for " + contextLookUpValue);
        }

        if (aClass == String.class && ((String) object).isEmpty()) {
            throw new ContextDependencyException("Context dependency check failed for " + contextLookUpValue + ". String size must not be zero");
        }
    }
}
