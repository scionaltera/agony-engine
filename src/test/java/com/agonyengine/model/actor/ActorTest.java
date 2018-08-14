package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class ActorTest {
    @Mock
    private Connection connection;

    private Actor actor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        actor = new Actor();
    }

    @Test
    public void testItemNameA() {
        actor.setName("test object");

        assertEquals("a test object", actor.getName());
    }

    @Test
    public void testItemNameAn() {
        actor.setName("ugly test object");

        assertEquals("an ugly test object", actor.getName());
    }

    @Test
    public void testItemNameThe() {
        actor.setName("the test object");

        assertEquals("the test object", actor.getName());
    }

    @Test
    public void testPlayerName() {
        actor.setConnection(connection);
        actor.setName("Scion");

        assertEquals("Scion", actor.getName());
    }
}
