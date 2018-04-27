package com.agonyengine.core.resource;

import com.agonyengine.core.model.actor.PlayerActorTemplate;
import com.agonyengine.core.model.stomp.GameOutput;
import com.agonyengine.core.model.stomp.UserInput;
import com.agonyengine.core.repository.PlayerActorTemplateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.agonyengine.core.resource.WebSocketResource.SPRING_SESSION_ID_KEY;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WebSocketResourceTest {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PlayerActorTemplateRepository playerActorTemplateRepository;

    @Mock
    private PlayerActorTemplate pat;

    @Mock
    private Session session;

    @Mock
    private UserInput input;

    private UUID sessionId = UUID.randomUUID();
    private Map<String, Object> headers = new HashMap<>();
    private Map<String, Object> sessionAttributes = new HashMap<>();
    private Message<byte[]> message;

    private WebSocketResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        message = buildMockMessage(sessionId.toString());

        when(pat.getAccount()).thenReturn("Dude007");
        when(pat.getGivenName()).thenReturn("Frank");
        when(sessionRepository.findById(eq(sessionId.toString()))).thenReturn(session);

        resource = new WebSocketResource(sessionRepository, playerActorTemplateRepository);
    }

    @Test
    public void testOnSubscribe() {
        GameOutput output = resource.onSubscribe();

        assertTrue(output.getOutput().stream().anyMatch(line -> line.equals("Subscribed!")));
    }

    @Test
    public void testOnInput() {
        UUID actorId = UUID.randomUUID();

        when(input.getInput()).thenReturn("Alpha!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));

        GameOutput output = resource.onInput(input, message);

        assertTrue(output.getOutput().stream().anyMatch(line -> line.equals("Frank: Alpha!")));
    }

    @Test
    public void testOnInputNoSession() {
        Message<byte[]> badMessage = buildMockMessage("fakeId");

        when(input.getInput()).thenReturn("Alpha!");

        GameOutput output = resource.onInput(input, badMessage);

        assertNotNull(output);

        verifyZeroInteractions(playerActorTemplateRepository);
    }

    @Test
    public void testOnInputNoPat() {
        UUID actorId = UUID.randomUUID();

        when(input.getInput()).thenReturn("Alpha!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.empty());

        GameOutput output = resource.onInput(input, message);

        assertTrue(output.getOutput().stream().anyMatch(line -> line.equals("Someone: Alpha!")));
    }

    private Message<byte[]> buildMockMessage(String id) {
        sessionAttributes.put(SPRING_SESSION_ID_KEY, id);

        headers.put(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES, sessionAttributes);

        return new GenericMessage<>(new byte[0], headers);
    }
}
