package com.agonyengine.web;

import com.agonyengine.stomp.model.StompPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniqueHandshakeHandlerTest {
    @Mock
    private ServerHttpRequest httpRequest;

    @Mock
    private WebSocketHandler webSocketHandler;

    private Map<String, Object> attributes = new HashMap<>();

    private UniqueHandshakeHandler handler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        handler = new UniqueHandshakeHandler();
    }

    @Test
    public void testDetermineUser() {
        Principal principal = handler.determineUser(httpRequest, webSocketHandler, attributes);

        assertTrue(principal instanceof StompPrincipal);

        UUID uuid = UUID.fromString(principal.getName());

        assertEquals(4, uuid.version());
    }
}
