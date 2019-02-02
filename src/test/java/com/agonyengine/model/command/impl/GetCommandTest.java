package com.agonyengine.model.command.impl;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.command.binding.ActorSameRoom;
import com.agonyengine.stomp.model.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;

public class GetCommandTest {
    @Mock
    private Actor actor;

    @Mock
    private GameOutput output;

    @Mock
    private ActorSameRoom actorSameRoom;

    @Mock
    private CommService commService;

    @Mock
    private InvokerService invokerService;

    @Mock
    private ActorRepository actorRepository;

    private GetCommand getCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        getCommand = new GetCommand(
            commService,
            invokerService,
            actorRepository
        );
    }

    @Test
    public void testInvokeNonCreature() {
        getCommand.invoke(actor, output, actorSameRoom);

        verify(output).append(contains("Alas"));
    }
}
