package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.map.Direction;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.RoomRepository;
import com.agonyengine.service.CommService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

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
    private CommService commService;

    private List<Direction> directions = new ArrayList<>();

    private LookCommand lookCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        lookCommand = new LookCommand(
            actorRepository,
            roomRepository,
            commService,
            directions
        );
    }

    @Test
    public void testInvokeNonCreature() {
        lookCommand.invoke(actor, output);

        verify(output).append(contains("Alas"));
    }
}
