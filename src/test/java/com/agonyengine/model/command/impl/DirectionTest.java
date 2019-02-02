package com.agonyengine.model.command.impl;

import com.agonyengine.model.map.Direction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class DirectionTest {
    private Direction direction;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        direction = Direction.WEST;
    }

    @Test
    public void testName() {
        assertEquals("west", direction.getName());
    }

    @Test
    public void testOpposite() {
        assertEquals("east", direction.getOpposite());
    }

    @Test
    public void testX() {
        assertEquals(-1, direction.getX());
    }

    @Test
    public void testY() {
        assertEquals(0, direction.getY());
    }

    @Test
    public void testZ() {
        assertEquals(0, direction.getZ());
    }
}
