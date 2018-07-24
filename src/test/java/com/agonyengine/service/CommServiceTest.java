package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ID_HEADER;

public class CommServiceTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private Actor actor;

    @Mock
    private GameOutput output;

    @Captor
    private ArgumentCaptor<MessageHeaders> messageHeadersCaptor;

    private List<Actor> observers = new ArrayList<>();

    private CommService commService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actor.getSessionId()).thenReturn("sessionId");
        when(actor.getSessionUsername()).thenReturn("sessionUsername");

        for (int i = 0; i < 3; i++) {
            Actor o = mock(Actor.class);

            when(o.getSessionId()).thenReturn("sessionId-" + i);
            when(o.getSessionUsername()).thenReturn("sessionUser-" + i);

            observers.add(o);
        }

        when(actorRepository.findByGameMapAndXAndY(isNull(), anyInt(), anyInt())).thenReturn(observers);

        commService = new CommService(actorRepository, simpMessagingTemplate);
    }

    @Test
    public void testEcho() {
        commService.echo(actor, output);

        verify(output, atLeast(2)).append(anyString());
        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUsername"),
            eq("/queue/output"),
            eq(output),
            messageHeadersCaptor.capture()
        );

        MessageHeaders headers = messageHeadersCaptor.getValue();

        assertEquals("sessionId", headers.get(SESSION_ID_HEADER));
    }

    @Test
    public void testEchoToRoom() {
        commService.echoToRoom(actor, output);

        verify(output, atLeast(2)).append(anyString());
        verify(simpMessagingTemplate, never()).convertAndSendToUser(
            eq("sessionUsername"),
            eq("/queue/output"),
            eq(output),
            any(MessageHeaders.class)
        );

        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUser-0"),
            eq("/queue/output"),
            eq(output),
            messageHeadersCaptor.capture()
        );

        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUser-1"),
            eq("/queue/output"),
            eq(output),
            messageHeadersCaptor.capture()
        );

        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUser-2"),
            eq("/queue/output"),
            eq(output),
            messageHeadersCaptor.capture()
        );

        List<MessageHeaders> headers = messageHeadersCaptor.getAllValues();

        assertEquals("sessionId-0", headers.get(0).get(SESSION_ID_HEADER));
        assertEquals("sessionId-1", headers.get(1).get(SESSION_ID_HEADER));
        assertEquals("sessionId-2", headers.get(2).get(SESSION_ID_HEADER));
    }
}
