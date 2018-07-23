package com.agonyengine.model.interpret;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ActorSameRoomTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private Actor actor;

    @Mock
    private Actor target;

    @Mock
    private GameMap gameMap;

    private ActorSameRoom actorSameRoom;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actor.getGameMap()).thenReturn(gameMap);
        when(actor.getName()).thenReturn("Stu");
        when(target.getName()).thenReturn("Dave");

        actorSameRoom = new ActorSameRoom(actorRepository);
    }

    @Test
    public void testBindTargetFound() {
        when(
            actorRepository.findByGameMapAndXAndY(
                eq(gameMap), anyInt(), anyInt()
            )).thenReturn(Arrays.asList(actor, target));

        boolean result = actorSameRoom.bind(actor, "DAVE");

        assertEquals("DAVE", actorSameRoom.getToken());
        assertEquals(target, actorSameRoom.getTarget());
        assertTrue(result);
    }

    @Test
    public void testBindTargetNotFound() {
        when(
            actorRepository.findByGameMapAndXAndY(
                eq(gameMap), anyInt(), anyInt()
            )).thenReturn(Arrays.asList(actor, target));

        boolean result = actorSameRoom.bind(actor, "JOHN");

        assertEquals("JOHN", actorSameRoom.getToken());
        assertNull(actorSameRoom.getTarget());
        assertFalse(result);
    }

    @Test
    public void testGetSyntaxDescription() {
        assertEquals("person in same room", ActorSameRoom.getSyntaxDescription());
    }
}
