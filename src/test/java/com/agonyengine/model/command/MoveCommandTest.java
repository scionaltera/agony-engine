package com.agonyengine.model.command;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.BodyPartCapability;
import com.agonyengine.model.actor.CreatureInfo;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.actor.Tile;
import com.agonyengine.model.actor.TileFlag;
import com.agonyengine.model.map.Room;
import com.agonyengine.model.map.RoomLocation;
import com.agonyengine.model.stomp.GameOutput;
import com.agonyengine.repository.ActorRepository;
import com.agonyengine.repository.RoomRepository;
import com.agonyengine.service.CommService;
import com.agonyengine.service.InvokerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MoveCommandTest {
    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private InvokerService invokerService;

    @Mock
    private CommService commService;

    @Mock
    private CreatureInfo creatureInfo;

    @Mock
    private GameMap gameMap;

    @Mock
    private Tile tile;

    @Mock
    private GameOutput output;

    @Captor
    private ArgumentCaptor<List<String>> listCaptor;

    private Actor actor = new Actor();
    private Direction direction = new Direction("north", "south", 0, 1, 0);
    private Room currentRoom = new Room();
    private Room destinationRoom = new Room();

    private MoveCommand moveCommand;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        currentRoom.setId(UUID.randomUUID());
        currentRoom.setLocation(new RoomLocation(0L, 0L));

        destinationRoom.setId(UUID.randomUUID());
        destinationRoom.setLocation(new RoomLocation((long)direction.getX(), (long)direction.getY()));

        actor.setName("Frank");
        actor.setRoomId(currentRoom.getId());
        actor.setGameMap(gameMap);
        actor.setX(0);
        actor.setY(0);
        actor.setCreatureInfo(creatureInfo);

        when(applicationContext.getBean(eq("actorRepository"), eq(ActorRepository.class))).thenReturn(actorRepository);
        when(applicationContext.getBean(eq("roomRepository"), eq(RoomRepository.class))).thenReturn(roomRepository);
        when(applicationContext.getBean(eq("invokerService"), eq(InvokerService.class))).thenReturn(invokerService);
        when(applicationContext.getBean(eq("commService"), eq(CommService.class))).thenReturn(commService);

        when(roomRepository.findById(eq(currentRoom.getId()))).thenReturn(Optional.of(currentRoom));
        when(roomRepository.findByLocationXAndLocationYAndLocationZ(0L, 1L, 0L)).thenReturn(Optional.of(destinationRoom));

        when(creatureInfo.hasCapability(eq(BodyPartCapability.WALK))).thenReturn(true);

        when(tile.getFlags()).thenReturn(EnumSet.noneOf(TileFlag.class));

        when(gameMap.hasTile(anyInt(), anyInt())).thenReturn(true);
        when(gameMap.getTile(anyInt(), anyInt())).thenReturn(tile);

        moveCommand = new MoveCommand(direction, applicationContext);
        moveCommand.postConstruct();
    }

    @Test
    public void testInvoke() {
        moveCommand.invoke(actor, output);

        verify(commService, times(2)).echoToRoom(eq(actor), any(GameOutput.class), eq(actor));

        assertEquals(destinationRoom.getId(), actor.getRoomId());

        verify(actorRepository).save(actor);

        verify(invokerService).invoke(eq(actor), eq(output), isNull(), listCaptor.capture());

        List<String> args = listCaptor.getValue();

        assertEquals(1, args.size());
        assertEquals("look", args.get(0));
    }

    @Test
    public void testInvokeNonCreature() {
        actor.setCreatureInfo(null);

        moveCommand.invoke(actor, output);

        verify(output).append(contains("Alas"));
    }

    @Test
    public void testInvokeIntoNonexistentRoom() {
        when(roomRepository.findByLocationXAndLocationYAndLocationZ(0L, 1L, 0L)).thenReturn(Optional.empty());

        moveCommand.invoke(actor, output);

        verify(output).append(contains("Alas, you cannot go that way."));

        verify(commService, never()).echoToRoom(any(), any(), any());

        assertEquals(currentRoom.getId(), actor.getRoomId());

        verify(actorRepository, never()).save(any());

        verify(invokerService, never()).invoke(any(), any(), any(), any());
    }

    @Test
    public void testInvokeIncapableOfWalking() {
        when(creatureInfo.hasCapability(eq(BodyPartCapability.WALK))).thenReturn(false);

        moveCommand.invoke(actor, output);

        verify(output).append(contains("Alas, you are unable to walk."));

        verify(commService, never()).echoToRoom(any(), any(), any());

        assertEquals(0L, (long)actor.getX());
        assertEquals(0L, (long)actor.getY());

        verify(actorRepository, never()).save(any());

        verify(invokerService, never()).invoke(any(), any(), any(), any());
    }
}
