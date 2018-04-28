package com.agonyengine.model.stomp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameOutputTest {
    private GameOutput output;

    @Test
    public void testDefaultConstructor() {
        output = new GameOutput();

        assertTrue(output.getOutput().isEmpty());
    }

    @Test
    public void testVarargConstructor() {
        output = new GameOutput("Alpha", "Beta");

        assertEquals("Alpha", output.getOutput().get(0));
        assertEquals("Beta", output.getOutput().get(1));
    }

    @Test
    public void testAppend() {
        output = new GameOutput();

        assertTrue(output.getOutput().isEmpty());

        output = output.append("Alpha").append("Beta");

        assertEquals("Alpha", output.getOutput().get(0));
        assertEquals("Beta", output.getOutput().get(1));
    }
}
