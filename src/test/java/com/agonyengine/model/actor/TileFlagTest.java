package com.agonyengine.model.actor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TileFlagTest {
    @Test
    public void testIndex() {
        assertEquals(0, TileFlag.IMPASSABLE.getIndex());
    }

    @Test
    public void testConverter() {
        new TileFlag.Converter();
    }
}
