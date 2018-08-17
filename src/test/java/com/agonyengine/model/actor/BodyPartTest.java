package com.agonyengine.model.actor;

import com.agonyengine.model.util.Bitfield;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class BodyPartTest {
    private BodyPart bodyPart;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        bodyPart = new BodyPart();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        bodyPart.setId(id);

        assertEquals(id, bodyPart.getId());
    }

    @Test
    public void testName() {
        bodyPart.setName("name");

        assertEquals("name", bodyPart.getName());
    }

    @Test
    public void testCapabilities() {
        Bitfield bitfield = new Bitfield();

        bodyPart.setCapabilities(bitfield);

        assertEquals(bitfield, bodyPart.getCapabilities());
    }
}
