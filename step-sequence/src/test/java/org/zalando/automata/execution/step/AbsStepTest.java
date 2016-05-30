package org.zalando.automata.execution.step;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.zalando.automata.execution.CliContext;
import org.zalando.automata.execution.Context;
import org.zalando.automata.execution.exception.ContextDependencyException;
import org.zalando.automata.execution.step.AbstractStep;
import org.zalando.automata.execution.step.ActionOnFailure;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

/**
 * Tests AbstractStep, but maven does not like "Abstract" part of name
 * Created by maryefyev on 22.02.16.
 */
@RunWith(DataProviderRunner.class)
public class AbsStepTest {
    
    private static final String KEY = "LOOK_UP_KEY";
    
    @Test
    public void logTest() throws Exception {
        final String message1 = "message1";
        final String message2 = "message2";
        
        final AbstractStep actionDefault = new AbstractStep() {
            @Override
            protected void doExecute(Context context) throws Exception {
                    logInfo(message1);
                    logError(message2);
            }
        };

        final CliContext context = Mockito.mock(CliContext.class);
        actionDefault.execute(context);

        verify(context, Mockito.times(1)).logInfo(message1);
        verify(context, Mockito.times(1)).logError(message2);
    }
    
    @Test
    public void actionOnFailure(){
        final AbstractStep actionDefault = new AbstractStep() {
            @Override
            protected void doExecute(Context context) throws Exception {

            }
        };
        
        assertEquals( ActionOnFailure.FAIL, actionDefault.getActionOnFailure());

        final AbstractStep actionSet = new AbstractStep(ActionOnFailure.PROCEED) {
            @Override
            protected void doExecute(Context context) throws Exception {

            }
        };

        assertEquals(ActionOnFailure.PROCEED, actionSet.getActionOnFailure());
    }

    @Test    
    public void execute() throws Exception {
        final AbstractStep step = new AbstractStep() {
            @Override
            protected void doExecute(Context context) throws Exception {
                
            }
        };

        final AbstractStep spy = Mockito.spy(step);

        final CliContext context = new CliContext();
        spy.execute(context);

        verify(spy, Mockito.times(1)).beforeExecution(context);
        verify(spy, Mockito.times(1)).doExecute(context);
        verify(spy, Mockito.times(1)).afterExecution(context);

        InOrder inOrder = Mockito.inOrder(spy);
        inOrder.verify(spy).beforeExecution(context);
        inOrder.verify(spy).doExecute(context);
        inOrder.verify(spy).afterExecution(context);

        assertTrue(spy.getContext() == context);
    }
    
    @Test(expected = ContextDependencyException.class)
    @UseDataProvider("verifyContext")
    public void verifyContextValue(final Context context) throws Exception {
        final AbstractStep step = new AbstractStep() {
            @Override
            protected void doExecute(Context context) throws Exception {
                verifyContextValue(KEY, String.class);
            }
        };
        step.execute(context);        
    }

    @DataProvider
    public static Object[][] verifyContext() {
        return new Object[][]{
                new Object[]{new CliContext()},
                new Object[]{new CliContext().put(KEY, null)},
                new Object[]{new CliContext().put(KEY, new HashMap<>())},
                new Object[]{new CliContext().put(KEY, "")}
        };
    }
    
}
