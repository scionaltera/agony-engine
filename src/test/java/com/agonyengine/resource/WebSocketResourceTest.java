package com.agonyengine.resource;

import com.agonyengine.model.actor.PlayerActorTemplate;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.PlayerActorTemplateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.agonyengine.resource.WebSocketResource.SPRING_SESSION_ID_KEY;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class WebSocketResourceTest {
    @Mock
    private InputTokenizer inputTokenizer;

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

    @Mock
    private Principal principal;

    private List<String[]> sentences = new ArrayList<>();
    private UUID sessionId = UUID.randomUUID();
    private Map<String, Object> headers = new HashMap<>();
    private Map<String, Object> sessionAttributes = new HashMap<>();
    private Message<byte[]> message;

    private WebSocketResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sentences.add(new String[] { "ALPHA" });
        message = buildMockMessage(sessionId.toString());

        when(principal.getName()).thenReturn("Shepherd");
        when(pat.getAccount()).thenReturn("Dude007");
        when(pat.getGivenName()).thenReturn("Frank");
        when(sessionRepository.findById(eq(sessionId.toString()))).thenReturn(session);

        resource = new WebSocketResource(
            "0.1.2-UNIT-TEST",
            new Date(),
            inputTokenizer,
            sessionRepository,
            playerActorTemplateRepository);
    }

    @Test
    public void testOnSubscribe() {
        GameOutput output = resource.onSubscribe();

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Non Breaking Space Greeting.".replace(" ", "&nbsp;"))));

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Breaking Space Greeting.")));
    }

    @Test
    public void testOnInput() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Alpha!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Alpha!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));

        GameOutput output = resource.onInput(principal, input, message);

        assertTrue(output.getOutput().size() >= 3);
    }

    @Test
    public void testOnInputNoSession() {
        Message<byte[]> badMessage = buildMockMessage("fakeId");

        when(input.getInput()).thenReturn("Alpha!");

        GameOutput output = resource.onInput(principal, input, badMessage);

        assertNotNull(output);

        verifyZeroInteractions(playerActorTemplateRepository);
    }

    @Test
    public void testOnInputNoPat() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Alpha!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Alpha!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.empty());

        GameOutput output = resource.onInput(principal, input, message);

        assertTrue(output.getOutput().size() >= 3);
    }

    private Message<byte[]> buildMockMessage(String id) {
        sessionAttributes.put(SPRING_SESSION_ID_KEY, id);

        headers.put(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES, sessionAttributes);

        return new GenericMessage<>(new byte[0], headers);
    }
}
