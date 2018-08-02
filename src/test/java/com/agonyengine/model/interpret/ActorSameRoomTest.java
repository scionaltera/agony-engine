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
        when(actor.getNameTokens()).thenReturn(new String[] {"Stu"});
        when(target.getNameTokens()).thenReturn(new String[] {"Dave"});

        when(
            actorRepository.findByGameMapAndXAndY(
                eq(gameMap), anyInt(), anyInt()
            )).thenReturn(Arrays.asList(actor, target));

        actorSameRoom = new ActorSameRoom(actorRepository);
    }

    @Test
    public void testBindTargetFound() {
        boolean result = actorSameRoom.bind(actor, "DAVE");

        assertEquals("DAVE", actorSameRoom.getToken());
        assertEquals(target, actorSameRoom.getTarget());
        assertTrue(result);
    }

    @Test
    public void testBindTargetFoundMultiWord() {
        when(target.getNameTokens()).thenReturn(new String[] {"rubber", "ball"});

        boolean result = actorSameRoom.bind(actor, "BALL");

        assertEquals("BALL", actorSameRoom.getToken());
        assertEquals(target, actorSameRoom.getTarget());
        assertTrue(result);
    }

    @Test
    public void testBindTargetNotFound() {
        boolean result = actorSameRoom.bind(actor, "JOHN");

        assertEquals("JOHN", actorSameRoom.getToken());
        assertNull(actorSameRoom.getTarget());
        assertFalse(result);
    }

    @Test
    public void testGetSyntaxDescription() {
        assertEquals("target in same room", ActorSameRoom.getSyntaxDescription());
    }
}
