package com.agonyengine.resource;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.Connection;
import com.agonyengine.model.actor.CreatureInfo;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.actor.Pronoun;
import com.agonyengine.model.actor.Tile;
import com.agonyengine.model.actor.Tileset;
import com.agonyengine.model.command.SayCommand;
import com.agonyengine.model.generator.BodyGenerator;
import com.agonyengine.model.interpret.QuotedString;
import com.agonyengine.model.interpret.Verb;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.stomp.UserInput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.GameMapRepository;
import com.agonyengine.repository.TilesetRepository;
import com.agonyengine.repository.VerbRepository;
import com.agonyengine.resource.exception.NoSuchActorException;
import com.agonyengine.service.CommService;
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
import static org.junit.Assert.*;
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
    private ActorRepository actorRepository;

    @Mock
    private TilesetRepository tilesetRepository;

    @Mock
    private VerbRepository verbRepository;

    @Mock
    private Actor actor;

    @Mock
    private CreatureInfo creatureInfo;

    @Mock
    private Connection connection;

    @Mock
    private Pronoun pronoun;

    @Mock
    private GameMap gameMap;

    @Mock
    private Tileset tileset;

    @Mock
    private Tile tile;

    @Mock
    private Session session;

    @Mock
    private UserInput input;

    @Mock
    private Principal principal;

    @Mock
    private InvokerService invokerService;

    @Mock
    private CommService commService;

    @Mock
    private BodyGenerator bodyGenerator;

    @Captor
    private ArgumentCaptor<List> listCaptor;

    @Captor
    private ArgumentCaptor<GameMap> gameMapCaptor;

    private UUID defaultMapId = UUID.randomUUID();
    private UUID inventoryTilesetId = UUID.randomUUID();
    private List<List<String>> sentences = new ArrayList<>();
    private String remoteIpAddress = "10.11.12.13";
    private UUID sessionId = UUID.randomUUID();
    private UUID actorId = UUID.randomUUID();
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
        when(actor.getConnection()).thenReturn(connection);
        when(actor.getCreatureInfo()).thenReturn(creatureInfo);
        when(actorRepository.findByConnectionSessionUsernameAndConnectionSessionId(eq("Shepherd"), eq(sessionId.toString()))).thenReturn(actor);
        when(actorRepository.findById(eq(actorId))).thenReturn(Optional.of(actor));
        when(sessionRepository.findById(eq(sessionId.toString()))).thenReturn(session);
        when(session.getAttribute(eq("remoteIpAddress"))).thenReturn(remoteIpAddress);
        when(gameMapRepository.getOne(eq(defaultMapId))).thenReturn(gameMap);
        when(tilesetRepository.getOne(any(UUID.class))).thenReturn(tileset);
        when(tileset.getTile(anyInt())).thenReturn(tile);

        when(actorRepository.save(any(Actor.class))).thenAnswer(i -> {
            Actor a = i.getArgument(0);

            a.setId(UUID.randomUUID());

            return a;
        });

        when(gameMapRepository.save(any(GameMap.class))).thenAnswer(i -> {
            GameMap m = i.getArgument(0);

            m.setId(UUID.randomUUID());

            return m;
        });

        verbs = buildMockVerbs();

        resource = new WebSocketResource(
            "0.1.2-UNIT-TEST",
            new Date(),
            defaultMapId,
            inventoryTilesetId,
            inputTokenizer,
            gameMapRepository,
            sessionRepository,
            actorRepository,
            tilesetRepository,
            invokerService,
            commService,
            bodyGenerator);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnSubscribeReconnectInWorld() {
        GameMap inventory = mock(GameMap.class);

        when(actor.getInventory()).thenReturn(inventory);
        when(actor.getGameMap()).thenReturn(gameMap);

        GameOutput output = resource.onSubscribe(principal, message, actorId.toString());

        verify(actor, never()).setInventory(any());
        verify(actor, never()).setGameMap(any());
        verify(actor, never()).setX(any());
        verify(actor, never()).setY(any());

        verify(connection).setSessionUsername(eq("Shepherd"));
        verify(connection).setSessionId(anyString());
        verify(connection).setRemoteIpAddress(anyString());
        verify(connection).setDisconnectedDate(isNull());

        verify(commService).echo(eq(actor), any(GameOutput.class));
        verify(commService).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));

        verify(actorRepository).save(actor);

        verify(invokerService).invoke(eq(actor), any(GameOutput.class), isNull(), listCaptor.capture());

        List<String> invokeList = listCaptor.getValue();

        assertEquals(1, invokeList.size());
        assertEquals("look", invokeList.get(0));

        assertTrue(output.getOutput().stream()
            .noneMatch(line -> line.equals("Non Breaking Space Greeting.".replace(" ", "&nbsp;"))));

        assertTrue(output.getOutput().stream()
            .noneMatch(line -> line.equals("Breaking Space Greeting.")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnSubscribeReconnectInVoid() {
        GameMap inventory = mock(GameMap.class);

        when(actor.getInventory()).thenReturn(inventory);
        when(actor.getGameMap()).thenReturn(null);

        GameOutput output = resource.onSubscribe(principal, message, actorId.toString());

        verify(actor, never()).setInventory(any());
        verify(actor).setGameMap(eq(gameMap));
        verify(actor).setX(0);
        verify(actor).setY(0);

        verify(connection).setSessionUsername(eq("Shepherd"));
        verify(connection).setSessionId(anyString());
        verify(connection).setRemoteIpAddress(anyString());
        verify(connection).setDisconnectedDate(isNull());

        verify(commService).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));

        verify(actorRepository).save(actor);

        verify(invokerService).invoke(eq(actor), any(GameOutput.class), isNull(), listCaptor.capture());

        List<String> invokeList = listCaptor.getValue();

        assertEquals(1, invokeList.size());
        assertEquals("look", invokeList.get(0));

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Non Breaking Space Greeting.".replace(" ", "&nbsp;"))));

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Breaking Space Greeting.")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnSubscribeFirstTimeConnect() {
        when(connection.getAccount()).thenReturn("Shepherd");
        when(actor.getPronoun()).thenReturn(pronoun);
        when(actor.getInventory()).thenReturn(null);
        when(actor.getGameMap()).thenReturn(null);

        GameOutput output = resource.onSubscribe(principal, message, actorId.toString());

        verify(actor).setInventory(gameMapCaptor.capture());
        verify(actor).setGameMap(eq(gameMap));
        verify(actor).setX(0);
        verify(actor).setY(0);

        verify(connection).setSessionUsername(eq("Shepherd"));
        verify(connection).setSessionId(anyString());
        verify(connection).setRemoteIpAddress(anyString());
        verify(connection).setDisconnectedDate(isNull());

        verify(commService).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));

        verify(actorRepository).save(actor);

        verify(invokerService).invoke(eq(actor), any(GameOutput.class), isNull(), listCaptor.capture());

        List<String> invokeList = listCaptor.getValue();

        assertEquals(1, invokeList.size());
        assertEquals("look", invokeList.get(0));

        GameMap inventory = gameMapCaptor.getValue();

        assertEquals(tile, inventory.getTile(0, 0));

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Non Breaking Space Greeting.".replace(" ", "&nbsp;"))));

        assertTrue(output.getOutput().stream()
            .anyMatch(line -> line.equals("Breaking Space Greeting.")));
    }

    @Test
    public void testOnSubscribeBodyUpgrade() {
        when(creatureInfo.getBodyVersion()).thenReturn(-1);

        resource.onSubscribe(principal, message, actorId.toString());

        verify(actor).setCreatureInfo(isNull());
        verify(actorRepository).save(eq(actor));
    }

    @Test
    public void testOnSubscribeGenerateBody() {
        when(actor.getCreatureInfo()).thenReturn(null);

        resource.onSubscribe(principal, message, actorId.toString());

        verify(bodyGenerator).generate(any());
        verify(actor).setCreatureInfo(any(CreatureInfo.class));
        verify(actorRepository).save(eq(actor));
    }

    @Test(expected = NoSuchActorException.class)
    public void testOnSubscribeNoTemplate() {
        when(actorRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        resource.onSubscribe(principal, message, actorId.toString());
    }

    @Test
    public void testOnInput() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Able!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(actorRepository.findById(eq(actorId))).thenReturn(Optional.of(actor));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("ABLE"))).thenReturn(Optional.of(verbs.get(0)));

        GameOutput output = resource.onInput(principal, input, message);

        assertNotNull(output);
        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), any(UserInput.class), anyList());
    }

    @Test
    public void testOnInputReconnected() {
        when(actorRepository.findByConnectionSessionUsernameAndConnectionSessionId(any(), any())).thenReturn(null);

        GameOutput output = resource.onInput(principal, input, message);

        verify(inputTokenizer, never()).tokenize(any());

        assertTrue(output.getOutput().stream().anyMatch(line -> line.contains("another browser")));
    }

    @Test
    public void testOnInputMultipleSentences() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Able. Baker. Charlie."))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able. Baker. Charlie.");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(actorRepository.findById(eq(actorId))).thenReturn(Optional.of(actor));
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
        when(actorRepository.findById(eq(actorId))).thenReturn(Optional.of(actor));
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
        when(actorRepository.findById(eq(actorId))).thenReturn(Optional.of(actor));
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
        when(actorRepository.findById(eq(actorId))).thenReturn(Optional.of(actor));
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
        when(actorRepository.findById(eq(actorId))).thenReturn(Optional.of(actor));
        when(verbRepository.findFirstByNameIgnoreCaseStartingWith(any(Sort.class), eq("ABLE"))).thenReturn(Optional.of(verbs.get(0)));
        when(verbs.get(0).isQuoting()).thenReturn(true);
        when(applicationContext.getBean(eq("ableCommand"))).thenReturn(alphaBean);

        GameOutput output = resource.onInput(principal, input, message);

        assertNotNull(output);
        verify(invokerService).invoke(any(Actor.class), any(GameOutput.class), any(UserInput.class), anyList());
    }

    @Test
    public void testOnInputNoActor() {
        UUID actorId = UUID.randomUUID();

        when(inputTokenizer.tokenize(eq("Able!"))).thenReturn(sentences);
        when(input.getInput()).thenReturn("Able!");
        when(session.getAttribute(eq("actor"))).thenReturn(actorId.toString());
        when(actorRepository.findById(eq(actorId))).thenReturn(Optional.empty());

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

            output.append("Test Passed: " + message.getToken());

            return null;
        }).when(alphaBean).invoke(any(Actor.class), any(GameOutput.class), any(QuotedString.class));
    }
}
