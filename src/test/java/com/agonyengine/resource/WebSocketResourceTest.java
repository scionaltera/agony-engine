package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.actor.PlayerActorTemplate;
import com.agonyengine.model.command.SayCommand;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.interpret.Verb;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.GameMapRepository;
import com.agonyengine.repository.PlayerActorTemplateRepository;
import com.agonyengine.repository.VerbRepository;
import com.agonyengine.service.InvokerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
    private GameMap gameMap;

    @Mock
    private Session session;

    @Mock
    private UserInput input;

    @Mock
    private Principal principal;

    @Mock
    private InvokerService invokerService;

    @Captor
    private ArgumentCaptor<Actor> actorCaptor;

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

        sentences.add(Collections.singletonList("ABLE"));
        message = buildMockMessage(sessionId.toString());

        when(principal.getName()).thenReturn("Shepherd");
        when(pat.getAccount()).thenReturn("Dude007");
        when(pat.getGivenName()).thenReturn("Frank");
        when(actor.getName()).thenReturn("Frank");
        when(actorRepository.findBySessionUsernameAndSessionId(eq("Shepherd"), eq(sessionId.toString()))).thenReturn(actor);
        when(sessionRepository.findById(eq(sessionId.toString()))).thenReturn(session);

        when(actorRepository.save(any(Actor.class))).thenAnswer(i -> {
            Actor a = i.getArgument(0);

            a.setId(UUID.randomUUID());

            return a;
        });

        verbs = buildMockVerbs();

        resource = new WebSocketResource(
            "0.1.2-UNIT-TEST",
            new Date(),
            defaultMapId,
            inputTokenizer,
            gameMapRepository,
            sessionRepository,
            actorRepository,
            playerActorTemplateRepository,
            invokerService);
    }

    @Test
    public void testOnSubscribe() {
        GameOutput output = resource.onSubscribe(principal, message);

        verify(invokerService).invoke(eq(actor), any(GameOutput.class), isNull(), anyList());

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Non Breaking Space Greeting.".replace(" ", "&nbsp;"))));

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Breaking Space Greeting.")));
    }

    @Test
    public void testOnSubscribeNullActor() {
        when(actorRepository.findBySessionUsernameAndSessionId(eq("Shepherd"), eq(sessionId.toString()))).thenReturn(null);
        when(playerActorTemplateRepository.findById(any(UUID.class))).thenReturn(Optional.of(pat));
        when(gameMapRepository.getOne(eq(defaultMapId))).thenReturn(gameMap);
        when(session.getAttribute(eq("actor_template"))).thenReturn(UUID.randomUUID().toString());

        GameOutput output = resource.onSubscribe(principal, message);

        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), isNull(), anyList());
        verify(actorRepository).save(actorCaptor.capture());

        Actor savedActor = actorCaptor.getValue();

        assertNotNull(savedActor.getId());
        assertEquals("Frank", savedActor.getName());
        assertEquals("Shepherd", savedActor.getSessionUsername());
        assertEquals(sessionId.toString(), savedActor.getSessionId());
        assertEquals(gameMap, savedActor.getGameMap());
        assertEquals((Integer)0, savedActor.getX());
        assertEquals((Integer)0, savedActor.getY());

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Non Breaking Space Greeting.".replace(" ", "&nbsp;"))));

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Breaking Space Greeting.")));
    }

    @Test
    public void testOnInput() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Able!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("ABLE"))).thenReturn(Optional.of(verbs.get(0)));

        GameOutput output = resource.onInput(principal, input, message);

        assertNotNull(output);
        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), any(UserInput.class), anyList());
    }

    @Test
    public void testOnInputMultipleSentences() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Able. Baker. Charlie."))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able. Baker. Charlie.");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("ABLE"))).thenReturn(Optional.of(verbs.get(0)));

        GameOutput output = resource.onInput(principal, input, message);

        assertNotNull(output);
        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), any(UserInput.class), anyList());
    }

    @Test
    public void testOnInputQuotingVerb() {
        UUID actorId = UUID.randomUUID();
        SayCommand alphaBean = mock(SayCommand.class);

        fakeCommandOutput(alphaBean);

        List<List<String>> sentences = new ArrayList<>();

        sentences.add(Arrays.asList("ABLE", "BAKER", "CHARLIE", "DOG", "EASY", "FOX"));

        when(inputTokenizer.tokenize(eq("Able baker charlie dog easy fox."))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able baker charlie dog easy fox.");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("ABLE"))).thenReturn(Optional.of(verbs.get(0)));
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("ableCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input, message);

        assertNotNull(output);
        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), any(UserInput.class), anyList());
    }

    @Test
    public void testOnInputQuotingVerbEmpty() {
        UUID actorId = UUID.randomUUID();
        SayCommand alphaBean = mock(SayCommand.class);
        List<List<String>> sentences = new ArrayList<>();

        sentences.add(Collections.singletonList("ABLE"));

        when(inputTokenizer.tokenize(eq("Able"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("ABLE"))).thenReturn(Optional.of(verbs.get(0)));
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("ableCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input, message);

        assertNotNull(output);
        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), any(UserInput.class), anyList());
    }

    @Test
    public void testOnInputQuotingVerbSeveralSpaces() {
        UUID actorId = UUID.randomUUID();
        SayCommand alphaBean = mock(SayCommand.class);

        fakeCommandOutput(alphaBean);

        List<List<String>> sentences = new ArrayList<>();

        sentences.add(Arrays.asList("ABLE", "BAKER", "CHARLIE", "DOG", "EASY", "FOX"));

        when(inputTokenizer.tokenize(eq("Able      baker charlie dog easy fox."))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able      baker charlie dog easy fox.");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("ABLE"))).thenReturn(Optional.of(verbs.get(0)));
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("ableCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input, message);

        assertNotNull(output);
        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), any(UserInput.class), anyList());
    }

    @Test
    public void testOnInputQuotingVerbMultipleSentences() {
        UUID actorId = UUID.randomUUID();
        SayCommand alphaBean = mock(SayCommand.class);

        fakeCommandOutput(alphaBean);

        List<List<String>> sentences = new ArrayList<>();

        sentences.add(Arrays.asList("ABLE", "BAKER"));
        sentences.add(Arrays.asList("CHARLIE", "DOG"));
        sentences.add(Arrays.asList("EASY", "FOX"));

        when(inputTokenizer.tokenize(eq("Able baker. Charlie dog. Easy fox."))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able baker. Charlie dog. Easy fox.");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.of(pat));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("ABLE"))).thenReturn(Optional.of(verbs.get(0)));
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("ableCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input, message);

        assertNotNull(output);
        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), any(UserInput.class), anyList());
    }

    @Test
    public void testOnInputNoPat() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Able!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(playerActorTemplateRepository.findById(eq(actorId))).thenReturn(Optional.empty());

        GameOutput output = resource.onInput(principal, input, message);

        assertTrue(output.getOutput().size() >= 2);
    }

    private Message<byte[]> buildMockMessage(String id) {
        sessionAttributes.put(SPRING_SESSION_ID_KEY, id);

        headers.put(SimpMessageHeaderAccessor.SESSION_ID_HEADER, sessionId.toString());
        headers.put(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES, sessionAttributes);

        return new GenericMessage<>(new byte[0], headers);
    }

    private List<Verb> buildMockVerbs() {
        String[] names = new String[] {"able", "baker", "charlie"};
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
