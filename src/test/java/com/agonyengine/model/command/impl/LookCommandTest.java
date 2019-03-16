package com.agonyengine.model.command.impl;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.service.BiomeService;
import com.agonyengine.stomp.model.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.RoomRepository;
import com.agonyengine.service.CommService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;

public class LookCommandTest {
    @Mock
    private Actor actor;

    @Mock
    private GameOutput output;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BiomeService biomeService;

    @Mock
    private CommService commService;

    private LookCommand lookCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        lookCommand = new LookCommand(
            actorRepository,
            roomRepository,
            biomeService,
            commService
        );
    }

    @Test
    public void testInvokeNonCreature() {
        lookCommand.invoke(actor, output);

        verify(output).append(contains("Alas"));
    }
}
