package org.zalando.automata.execution.step;

import org.zalando.automata.execution.Context;

/**
 * Created by maryefyev on 13.10.15.
 */
public interface Step {
    String getName();
    void execute(final Context context) throws Exception;
    ActionOnFailure getActionOnFailure();
    void setActionOnFailure(ActionOnFailure action);
}
