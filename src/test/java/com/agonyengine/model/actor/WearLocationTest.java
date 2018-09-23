package com.agonyengine.model.actor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WearLocationTest {
    @Test
    public void testIndex() {
        assertEquals(0, WearLocation.HEAD.getIndex());
    }

    @Test
    public void testConverter() {
        new WearLocation.Converter();
    }
}
