package com.agonyengine.model.command.impl;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.command.binding.QuotedString;
import com.agonyengine.stomp.model.GameOutput;
import com.agonyengine.service.CommService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;

public class SayCommandTest {
    @Mock
    private Actor actor;

    @Mock
    private GameOutput output;

    @Mock
    private QuotedString quotedString;

    @Mock
    private CommService commService;

    private SayCommand sayCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sayCommand = new SayCommand(commService);
    }

    @Test
    public void testInvokeNonCreature() {
        sayCommand.invoke(actor, output, quotedString);

        verify(output).append(contains("Alas"));
    }
}
