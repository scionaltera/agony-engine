package com.agonyengine.model.command.binding;

import com.agonyengine.model.actor.Actor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QuotedStringTest {
    @Mock
    private Actor actor;

    private QuotedString quotedString;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);



        quotedString = new QuotedString();
    }

    @Test
    public void testBindText() {
        boolean result = quotedString.bind(actor, "a box of oxen");

        assertEquals("a box of oxen", quotedString.getToken());
        assertTrue(result);
    }

    @Test
    public void testBindHtml() {
        boolean result = quotedString.bind(actor, "an ox of <em>boxen</em>");

        assertEquals("an ox of &lt;em&gt;boxen&lt;/em&gt;", quotedString.getToken());
        assertTrue(result);
    }

    @Test
    public void testBindEmptyString() {
        assertFalse(quotedString.bind(actor, ""));
    }

    @Test
    public void testGetSyntaxDescription() {
        assertEquals("quoted text", QuotedString.getSyntaxDescription());
    }
}
