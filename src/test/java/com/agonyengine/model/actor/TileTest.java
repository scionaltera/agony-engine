package com.agonyengine.model.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.EnumSet;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TileTest {
    private Tile tile;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        tile = new Tile();
    }

    @Test
    public void testId() {
        UUID id = UUID.randomUUID();

        tile.setId(id);

        assertEquals(id, tile.getId());
    }

    @Test
    public void testIndex() {
        int index = 1;

        tile.setIndex(index);

        assertEquals(index, tile.getIndex());
    }

    @Test
    public void testRoomTitle() {
        String title = "Inner Sanctum";

        tile.setRoomTitle(title);

        assertEquals(title, tile.getRoomTitle());
    }

    @Test
    public void testRoomDescription() {
        String description = "An empty room, ten feet by ten feet.";

        tile.setRoomDescription(description);

        assertEquals(description, tile.getRoomDescription());
    }

    @Test
    public void testRoomDescriptionWrap() {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < 100; i++) {
            buf.append("word ");
        }

        tile.setRoomDescription(buf.toString().trim());

        String out = tile.getRoomDescription();

        assertTrue(out.contains("<br/>"));
        assertTrue(out.contains("word "));
    }

    @Test
    public void testTileset() {
        Tileset tileset = mock(Tileset.class);

        tile.setTileset(tileset);

        assertEquals(tileset, tile.getTileset());
    }

    @Test
    public void testFlags() {
        EnumSet<TileFlag> flags = EnumSet.noneOf(TileFlag.class);

        flags.add(TileFlag.IMPASSABLE);

        tile.setFlags(flags);

        assertEquals(flags, tile.getFlags());
        assertTrue(tile.getFlags().contains(TileFlag.IMPASSABLE));
    }
}
