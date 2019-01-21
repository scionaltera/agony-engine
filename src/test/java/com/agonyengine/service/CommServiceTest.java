package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.Connection;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

public class CommServiceTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private Actor actor;

    @Mock
    private Connection connection;

    @Mock
    private GameOutput output;

    private List<Actor> observers = new ArrayList<>();

    private CommService commService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actor.getConnection()).thenReturn(connection);
        when(connection.getSessionId()).thenReturn("sessionId");
        when(connection.getSessionUsername()).thenReturn("sessionUsername");

        for (int i = 0; i < 3; i++) {
            Actor o = mock(Actor.class);
            Connection c = mock(Connection.class);

            when(o.getConnection()).thenReturn(c);
            when(c.getSessionId()).thenReturn("sessionId-" + i);
            when(c.getSessionUsername()).thenReturn("sessionUser-" + i);

            observers.add(o);
        }

        when(actorRepository.findByRoomId(isNull())).thenReturn(observers);

        commService = new CommService(actorRepository, simpMessagingTemplate);
    }

    @Test
    public void testEcho() {
        commService.echo(actor, output);

        verify(output, atLeast(2)).append(anyString());
        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUsername"),
            eq("/queue/output"),
            eq(output)
        );
    }

    @Test
    public void testEchoToRoom() {
        commService.echoToRoom(actor, output);

        verify(output, atLeast(2)).append(anyString());
        verify(simpMessagingTemplate, never()).convertAndSendToUser(
            eq("sessionUsername"),
            eq("/queue/output"),
            eq(output)
        );

        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUser-0"),
            eq("/queue/output"),
            eq(output)
        );

        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUser-1"),
            eq("/queue/output"),
            eq(output)
        );

        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUser-2"),
            eq("/queue/output"),
            eq(output)
        );
    }
}
