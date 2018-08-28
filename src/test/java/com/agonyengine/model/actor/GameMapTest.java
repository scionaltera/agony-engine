package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameMapTest {
    private byte[] defaultMap = new byte[] {
        0x01, 0x02, 0x03,
        0x04, 0x05, 0x06,
        0x07, 0x08, 0x09,
    };

    private Map<Integer, Tile> tiles = new HashMap<>();
    private GameMap map;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Tileset tileset = new Tileset();

        for (int i = 1; i < 10; i++) {
            Tile tile = new Tile();

            tile.setId(UUID.randomUUID());
            tile.setIndex(i);

            tileset.setTile(tile);
            tiles.put(tile.getIndex(), tile);
        }

        map = new GameMap(3, defaultMap);
        map.setTileset(tileset);
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
            0x0A, 0x0B, 0x0C,
            0x0D, 0x0E, 0x0F,
            0x10, 0x11, 0x12,
        };

        assertEquals(defaultMap, map.getTiles());

        map.setTiles(updatedMap);

        assertEquals(updatedMap, map.getTiles());
    }

    @Test
    public void testSetGetTile() {
        assertEquals(tiles.get(5), map.getTile(1, 1));

        map.setTile(1, 1, (byte)0x01);

        assertEquals(tiles.get(1), map.getTile(1, 1));
    }

    @Test
    public void testGetTiles() {
        assertEquals(tiles.get(1), map.getTile(0, 0));
        assertEquals(tiles.get(2), map.getTile(1, 0));
        assertEquals(tiles.get(3), map.getTile(2, 0));
        assertEquals(tiles.get(4), map.getTile(0, 1));
        assertEquals(tiles.get(5), map.getTile(1, 1));
        assertEquals(tiles.get(6), map.getTile(2, 1));
        assertEquals(tiles.get(7), map.getTile(0, 2));
        assertEquals(tiles.get(8), map.getTile(1, 2));
        assertEquals(tiles.get(9), map.getTile(2, 2));
    }

    @Test
    public void testTileset() {
        Tileset tileset = new Tileset();

        map.setTileset(tileset);

        assertEquals(tileset, map.getTileset());
    }

    @Test
    public void testCorners() {
        assertFalse(map.hasTile(-1, 0));
        assertFalse(map.hasTile(0, -1));
        assertTrue(map.hasTile(0, 0));

        assertFalse(map.hasTile(3, 0));
        assertFalse(map.hasTile(2, -1));
        assertTrue(map.hasTile(2, 0));

        assertFalse(map.hasTile(0, 3));
        assertFalse(map.hasTile(-1, 2));
        assertTrue(map.hasTile(0, 2));

        assertFalse(map.hasTile(2, 3));
        assertFalse(map.hasTile(3, 2));
        assertTrue(map.hasTile(2, 2));
    }
}
