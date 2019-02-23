package com.agonyengine.config;

import com.agonyengine.util.noise.FbmParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class WorldConfiguration {
    public static final long WIDTH = 1024 * 10;
    public static final long HEIGHT = 1024 * 10;
    public static final long START_X = WIDTH / 2;
    public static final long START_Y = HEIGHT / 2;
    public static final long START_Z = 0L;

    @Value("${agonyengine.worldgen.seed}")
    private long worldGenSeed;

    @Bean(name = "worldGenRandom")
    public Random getWorldGenRandom() {
        return new Random(worldGenSeed);
    }

    @Bean(name = "grossElevationParameters")
    public FbmParameters getGrossElevationParameters() {
        return new FbmParameters(
            getWorldGenRandom().nextLong(),
            HEIGHT,
            WIDTH,
            0.0003,
            1.0,
            2.1042,
            0.6,
            6
        );
    }

    @Bean(name = "elevationParameters")
    public FbmParameters getElevationParameters() {
        return new FbmParameters(
            getWorldGenRandom().nextLong(),
            HEIGHT,
            WIDTH,
            0.001,
            1.0,
            2.1042,
            0.7,
            6
        );
    }

    @Bean(name = "rainfallParameters")
    public FbmParameters getRainfallParameters() {
        return new FbmParameters(
            getWorldGenRandom().nextLong(),
            HEIGHT,
            WIDTH,
            0.001,
            1.0,
            2.1042,
            0.5,
            6
        );
    }
}
