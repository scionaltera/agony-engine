package com.agonyengine.model.generator;

import com.agonyengine.model.actor.GameMap;
import com.agonyengine.model.actor.Tile;
import com.agonyengine.model.actor.TileFlag;
import com.agonyengine.model.actor.Tileset;
import com.agonyengine.repository.GameMapRepository;
import com.agonyengine.repository.TileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.agonyengine.model.actor.GameMap.CURRENT_MAP_VERSION;
import static com.agonyengine.model.actor.GameMap.NO_UPDATE_VERSION;

@Component
public class MapGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapGenerator.class);
    private static final int MAP_ADDITIONAL_WIDTH = 15;

    static final int MAP_MIN_WIDTH = 10;

    private final Random RANDOM;

    private GameMapRepository gameMapRepository;
    private TileRepository tileRepository;

    @Inject
    public MapGenerator(GameMapRepository gameMapRepository, TileRepository tileRepository) {
        this.gameMapRepository = gameMapRepository;
        this.tileRepository = tileRepository;

        RANDOM = new Random();
    }

    MapGenerator(GameMapRepository gameMapRepository, TileRepository tileRepository, Random random) {
        this.gameMapRepository = gameMapRepository;
        this.tileRepository = tileRepository;

        RANDOM = random;
    }

    public GameMap generateMap(Tileset tileset) {
        GameMap map = createBlankMap(tileset);

        List<Tile> wilderness = findTilesByFlag(map.getTileset(), TileFlag.WILDERNESS);
        List<Tile> impassable = findTilesByFlag(map.getTileset(), TileFlag.IMPASSABLE);

        floodFill(map, wilderness);

        for (int i = 0; i < RANDOM.nextInt(3); i++) {
            drawCircle(
                map,
                impassable.get(0),
                RANDOM.nextInt(map.getWidth()),
                RANDOM.nextInt(map.getWidth()),
                RANDOM.nextInt(map.getWidth() / 3));
        }

        map.setVersion(CURRENT_MAP_VERSION);

        return gameMapRepository.save(map);
    }

    public GameMap updateMap(GameMap map) {
        if (map.getVersion() == CURRENT_MAP_VERSION || map.getVersion() == NO_UPDATE_VERSION) {
            return map;
        }

        LOGGER.info("Updating map {} from version {} to {}", map.getId(), map.getVersion(), CURRENT_MAP_VERSION);

        List<Tile> wilderness = findTilesByFlag(map.getTileset(), TileFlag.WILDERNESS);
        List<Tile> impassable = findTilesByFlag(map.getTileset(), TileFlag.IMPASSABLE);

        // Required changes between previous version and latest version go here.

        map.setVersion(CURRENT_MAP_VERSION);

        return gameMapRepository.save(map);
    }

    GameMap createBlankMap(Tileset tileset) {
        int width = MAP_MIN_WIDTH + RANDOM.nextInt(MAP_ADDITIONAL_WIDTH);
        GameMap map = new GameMap(width, new byte[width * width]);

        map.setTileset(tileset);

        return map;
    }

    void floodFill(GameMap map, List<Tile> tiles) {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getWidth(); y++) {
                Collections.shuffle(tiles);
                map.setTile(x, y, (byte)tiles.get(0).getIndex());
            }
        }
    }

    void drawSquare(GameMap map, Tile tile, int centerX, int centerY, int radius) {
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                try {
                    map.setTile(x, y, (byte) tile.getIndex());
                } catch (IllegalArgumentException e) {
                    LOGGER.trace("Square is partially outside map boundaries: ({}, {})", x, y);
                }
            }
        }
    }

    void drawCircle(GameMap map, Tile tile, int centerX, int centerY, int radius) {
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                if (distance(centerX, centerY, x, y) <= radius) {
                    try {
                        map.setTile(x, y, (byte) tile.getIndex());
                    } catch (IllegalArgumentException e) {
                        LOGGER.trace("Circle is partially outside map boundaries: ({}, {})", x, y);
                    }
                }
            }
        }
    }

    private List<Tile> findTilesByFlag(Tileset tileset, TileFlag flag) {
        List<Tile> tiles = tileRepository.findByTileset(tileset);

        return tiles.stream()
            .filter(tile -> tile.getFlags().contains(flag))
            .collect(Collectors.toList());
    }

    double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
