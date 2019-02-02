package com.agonyengine.model.command.binding;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ActorInventoryTest {
    @Mock
    private ActorRepository actorRepository;

    @Mock
    private Actor actor;

    @Mock
    private Actor target;

    private UUID inventoryId = UUID.randomUUID();

    private ActorInventory actorInventory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actor.getInventoryId()).thenReturn(inventoryId);
        when(actor.getNameTokens()).thenReturn(new String[] {"Stu"});
        when(target.getNameTokens()).thenReturn(new String[] {"sword"});

        when(
            actorRepository.findByRoomId(eq(inventoryId))
        ).thenReturn(Collections.singletonList(target));

        actorInventory = new ActorInventory(actorRepository);
    }

    @Test
    public void testBindTargetFound() {
        boolean result = actorInventory.bind(actor, "SWORD");

        assertEquals("SWORD", actorInventory.getToken());
        assertEquals(target, actorInventory.getTarget());
        assertTrue(result);
    }

    @Test
    public void testBindTargetFoundMultiWord() {
        when(target.getNameTokens()).thenReturn(new String[] {"paperback", "book"});

        boolean result = actorInventory.bind(actor, "PAPER");

        assertEquals("PAPER", actorInventory.getToken());
        assertEquals(target, actorInventory.getTarget());
        assertTrue(result);
    }

    @Test
    public void testBindTargetNotFound() {
        boolean result = actorInventory.bind(actor, "SHIELD");

        assertEquals("SHIELD", actorInventory.getToken());
        assertNull(actorInventory.getTarget());
        assertFalse(result);
    }

    @Test
    public void testGetSyntaxDescription() {
        assertEquals("target in inventory", ActorInventory.getSyntaxDescription());
    }
}
