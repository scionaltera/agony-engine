package com.agonyengine.model.actor;

import com.agonyengine.model.util.Bitfield;
import org.junit.Test;

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
        Bitfield bitfield = new Bitfield(SPEAK, WALK, HOLD);

        assertEquals("speak, walk, hold items", BodyPartCapability.toLabels(bitfield));
    }
}
