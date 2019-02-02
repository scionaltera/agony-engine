package com.agonyengine.model.command.impl;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.Connection;
import com.agonyengine.model.command.binding.QuotedString;
import com.agonyengine.stomp.model.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class QuitCommandTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private CommService commService;

    @Mock
    private Actor actor;

    @Mock
    private Connection connection;

    @Mock
    private GameOutput output;

    @Mock
    private QuotedString quotedString;

    private QuitCommand quitCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(quotedString.getToken()).thenReturn("NOW");
        when(actor.getConnection()).thenReturn(connection);
        when(actor.getName()).thenReturn("Scion");

        quitCommand = new QuitCommand(actorRepository, commService);
    }

    @Test
    public void testIncorrectArg() {
        when(quotedString.getToken()).thenReturn("PSYCHE");

        quitCommand.invoke(actor, output, quotedString);

        verify(actor, never()).setRoomId(isNull());
        verify(commService, never()).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));
        verify(actorRepository, never()).save(eq(actor));
        verify(output, never()).append(contains("window.location"));
    }

    @Test
    public void testQuit() {
        quitCommand.invoke(actor, output, quotedString);

        verify(output).append(contains("Goodbye, Scion!"));
        verify(output).append(contains("window.location"));
        verify(commService).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));
        verify(actor).setRoomId(isNull());
        verify(actorRepository).save(eq(actor));
    }
}
