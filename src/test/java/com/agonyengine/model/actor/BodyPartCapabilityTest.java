package com.agonyengine.model.actor;

import org.junit.Test;

import java.util.EnumSet;

import static com.agonyengine.model.actor.BodyPartCapability.HOLD;
import static com.agonyengine.model.actor.BodyPartCapability.SPEAK;
import static com.agonyengine.model.actor.BodyPartCapability.WALK;
import static org.junit.Assert.assertEquals;

public class BodyPartCapabilityTest {
    @Test
    public void testDescription() {
        assertEquals("speak", SPEAK.getDescription());
    }

    @Test
    public void testToLabels() {
        EnumSet<BodyPartCapability> bitfield = EnumSet.of(SPEAK, WALK, HOLD);

        assertEquals("speak, walk, hold items", BodyPartCapability.toLabels(bitfield));
    }
}
