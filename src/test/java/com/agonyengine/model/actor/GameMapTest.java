package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class GameMapTest {
    private byte[] defaultMap = new byte[] {
        0x07, 0x08, 0x09,
        0x04, 0x05, 0x06,
        0x01, 0x02, 0x03
    };

    private GameMap map;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        map = new GameMap(3, defaultMap);
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        map.setId(id);

        assertEquals(id, map.getId());
    }

    @Test
    public void testWidth() {
        assertEquals(3, map.getWidth());

        map.setWidth(5);

        assertEquals(5, map.getWidth());
    }

    @Test
    public void testTiles() {
        byte[] updatedMap = new byte[] {
            0x10, 0x11, 0x12,
            0x0D, 0x0E, 0x0F,
            0x0A, 0x0B, 0x0C
        };

        assertEquals(defaultMap, map.getTiles());

        map.setTiles(updatedMap);

        assertEquals(updatedMap, map.getTiles());
    }

    @Test
    public void testSetGetTile() {
        assertEquals(0x05, map.getTile(1, 1));

        map.setTile(1, 1, (byte)0x7F);

        assertEquals((byte)0x7F, map.getTile(1, 1));
    }
}
