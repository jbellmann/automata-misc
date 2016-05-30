package org.zalando.automata.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by maryefyev on 26.02.16.
 */
public abstract class DefaultContext implements Context {

    private Map<String, Object> contextValues = new HashMap<>();

    private Optional<String> contextId = Optional.empty();

    public DefaultContext() {}

    public DefaultContext(final String contextId) {
        this.contextId = Optional.ofNullable(contextId);
    }

    @Override
    public Optional<String> getContextId() {
        return this.contextId;
    }

    @Override
    public Object get(final String key) {
        return this.contextValues.get(key);
    }

    @Override
    public Context put(final String key, final Object value) {
        this.contextValues.put(key, value);
        return this;
    }
}
