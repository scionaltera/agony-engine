package com.agonyengine.service;

import com.agonyengine.model.map.Biome;
import com.agonyengine.repository.RoomRepository;
import com.agonyengine.util.noise.FbmParameters;
import com.agonyengine.util.noise.MapFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BiomeServiceTest {
    private static final long WIDTH = 1024 * 10;
    private static final long HEIGHT = 1024 * 10;
    private static final Random RANDOM = new Random(1L);

    @Mock
    private RoomRepository roomRepository;

    private MapFactory mapFactory;
    private FbmParameters grossElevationParameters = new FbmParameters(
        RANDOM.nextLong(),
        HEIGHT,
        WIDTH,
        0.0003,
        1.0,
        2.1042,
        0.6,
        6
    );;
    private FbmParameters elevationParameters = new FbmParameters(
        RANDOM.nextLong(),
        HEIGHT,
        WIDTH,
        0.001,
        1.0,
        2.1042,
        0.7,
        6
    );
    private FbmParameters rainfallParameters = new FbmParameters(
        RANDOM.nextLong(),
        HEIGHT,
        WIDTH,
        0.001,
        1.0,
        2.1042,
        0.5,
        6
    );

    private BiomeService biomeService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mapFactory = new MapFactory();

        biomeService = new BiomeService(
            mapFactory,
            roomRepository,
            grossElevationParameters,
            elevationParameters,
            rainfallParameters);
    }

    @Test
    public void testComputeOcean() {
        Biome biome = biomeService.computeBiome(new long[] {0, 0});

        assertEquals("Ocean", biome.getName());
    }

    @Test
    public void testComputeSubtropicalDesert() {
        Biome biome = biomeService.computeBiome(new long[] {1, 1});

        assertEquals("Subtropical Desert", biome.getName());
    }

    @Test
    public void testComputeGrassland() {
        Biome biome1 = biomeService.computeBiome(new long[] {1, 2});
        Biome biome2 = biomeService.computeBiome(new long[] {2, 2});
        Biome biome3 = biomeService.computeBiome(new long[] {2, 3});

        assertEquals("Grassland", biome1.getName());
        assertEquals("Grassland", biome2.getName());
        assertEquals("Grassland", biome3.getName());
    }

    @Test
    public void testComputeTropicalSeasonalForest() {
        Biome biome1 = biomeService.computeBiome(new long[] {1, 3});
        Biome biome2 = biomeService.computeBiome(new long[] {1, 4});

        assertEquals("Tropical Seasonal Forest", biome1.getName());
        assertEquals("Tropical Seasonal Forest", biome2.getName());
    }

    @Test
    public void testComputeTropicalRainForest() {
        Biome biome1 = biomeService.computeBiome(new long[] {1, 5});
        Biome biome2 = biomeService.computeBiome(new long[] {1, 6});

        assertEquals("Tropical Rain Forest", biome1.getName());
        assertEquals("Tropical Rain Forest", biome2.getName());
    }

    @Test
    public void testComputeTemperateForest() {
        Biome biome = biomeService.computeBiome(new long[] {2, 1});

        assertEquals("Temperate Forest", biome.getName());
    }

    @Test
    public void testComputeTemperateDeciduousForest() {
        Biome biome1 = biomeService.computeBiome(new long[] {2, 4});
        Biome biome2 = biomeService.computeBiome(new long[] {2, 5});

        assertEquals("Temperate Deciduous Forest", biome1.getName());
        assertEquals("Temperate Deciduous Forest", biome2.getName());
    }

    @Test
    public void testComputeTemperateRainForest() {
        Biome biome = biomeService.computeBiome(new long[]{2, 6});

        assertEquals("Temperate Rain Forest", biome.getName());
    }

    @Test
    public void testComputeTemperateDesert() {
        Biome biome1 = biomeService.computeBiome(new long[] {3, 1});
        Biome biome2 = biomeService.computeBiome(new long[] {3, 1});

        assertEquals("Temperate Desert", biome1.getName());
        assertEquals("Temperate Desert", biome2.getName());
    }

    @Test
    public void testComputeShrubland() {
        Biome biome1 = biomeService.computeBiome(new long[] {3, 3});
        Biome biome2 = biomeService.computeBiome(new long[] {3, 4});

        assertEquals("Shrubland", biome1.getName());
        assertEquals("Shrubland", biome2.getName());
    }

    @Test
    public void testComputeTaiga() {
        Biome biome1 = biomeService.computeBiome(new long[] {3, 5});
        Biome biome2 = biomeService.computeBiome(new long[] {3, 6});

        assertEquals("Taiga", biome1.getName());
        assertEquals("Taiga", biome2.getName());
    }

    @Test
    public void testComputeScorched() {
        Biome biome = biomeService.computeBiome(new long[]{4, 1});

        assertEquals("Scorched", biome.getName());
    }

    @Test
    public void testComputeBare() {
        Biome biome = biomeService.computeBiome(new long[]{4, 2});

        assertEquals("Bare", biome.getName());
    }

    @Test
    public void testComputeTundra() {
        Biome biome = biomeService.computeBiome(new long[]{4, 3});

        assertEquals("Tundra", biome.getName());
    }

    @Test
    public void testComputeSnow() {
        Biome biome1 = biomeService.computeBiome(new long[]{4, 4});
        Biome biome2 = biomeService.computeBiome(new long[]{4, 5});
        Biome biome3 = biomeService.computeBiome(new long[]{4, 6});

        assertEquals("Snow", biome1.getName());
        assertEquals("Snow", biome2.getName());
        assertEquals("Snow", biome3.getName());
    }
}
