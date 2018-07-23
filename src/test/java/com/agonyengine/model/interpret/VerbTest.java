package com.agonyengine.model.interpret;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VerbTest {
    private Verb verb;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        verb = new Verb();
    }

    @Test
    public void testName() {
        verb.setName("name");

        assertEquals("name", verb.getName());
    }

    @Test
    public void testPriority() {
        verb.setPriority(100);

        assertEquals(100, verb.getPriority());
    }

    @Test
    public void testQuoting() {
        verb.setQuoting(true);

        assertTrue(verb.isQuoting());
    }

    @Test
    public void testBean() {
        verb.setBean("verbBean");

        assertEquals("verbBean", verb.getBean());
    }
}
