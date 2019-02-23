package com.agonyengine.service;

import com.agonyengine.config.WorldConfiguration;
import com.agonyengine.model.map.Biome;
import com.agonyengine.util.noise.FbmParameters;
import com.agonyengine.util.noise.MapFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class BiomeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeService.class);

    private MapFactory mapFactory;

    private FbmParameters grossElevationParameters;
    private FbmParameters elevationParameters;
    private FbmParameters rainfallParameters;

    private long elevationHiWater = 0;
    private long elevationLoWater = 10;
    private long rainfallHiWater = 0;
    private long rainfallLoWater = 10;

    public BiomeService(MapFactory mapFactory,
                        @Named("grossElevationParameters") FbmParameters grossElevationParameters,
                        @Named("elevationParameters") FbmParameters elevationParameters,
                        @Named("rainfallParameters") FbmParameters rainfallParameters) {
        this.mapFactory = mapFactory;
        this.grossElevationParameters = grossElevationParameters;
        this.elevationParameters = elevationParameters;
        this.rainfallParameters = rainfallParameters;
    }

    @PostConstruct
    public void setup() {
        LOGGER.info("Writing world map image...");
        debugImages();

        LOGGER.info("Elevation lo/hi: {}/{}", elevationLoWater, elevationHiWater);
        LOGGER.info("Rainfall lo/hi: {}/{}", rainfallLoWater, rainfallHiWater);
    }

    private void debugImages() {
        final int IMG_WIDTH = 1024 * 2;
        final int IMG_HEIGHT = 1024 * 2;
        final int IMG_START_X = (int)(WorldConfiguration.WIDTH / 2L) - (IMG_WIDTH / 2);
        final int IMG_START_Y = (int)(WorldConfiguration.HEIGHT / 2L) - (IMG_HEIGHT / 2);

        LOGGER.info("FBM seed values: {} {} {}",
            grossElevationParameters.getSeed(),
            elevationParameters.getSeed(),
            rainfallParameters.getSeed());

        try {
            BufferedImage biomeImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_ARGB);

            for (int x = 0; x < IMG_WIDTH; x++) {
                for (int y = 0; y < IMG_HEIGHT; y++) {
                    long[] climate = computeClimate(IMG_START_X + x, IMG_START_Y + y);

                    biomeImage.setRGB(x, y, computeBiomeRgb(climate[0], climate[1]));
                }
            }

            ImageIO.write(biomeImage, "PNG", new File("images/biome.png"));
        } catch (IOException e) {
            LOGGER.error("IOE writing images!", e);
        }
    }

    public Biome computeBiome(long x, long y) {
        long[] climate = computeClimate(x, y);
        long elevation = climate[0];
        long rainfall = climate[1];
        Biome biome = new Biome();

        if (elevation <= 0) {
            biome.setName("Ocean");
        } else if (elevation == 1) {
            if (rainfall == 1) {
                biome.setName("Subtropical Desert");
            } else if (rainfall == 2) {
                biome.setName("Grassland");
            } else if (rainfall == 3 || rainfall == 4) {
                biome.setName("Tropical Seasonal Forest");
            } else if (rainfall == 5 || rainfall == 6) {
                biome.setName("Tropical Rain Forest");
            }
        } else if (elevation == 2) {
            if (rainfall == 1) {
                biome.setName("Temperate Forest");
            } else if (rainfall == 2 || rainfall == 3) {
                biome.setName("Grassland");
            } else if (rainfall == 4 || rainfall == 5) {
                biome.setName("Temperate Deciduous Forest");
            } else if (rainfall == 6) {
                biome.setName("Temperate Rain Forest");
            }
        } else if (elevation == 3) {
            if (rainfall == 1 || rainfall == 2) {
                biome.setName("Temperate Desert");
            } else if (rainfall == 3 || rainfall == 4) {
                biome.setName("Shrubland");
            } else if (rainfall == 5 || rainfall == 6) {
                biome.setName("Taiga");
            }
        } else if (elevation == 4) {
            if (rainfall == 1) {
                biome.setName("Scorched");
            } else if (rainfall == 2) {
                biome.setName("Bare");
            } else if (rainfall == 3) {
                biome.setName("Tundra");
            } else if (rainfall == 4 || rainfall == 5 || rainfall == 6) {
                biome.setName("Snow");
            }
        }

        return biome;
    }

    private long[] computeClimate(long x, long y) {
        double grossElevation = mapFactory.generateCell(grossElevationParameters, x, y);
        double elevation = mapFactory.generateCell(elevationParameters, x, y);
        double rainfall = mapFactory.generateCell(rainfallParameters, x, y);

        elevation = (elevation + grossElevation) / 2.0; // apply gross elevation
        elevation += 0.13 * ((y - (WorldConfiguration.HEIGHT / 2.0)) / WorldConfiguration.HEIGHT); // apply elevation gradient
        rainfall -= 0.25 * ((y - (WorldConfiguration.HEIGHT / 2.0)) / WorldConfiguration.HEIGHT); // apply rainfall gradient

        long e = clamp(Math.round((elevation * 16) - 7.3), 0, 4);
        long r = clamp(Math.round(rainfall * 7), 1, 6);

        return new long[] { e, r };
    }

    private long clamp(long in, long min, long max) {
        return Math.min(max, Math.max(min, in));
    }

    private int computeBiomeRgb(long elevation, long rainfall) {
        elevationHiWater = Math.max(elevationHiWater, elevation);
        elevationLoWater = Math.min(elevationLoWater, elevation);
        rainfallHiWater = Math.max(rainfallHiWater, rainfall);
        rainfallLoWater = Math.min(rainfallLoWater, rainfall);

        if (elevation <= 0) {
            return 0xFF002288; // ocean
        } else if (elevation == 1) {
            if (rainfall == 1) {
                return 0xFF02b98b; // subtropical desert
            } else if (rainfall == 2) {
                return 0xFF88aa55; // grassland
            } else if (rainfall == 3 || rainfall == 4) {
                return 0xFF559944; // tropical seasonal forest
            } else if (rainfall == 5 || rainfall == 6) {
                return 0xFF337755; // tropical rain forest
            }
        } else if (elevation == 2) {
            if (rainfall == 1) {
                return 0xFFc9d29b; // temperate desert
            } else if (rainfall == 2 || rainfall == 3) {
                return 0xFF88aa55; // grassland
            } else if (rainfall == 4 || rainfall == 5) {
                return 0xFF679459; // temperate deciduous forest
            } else if (rainfall == 6) {
                return 0xFF448855; // temperate rain forest
            }
        } else if (elevation == 3) {
            if (rainfall == 1 || rainfall == 2) {
                return 0xFFc9d29b; // temperate desert
            } else if (rainfall == 3 || rainfall == 4) {
                return 0xFF889977; // shrubland
            } else if (rainfall == 5 || rainfall == 6) {
                return 0xFF99aa77; // taiga
            }
        } else if (elevation == 4) {
            if (rainfall == 1) {
                return 0xFF555555; // scorched
            } else if (rainfall == 2) {
                return 0xFF888888; // bare
            } else if (rainfall == 3) {
                return 0xFFbbbbaa; // tundra
            } else if (rainfall == 4 || rainfall == 5 || rainfall == 6) {
                return 0xFFffffff; // snow
            }
        }

        return 0xFFff0000; // red - shouldn't get here
    }
//
//    private int computeRgb(int value) {
//        int rgb = 0xFF000000;
//
//        rgb = rgb | (value); // blue
//        rgb = rgb | (value << 8); // green
//        rgb = rgb | (value << 16); // red
//
//        return rgb;
//    }
}
