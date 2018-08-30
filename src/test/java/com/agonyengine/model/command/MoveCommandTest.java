package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.BodyPartCapability;
import com.agonyengine.model.actor.CreatureInfo;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.exit.Exit;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.model.util.Location;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.ExitRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MoveCommandTest {
    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private ExitRepository exitRepository;

    @Mock
    private InvokerService invokerService;

    @Mock
    private CommService commService;

    @Mock
    private Actor actor;

    @Mock
    private CreatureInfo creatureInfo;

    @Mock
    private GameMap gameMap;

    @Mock
    private GameOutput output;

    @Captor
    private ArgumentCaptor<List<String>> listCaptor;

    private Direction direction = new Direction("north", "south", 0, 1);

    private MoveCommand moveCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(applicationContext.getBean(eq("actorRepository"), eq(ActorRepository.class))).thenReturn(actorRepository);
        when(applicationContext.getBean(eq("exitRepository"), eq(ExitRepository.class))).thenReturn(exitRepository);
        when(applicationContext.getBean(eq("invokerService"), eq(InvokerService.class))).thenReturn(invokerService);
        when(applicationContext.getBean(eq("commService"), eq(CommService.class))).thenReturn(commService);

        when(actor.getName()).thenReturn("Frank");
        when(actor.getGameMap()).thenReturn(gameMap);
        when(actor.getCreatureInfo()).thenReturn(creatureInfo);
        when(actor.getX()).thenReturn(0);
        when(actor.getY()).thenReturn(0);

        when(creatureInfo.hasCapability(eq(BodyPartCapability.WALK))).thenReturn(true);

        when(gameMap.hasTile(anyInt(), anyInt())).thenReturn(true);

        moveCommand = new MoveCommand(direction, applicationContext);
        moveCommand.postConstruct();
    }

    @Test
    public void testInvoke() {
        moveCommand.invoke(actor, output);

        verify(gameMap).hasTile(0, 1);

        verify(commService, times(2)).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));

        verify(actor).setX(0);
        verify(actor).setY(1);

        verify(actorRepository).save(actor);

        verify(invokerService).invoke(eq(actor), eq(output), isNull(), listCaptor.capture());

        List<String> args = listCaptor.getValue();

        assertEquals(1, args.size());
        assertEquals("look", args.get(0));
    }

    @Test
    public void testInvokeNonCreature() {
        when(actor.getCreatureInfo()).thenReturn(null);

        moveCommand.invoke(actor, output);

        verify(output).append(contains("Alas"));
    }

    @Test
    public void testExit() {
        GameMap map = new GameMap();
        Exit exit = new Exit();
        Location destination = new Location();

        destination.setGameMap(map);
        destination.setX(4);
        destination.setY(5);

        exit.setDirection("north");
        exit.setDestination(destination);

        when(exitRepository.findByDirectionAndLocationGameMapAndLocationXAndLocationY(any(), any(), any(), any())).thenReturn(exit);

        moveCommand.invoke(actor, output);

        verify(commService, times(2)).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));

        verify(actor).setGameMap(eq(map));
        verify(actor).setX(eq(destination.getX()));
        verify(actor).setY(eq(destination.getY()));

        verify(actorRepository).save(actor);

        verify(invokerService).invoke(eq(actor), eq(output), isNull(), listCaptor.capture());

        List<String> args = listCaptor.getValue();

        assertEquals(1, args.size());
        assertEquals("look", args.get(0));
    }

    @Test
    public void testInvokeIllegalDirection() {
        when(gameMap.hasTile(0, 1)).thenReturn(false);

        moveCommand.invoke(actor, output);

        verify(output).append(contains("Alas, you cannot go that way."));

        verify(gameMap).hasTile(anyInt(), anyInt());

        verify(commService, never()).echoToRoom(any(), any(), any());

        verify(actor, never()).setX(anyInt());
        verify(actor, never()).setY(anyInt());

        verify(actorRepository, never()).save(any());

        verify(invokerService, never()).invoke(any(), any(), any(), any());
    }

    @Test
    public void testInvokeIncapableOfWalking() {
        when(creatureInfo.hasCapability(eq(BodyPartCapability.WALK))).thenReturn(false);

        moveCommand.invoke(actor, output);

        verify(output).append(contains("Alas, you are unable to walk."));

        verify(commService, never()).echoToRoom(any(), any(), any());

        verify(actor, never()).setX(anyInt());
        verify(actor, never()).setY(anyInt());

        verify(actorRepository, never()).save(any());

        verify(invokerService, never()).invoke(any(), any(), any(), any());
    }
}
