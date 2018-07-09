package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.PlayerActorTemplate;
import com.agonyengine.model.command.HelpCommand;
import com.agonyengine.model.command.SayCommand;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.interpret.Verb;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.GameMapRepository;
import com.agonyengine.repository.PlayerActorTemplateRepository;
import com.agonyengine.repository.VerbRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.agonyengine.resource.WebSocketResource.SPRING_SESSION_ID_KEY;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class WebSocketResourceTest {
    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private InputTokenizer inputTokenizer;

    @Mock
    private GameMapRepository gameMapRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PlayerActorTemplateRepository playerActorTemplateRepository;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private VerbRepository verbRepository;

    @Mock
    private PlayerActorTemplate pat;

    @Mock
    private Actor actor;

    @Mock
    private Session session;

    @Mock
    private UserInput input;

    @Mock
    private Principal principal;

    private UUID defaultMapId = UUID.randomUUID();
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
        when(actor.getName()).thenReturn("Frank");
        when(actorRepository.findBySessionUsername(eq("Shepherd"))).thenReturn(actor);
        when(sessionRepository.findById(eq(sessionId.toString()))).thenReturn(session);

        verbs = buildMockVerbs();

        resource = new WebSocketResource(
            "0.1.2-UNIT-TEST",
            new Date(),
            defaultMapId,
            applicationContext,
            inputTokenizer,
            gameMapRepository,
            sessionRepository,
            actorRepository,
            playerActorTemplateRepository,
            verbRepository);
    }

    @Test
    public void testOnSubscribe() {
        GameOutput output = resource.onSubscribe(principal, message);

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
            GameOutput output = i.getArgument(1);

            output.append("Test Passed");

            return null;
        }).when(alphaBean).invoke(any(Actor.class), any(GameOutput.class));

        when(inputTokenizer.tokenize(eq("Able!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findAll(any(Sort.class))).thenReturn(verbs);
        when(applicationContext.getBean(eq("alphaCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input);

        assertTrue(output.getOutput().stream().anyMatch("Test Passed"::equals));

        verify(applicationContext).getBean(eq("alphaCommand"));
        verify(alphaBean).invoke(any(Actor.class), any(GameOutput.class));
    }

    @Test
    public void testOnInputQuotingVerb() {
        UUID actorId = UUID.randomUUID();
        SayCommand alphaBean = mock(SayCommand.class);

        fakeCommandOutput(alphaBean);

        List<List<String>> sentences = new ArrayList<>();

        sentences.add(Arrays.asList("ALPHA", "BAKER", "CHARLIE", "DOG", "EASY", "FOX"));

        when(inputTokenizer.tokenize(eq("Able baker charlie dog easy fox."))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able baker charlie dog easy fox.");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findAll(any(Sort.class))).thenReturn(verbs);
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("alphaCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input);

        assertTrue(output.getOutput().stream().anyMatch("Test Passed: baker charlie dog easy fox."::equals));

        verify(applicationContext).getBean(eq("alphaCommand"));
        verify(alphaBean).invoke(any(Actor.class), any(GameOutput.class), any(QuotedString.class));
    }

    @Test
    public void testOnInputQuotingVerbEmpty() {
        UUID actorId = UUID.randomUUID();
        SayCommand alphaBean = mock(SayCommand.class);
        List<List<String>> sentences = new ArrayList<>();

        sentences.add(Collections.singletonList("ALPHA"));

        when(inputTokenizer.tokenize(eq("Able"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findAll(any(Sort.class))).thenReturn(verbs);
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("alphaCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input);

        assertTrue(output.getOutput().stream().anyMatch(line -> line.contains("cannot be empty")));

        verify(applicationContext, never()).getBean(eq("alphaCommand"));
        verify(alphaBean, never()).invoke(any(Actor.class), any(GameOutput.class), any(QuotedString.class));
    }

    @Test
    public void testOnInputQuotingVerbSeveralSpaces() {
        UUID actorId = UUID.randomUUID();
        SayCommand alphaBean = mock(SayCommand.class);

        fakeCommandOutput(alphaBean);

        List<List<String>> sentences = new ArrayList<>();

        sentences.add(Arrays.asList("ALPHA", "BAKER", "CHARLIE", "DOG", "EASY", "FOX"));

        when(inputTokenizer.tokenize(eq("Able      baker charlie dog easy fox."))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able      baker charlie dog easy fox.");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findAll(any(Sort.class))).thenReturn(verbs);
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("alphaCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input);

        assertTrue(output.getOutput().stream().anyMatch("Test Passed: baker charlie dog easy fox."::equals));

        verify(applicationContext).getBean(eq("alphaCommand"));
        verify(alphaBean).invoke(any(Actor.class), any(GameOutput.class), any(QuotedString.class));
    }

    @Test
    public void testOnInputQuotingVerbMultipleSentences() {
        UUID actorId = UUID.randomUUID();
        SayCommand alphaBean = mock(SayCommand.class);

        fakeCommandOutput(alphaBean);

        List<List<String>> sentences = new ArrayList<>();

        sentences.add(Arrays.asList("ALPHA", "BAKER"));
        sentences.add(Arrays.asList("CHARLIE", "DOG"));
        sentences.add(Arrays.asList("EASY", "FOX"));

        when(inputTokenizer.tokenize(eq("Able baker. Charlie dog. Easy fox."))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able baker. Charlie dog. Easy fox.");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findAll(any(Sort.class))).thenReturn(verbs);
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("alphaCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input);

        assertTrue(output.getOutput().stream().anyMatch("Test Passed: baker. Charlie dog. Easy fox."::equals));

        verify(applicationContext).getBean(eq("alphaCommand"));
        verify(alphaBean).invoke(any(Actor.class), any(GameOutput.class), any(QuotedString.class));
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

        GameOutput output = resource.onInput(principal, input);

        assertTrue(output.getOutput().stream().anyMatch("[dwhite]No bean named 'alphaCommand' available"::equals));

        verify(applicationContext).getBean(eq("alphaCommand"));
    }

    @Test
    public void testOnInputNoPat() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Alpha!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Alpha!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.empty());

        GameOutput output = resource.onInput(principal, input);

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

    private void fakeCommandOutput(SayCommand alphaBean) {
        doAnswer(i -> {
            GameOutput output = i.getArgument(1);
            QuotedString message = i.getArgument(2);

            output.append("Test Passed: " + message.getText());

            return null;
        }).when(alphaBean).invoke(any(Actor.class), any(GameOutput.class), any(QuotedString.class));
    }
}
