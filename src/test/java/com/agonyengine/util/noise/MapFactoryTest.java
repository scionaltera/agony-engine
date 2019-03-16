package com.agonyengine.util.noise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MapFactoryTest {
    private static final long WIDTH = 1024 * 10;
    private static final long HEIGHT = 1024 * 10;
    private static final Random RANDOM = new Random(1L);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private FbmParameters parameters = new FbmParameters(
        RANDOM.nextLong(),
        HEIGHT,
        WIDTH,
        0.001,
        1.0,
        2.1042,
        0.7,
        6
    );

    private MapFactory mapFactory;

    @Before
    public void setUp() {
        mapFactory = new MapFactory();
    }

    @Test
    public void testGenerateCell() throws IOException {
        Double[] expectedValues = OBJECT_MAPPER.readValue(
            MapFactoryTest.class.getResourceAsStream("/mapFactoryExpectedCells.json"),
            Double[].class
        );
        int count = 0;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                double value = mapFactory.generateCell(parameters, x, y);

                assertEquals(expectedValues[count++], value, 0.00001);
            }
        }
    }
}
