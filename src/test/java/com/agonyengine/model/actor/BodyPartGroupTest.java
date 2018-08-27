package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class BodyPartGroupTest {
    private BodyPartGroup bodyPartGroup;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        bodyPartGroup = new BodyPartGroup();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        bodyPartGroup.setId(id);

        assertEquals(id, bodyPartGroup.getId());
    }

    @Test
    public void testName() {
        String name = "bodyPartGroup";

        bodyPartGroup.setName(name);

        assertEquals(name, bodyPartGroup.getName());
    }

    @Test
    public void testBodyPartTemplates() {
        List<BodyPartTemplate> templates = new ArrayList<>();

        bodyPartGroup.setBodyPartTemplates(templates);

        assertEquals(templates, bodyPartGroup.getBodyPartTemplates());
    }
}
