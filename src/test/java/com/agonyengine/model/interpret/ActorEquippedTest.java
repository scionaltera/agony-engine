package com.agonyengine.model.interpret;

import com.agonyengine.model.actor.Actor;
import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.CreatureInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ActorEquippedTest {
    @Mock
    private Actor actor;

    @Mock
    private Actor equipment;

    @Mock
    private CreatureInfo creatureInfo;

    @Mock
    private BodyPart bodyPart;

    private ActorEquipped actorEquipped;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(actor.getCreatureInfo()).thenReturn(creatureInfo);
        when(creatureInfo.getBodyParts()).thenReturn(Collections.singletonList(bodyPart));

        actorEquipped = new ActorEquipped();
    }

    @Test
    public void testBind() {
        when(bodyPart.getArmor()).thenReturn(equipment);
        when(equipment.getNameTokens()).thenReturn(new String[] { "equipment" });

        String token = "equipment";

        assertTrue(actorEquipped.bind(actor, token));

        assertEquals(token, actorEquipped.getToken());
        assertEquals(equipment, actorEquipped.getTarget());
        assertTrue(actorEquipped.getWearLocations().contains(bodyPart));
    }

    @Test
    public void testBindNoCreatureInfo() {
        when(actor.getCreatureInfo()).thenReturn(null);

        String token = "equipment";

        assertFalse(actorEquipped.bind(actor, token));

        assertEquals(token, actorEquipped.getToken());

        assertNull(actorEquipped.getTarget());
        assertNull(actorEquipped.getWearLocations());
    }

    @Test
    public void testBindNotEquipped() {
        when(bodyPart.getArmor()).thenReturn(null);

        String token = "equipment";

        assertFalse(actorEquipped.bind(actor, token));

        assertEquals(token, actorEquipped.getToken());

        assertNull(actorEquipped.getTarget());
        assertNull(actorEquipped.getWearLocations());
    }
}
