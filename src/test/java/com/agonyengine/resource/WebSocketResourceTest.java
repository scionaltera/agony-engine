package com.agonyengine.resource;

import com.agonyengine.model.actor.PlayerActorTemplate;
import com.agonyengine.model.command.HelpCommand;
import com.agonyengine.model.interpret.Verb;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.PlayerActorTemplateRepository;
import com.agonyengine.repository.VerbRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
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
import static org.mockito.Mockito.*;

public class WebSocketResourceTest {
    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private InputTokenizer inputTokenizer;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PlayerActorTemplateRepository playerActorTemplateRepository;

    @Mock
    private VerbRepository verbRepository;

    @Mock
    private PlayerActorTemplate pat;

    @Mock
    private Session session;

    @Mock
    private UserInput input;

    @Mock
    private Principal principal;

    private List<List<String>> sentences = new ArrayList<>();
    private UUID sessionId = UUID.randomUUID();
    private Map<String, Object> headers = new HashMap<>();
    private Map<String, Object> sessionAttributes = new HashMap<>();
    private Message<byte[]> message;
    private List<Verb> verbs;

    private WebSocketResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sentences.add(Collections.singletonList("ALPHA"));
        message = buildMockMessage(sessionId.toString());

        when(principal.getName()).thenReturn("Shepherd");
        when(pat.getAccount()).thenReturn("Dude007");
        when(pat.getGivenName()).thenReturn("Frank");
        when(sessionRepository.findById(eq(sessionId.toString()))).thenReturn(session);

        verbs = buildMockVerbs();

        resource = new WebSocketResource(
            "0.1.2-UNIT-TEST",
            new Date(),
            applicationContext,
            inputTokenizer,
            sessionRepository,
            playerActorTemplateRepository,
            verbRepository);
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
        HelpCommand alphaBean = mock(HelpCommand.class);

        doAnswer(i -> {
            GameOutput output = i.getArgument(0);

            output.append("Test Passed");

            return null;
        }).when(alphaBean).invoke(any(GameOutput.class));

        when(inputTokenizer.tokenize(eq("Able!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findAll(any(Sort.class))).thenReturn(verbs);
        when(applicationContext.getBean(eq("alphaCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input, message);

        assertTrue(output.getOutput().stream().anyMatch("Test Passed"::equals));

        verify(applicationContext).getBean(eq("alphaCommand"));
        verify(alphaBean).invoke(any(GameOutput.class));
    }

    @Test
    public void testOnInputNoCommand() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Able!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findAll(any(Sort.class))).thenReturn(verbs);
        when(applicationContext.getBean(eq("alphaCommand")))
            .thenThrow(new NoSuchBeanDefinitionException("alphaCommand"));

        GameOutput output = resource.onInput(principal, input, message);

        assertTrue(output.getOutput().stream().anyMatch("[dwhite]No bean named 'alphaCommand' available"::equals));

        verify(applicationContext).getBean(eq("alphaCommand"));
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

    private List<Verb> buildMockVerbs() {
        String[] names = new String[] {"alpha", "bravo", "charlie"};
        List<Verb> verbs = new ArrayList<>();

        for (String name : names) {
            Verb verb = mock(Verb.class);

            when(verb.getName()).thenReturn(name);
            when(verb.getPriority()).thenReturn(500);
            when(verb.getBean()).thenReturn(name + "Command");

            verbs.add(verb);
        }

        return verbs;
    }
}
