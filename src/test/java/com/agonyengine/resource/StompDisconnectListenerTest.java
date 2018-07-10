package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StompDisconnectListenerTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private SessionDisconnectEvent disconnectEvent;

    @Mock
    private Principal principal;

    @Mock
    private Actor actor;

    private Message<byte[]> message;

    private StompDisconnectListener stompDisconnectListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        message = buildMockMessage();

        stompDisconnectListener = new StompDisconnectListener(actorRepository);
    }

    @Test
    public void testApplicationEvent() {
        when(disconnectEvent.getMessage()).thenReturn(message);
        when(disconnectEvent.getSessionId()).thenReturn("SessionId");
        when(principal.getName()).thenReturn("SessionUser");
        when(actorRepository.findBySessionUsernameAndSessionId(eq("SessionUser"), eq("SessionId"))).thenReturn(actor);
        when(actor.getName()).thenReturn("Stan");

        stompDisconnectListener.onApplicationEvent(disconnectEvent);

        verify(actorRepository).delete(eq(actor));
    }

    @Test
    public void testActorNotFound() {
        when(disconnectEvent.getMessage()).thenReturn(message);
        when(disconnectEvent.getSessionId()).thenReturn("SessionId");
        when(principal.getName()).thenReturn("SessionUser");
        when(actorRepository.findBySessionUsernameAndSessionId(eq("SessionUser"), eq("SessionId"))).thenReturn(null);

        stompDisconnectListener.onApplicationEvent(disconnectEvent);

        verify(actorRepository, never()).delete(eq(actor));
    }

    private Message<byte[]> buildMockMessage() {
        Map<String, Object> headers = new HashMap<>();

        headers.put(SimpMessageHeaderAccessor.USER_HEADER, principal);

        return new GenericMessage<>(new byte[0], headers);
    }
}
