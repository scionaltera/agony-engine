package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class TilesetTest {
    private Tileset tileset;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        tileset = new Tileset();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        tileset.setId(id);

        assertEquals(id, tileset.getId());
    }

    @Test
    public void testName() {
        String name = "A Faraway Land";

        tileset.setName(name);

        assertEquals(name, tileset.getName());
    }

    @Test
    public void testTile() {
        Tile tile = new Tile();

        tile.setId(UUID.randomUUID());
        tile.setIndex(3);

        tileset.setTile(tile);

        assertEquals(tile, tileset.getTile(3));
    }
}
