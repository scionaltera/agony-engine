package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.command.LookCommand;
import com.agonyengine.model.command.SayCommand;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.stomp.GameOutput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;

public class InvokerServiceTest {
    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private LookCommand lookCommand;

    @Mock
    private SayCommand sayCommand;

    @Mock
    private Actor actor;

    @Mock
    private GameOutput output;

    private InvokerService invokerService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(applicationContext.getBean(eq("lookCommand"))).thenReturn(lookCommand);
        when(applicationContext.getBean(eq("sayCommand"))).thenReturn(sayCommand);

        invokerService = new InvokerService(applicationContext);
    }

    @Test
    public void testInvokeNoArgs() {
        invokerService.invoke("lookCommand", actor, output);

        verify(lookCommand).invoke(eq(actor), eq(output));
    }

    @Test
    public void testInvokeQuotedString() {
        QuotedString quoted = new QuotedString("foo"); // mocks don't play nice with all the reflection

        invokerService.invoke("sayCommand", actor, output, quoted);

        verify(sayCommand).invoke(eq(actor), eq(output), eq(quoted));
    }
}
