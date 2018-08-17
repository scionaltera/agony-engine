package com.agonyengine.model.generator;

import com.agonyengine.model.actor.BodyPart;
import com.agonyengine.model.actor.BodyPartCapability;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class BodyGeneratorTest {
    private BodyGenerator bodyGenerator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        bodyGenerator = new BodyGenerator();
    }

    @Test
    public void testGenerate() {
        List<BodyPart> parts = bodyGenerator.generate("humanoid");

        assertTrue(parts
            .stream()
            .anyMatch(part -> part.getCapabilities().isSet(BodyPartCapability.CAN_WALK.getIndex())));
    }
}
