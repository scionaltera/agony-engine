package com.agonyengine.model.util;

import com.agonyengine.model.actor.GameMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class LocationTest {
    private Location location;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        location = new Location();
    }

    @Test
    public void testGameMap() {
        GameMap map = new GameMap();

        location.setGameMap(map);

        assertEquals(map, location.getGameMap());
    }

    @Test
    public void testX() {
        int x = 5;

        location.setX(x);

        assertEquals(x, (int)location.getX());
    }

    @Test
    public void testY() {
        int y = 5;

        location.setY(y);

        assertEquals(y, (int)location.getY());
    }
}
