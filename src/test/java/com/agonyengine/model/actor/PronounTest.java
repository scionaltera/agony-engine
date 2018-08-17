package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class PronounTest {
    private Pronoun pronoun;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        pronoun = new Pronoun();
    }

    @Test
    public void testSubject() {
        pronoun.setSubject("subject");

        assertEquals("subject", pronoun.getSubject());
    }

    @Test
    public void testObject() {
        pronoun.setObject("object");

        assertEquals("object", pronoun.getObject());
    }

    @Test
    public void testPossessive() {
        pronoun.setPossessive("possessive");

        assertEquals("possessive", pronoun.getPossessive());
    }

    @Test
    public void testPossessivePronoun() {
        pronoun.setPossessivePronoun("possessive pronoun");

        assertEquals("possessive pronoun", pronoun.getPossessivePronoun());
    }

    @Test
    public void testReflexive() {
        pronoun.setReflexive("reflexive");

        assertEquals("reflexive", pronoun.getReflexive());
    }
}
