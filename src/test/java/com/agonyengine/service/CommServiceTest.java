package com.agonyengine.service;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.stomp.GameOutput;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ID_HEADER;

public class CommServiceTest {
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Mock
    private Actor actor;

    @Mock
    private GameOutput output;

    @Captor
    private ArgumentCaptor<MessageHeaders> messageHeadersCaptor;

    private CommService commService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actor.getSessionId()).thenReturn("sessionId");
        when(actor.getSessionUsername()).thenReturn("sessionUsername");

        commService = new CommService(simpMessagingTemplate);
    }

    @Test
    public void testEcho() {
        commService.echo(actor, output);

        verify(simpMessagingTemplate).convertAndSendToUser(
            eq("sessionUsername"),
            eq("/queue/output"),
            eq(output),
            messageHeadersCaptor.capture()
        );

        MessageHeaders headers = messageHeadersCaptor.getValue();

        assertEquals("sessionId", headers.get(SESSION_ID_HEADER));

    }
}
