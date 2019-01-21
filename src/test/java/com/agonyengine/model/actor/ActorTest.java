package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;

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
    public void testId() {
        UUID id = UUID.randomUUID();

        actor.setId(id);

        assertEquals(id, actor.getId());
    }

    @Test
    public void testNameTokens() {
        actor.setName("test object");

        assertArrayEquals(new String[] {"test", "object"}, actor.getNameTokens());
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

    @Test
    public void testPlainName() {
        actor.setName("test object");

        assertEquals("test object", actor.getPlainName());
    }

    @Test
    public void testPronoun() {
        Pronoun pronoun = mock(Pronoun.class);

        actor.setPronoun(pronoun);

        assertEquals(pronoun, actor.getPronoun());
    }

    @Test
    public void testConnection() {
        Connection connection = mock(Connection.class);

        actor.setConnection(connection);

        assertEquals(connection, actor.getConnection());
    }

    @Test
    public void testCreatureInfo() {
        CreatureInfo creatureInfo = mock(CreatureInfo.class);

        actor.setCreatureInfo(creatureInfo);

        assertEquals(creatureInfo, actor.getCreatureInfo());
    }

    @Test
    public void testItemInfo() {
        ItemInfo itemInfo = new ItemInfo();

        actor.setItemInfo(itemInfo);

        assertEquals(itemInfo, actor.getItemInfo());
    }

    @Test
    public void testInventory() {
        UUID roomId = UUID.randomUUID();

        actor.setInventoryId(roomId);

        assertEquals(roomId, actor.getInventoryId());
    }
}
