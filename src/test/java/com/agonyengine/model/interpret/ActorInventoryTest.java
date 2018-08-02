package com.agonyengine.model.interpret;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.GameMap;
import com.agonyengine.repository.ActorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

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

    @Mock
    private GameMap gameMap;

    private ActorInventory actorInventory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actor.getInventory()).thenReturn(gameMap);
        when(actor.getNameTokens()).thenReturn(new String[] {"Stu"});
        when(target.getNameTokens()).thenReturn(new String[] {"sword"});

        when(
            actorRepository.findByGameMap(eq(gameMap))
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
