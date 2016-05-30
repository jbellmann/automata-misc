package org.zalando.automata.execution;

import org.junit.Ignore;
import org.junit.Test;
import org.zalando.automata.execution.CliContext;
import org.zalando.automata.execution.DefaultContext;
import org.zalando.automata.execution.SimpleContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by maryefyev on 26.02.16.
 */
public class DefaultContextTest {

    @Test
    public void contextId(){
        final DefaultContext noIdSimple = new SimpleContext();
        assertFalse(noIdSimple.getContextId().isPresent());

        final DefaultContext noIdCli = new CliContext();
        assertFalse(noIdCli.getContextId().isPresent());

        final String contextId = "contextId";
        final DefaultContext withIdSimple = new SimpleContext(contextId, null);
        assertTrue(withIdSimple.getContextId().isPresent());
        assertEquals(contextId, withIdSimple.getContextId().get());

        final DefaultContext withIdCli = new CliContext(contextId);
        assertTrue(withIdCli.getContextId().isPresent());
        assertEquals(contextId, withIdCli.getContextId().get());
    }

    @Test
    public void contextPutGet(){
        final String key1 = "key1";
        final String value1 = "value1";

        final String key2 = "key2";
        final Map<String, String> value2 = new HashMap<>();

        final DefaultContext noIdSimple = new SimpleContext();
        noIdSimple.put(key1, value1);
        assertEquals(value1, noIdSimple.get(key1));

        noIdSimple.put(key2, value2);
        assertEquals(value2, (Map) noIdSimple.get(key2));
    }
}
