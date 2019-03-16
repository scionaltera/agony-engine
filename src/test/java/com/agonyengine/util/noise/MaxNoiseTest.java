package com.agonyengine.util.noise;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxNoiseTest {
    private MaxNoise maxNoise;

    @Before
    public void setUp() {
        maxNoise = new MaxNoise();
    }

    @Test
    public void testMaxNoise() {
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                assertEquals(1.0f, maxNoise.eval(x * 100, y * 100), 0.01);
            }
        }
    }
}

