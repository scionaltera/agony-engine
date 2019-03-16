package com.agonyengine.util.noise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AdjustedNoiseTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private AdjustedNoise adjustedNoise;

    @Before
    public void setUp() {
        adjustedNoise = new AdjustedNoise(1L);
    }

    @Test
    public void testEval() throws IOException {
        Double[] expectedValues = OBJECT_MAPPER.readValue(
            MapFactoryTest.class.getResourceAsStream("/adjustedNoiseExpectedValues.json"),
            Double[].class
        );
        int count = 0;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                assertEquals(expectedValues[count++], adjustedNoise.eval(x * 100, y * 100), 0.00001);
            }
        }
    }
}
