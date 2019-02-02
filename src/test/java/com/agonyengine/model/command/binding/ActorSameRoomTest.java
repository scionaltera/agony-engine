package com.agonyengine.model.command.binding;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ActorSameRoomTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private Actor actor;

    @Mock
    private Actor target;

    private ActorSameRoom actorSameRoom;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        UUID roomId = UUID.randomUUID();

        when(actor.getNameTokens()).thenReturn(new String[] {"Stu"});
        when(actor.getRoomId()).thenReturn(roomId);

        when(target.getNameTokens()).thenReturn(new String[] {"Dave"});
        when(target.getRoomId()).thenReturn(roomId);

        when(
            actorRepository.findByRoomId(
                any(UUID.class)
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
