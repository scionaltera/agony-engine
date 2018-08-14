package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.Connection;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.service.CommService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StompDisconnectListenerTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private CommService commService;

    @Mock
    private SessionDisconnectEvent disconnectEvent;

    @Mock
    private Principal principal;

    @Mock
    private Actor actor;

    @Mock
    private Connection connection;

    private Message<byte[]> message;

    private StompDisconnectListener stompDisconnectListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        message = buildMockMessage();

        when(actor.getConnection()).thenReturn(connection);

        stompDisconnectListener = new StompDisconnectListener(
            actorRepository,
            commService);
    }

    @Test
    public void testApplicationEvent() {
        when(disconnectEvent.getMessage()).thenReturn(message);
        when(disconnectEvent.getSessionId()).thenReturn("SessionId");
        when(principal.getName()).thenReturn("SessionUser");
        when(actorRepository.findByConnectionSessionUsernameAndConnectionSessionId(eq("SessionUser"), eq("SessionId"))).thenReturn(actor);
        when(actor.getName()).thenReturn("Stan");
        when(connection.getRemoteIpAddress()).thenReturn("10.11.12.13");
        stompDisconnectListener.onApplicationEvent(disconnectEvent);

        verify(commService).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));
        verify(connection).setDisconnectedDate(any(Date.class));
        verify(actorRepository).save(eq(actor));
    }

    @Test
    public void testActorNotFound() {
        when(disconnectEvent.getMessage()).thenReturn(message);
        when(disconnectEvent.getSessionId()).thenReturn("SessionId");
        when(principal.getName()).thenReturn("SessionUser");
        when(actorRepository.findByConnectionSessionUsernameAndConnectionSessionId(eq("SessionUser"), eq("SessionId"))).thenReturn(null);

        stompDisconnectListener.onApplicationEvent(disconnectEvent);

        verify(actorRepository, never()).delete(eq(actor));
    }

    private Message<byte[]> buildMockMessage() {
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> sessionAttributes = new HashMap<>();

        sessionAttributes.put("SPRING.SESSION.ID", "springSessionId");

        headers.put(SimpMessageHeaderAccessor.USER_HEADER, principal);
        headers.put(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES, sessionAttributes);

        return new GenericMessage<>(new byte[0], headers);
    }
}
