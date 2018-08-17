package com.agonyengine.model.actor;

import com.agonyengine.model.util.Bitfield;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.agonyengine.model.actor.BodyPartCapability.CAN_WALK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreatureInfoTest {
    private CreatureInfo creatureInfo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        creatureInfo = new CreatureInfo();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        creatureInfo.setId(id);

        assertEquals(id, creatureInfo.getId());
    }

    @Test
    public void testBodyParts() {
        List<BodyPart> bodyParts = new ArrayList<>();

        creatureInfo.setBodyParts(bodyParts);

        assertEquals(bodyParts, creatureInfo.getBodyParts());
    }

    @Test
    public void testHasCapability() {
        List<BodyPart> bodyParts = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            BodyPart part = mock(BodyPart.class);
            Bitfield capabilities = mock(Bitfield.class);

            when(part.getCapabilities()).thenReturn(capabilities);

            if (i == 0) {
                when(capabilities.isSet(eq(CAN_WALK.getIndex()))).thenReturn(true);
            }

            bodyParts.add(part);
        }

        creatureInfo.setBodyParts(bodyParts);

        assertTrue(creatureInfo.hasCapability(CAN_WALK));
    }
}
